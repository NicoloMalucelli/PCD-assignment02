package rx.controller;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.parallel.ParallelFlowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import rx.model.Model;
import utils.AnalyzedFile;
import utils.Folder;
import utils.Result;
import utils.SetupInfo;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

public class Controller implements SourceAnalyzer {
    private final Model model;
    public Controller(Model model){
        this.model = model;
    }

    public void stop(){
        this.model.getShouldStop().set(true);
    }

    @Override
    public Single<Result> getReport(SetupInfo setupInfo){
        this.model.resetShouldStop();
        Result emptyResult = new Result(setupInfo.nIntervals(), setupInfo.lastIntervalLowerBound(), setupInfo.nFiles());
        return this.analyzeFolder(setupInfo.directory())
                .sequential()
                .reduce(emptyResult, (result, af) -> result.accumulate(af));
    }

    @Override
    public Flowable<Result> analyzeSources(SetupInfo setupInfo){
        this.model.resetShouldStop();
        Result result = new Result(setupInfo.nIntervals(), setupInfo.lastIntervalLowerBound(), setupInfo.nFiles());
        return this.analyzeFolder(setupInfo.directory())
                .sequential()
                .map(af -> result.accumulate(af));
    }

    private Flowable<Folder> getSubFolders(Folder folder){
        return Flowable.fromIterable(folder.getSubFolders())
                .skipWhile(e -> this.model.getShouldStop().get())
                .flatMap(f -> this.getSubFolders(f))
                .concatWith(Flowable.just(folder));
    }

    private ParallelFlowable<AnalyzedFile> analyzeFolder(String folder) {
        return Flowable.just(folder)
                .map(p -> Folder.fromDirectory(new File(String.valueOf(p))))
                .flatMap(f -> this.getSubFolders(f))
                .subscribeOn(Schedulers.io())
                .parallel()
                .runOn(Schedulers.computation())
                .flatMap(f -> Flowable.fromIterable(f.getDocuments()))
                .map(d -> new AnalyzedFile(d.getPath(), d.countLines()));
    }
}
