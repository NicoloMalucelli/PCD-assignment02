package rx.controller;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import utils.AnalyzedFile;
import utils.Folder;
import utils.Result;
import utils.SetupInfo;

import java.io.File;

public class Controller {

    public Single<Result> getReport(SetupInfo setupInfo){
        Result emptyResult = new Result(setupInfo.nIntervals(), setupInfo.lastIntervalLowerBound(), setupInfo.nFiles());
        return this.analyzeFolder(setupInfo.directory())
                .reduce(emptyResult, (result, af) -> result.accumulate(af));
    }

    private Flowable<Folder> getSubFolders(Folder folder){
        return Flowable.fromIterable(folder.getSubFolders())
                .flatMap(f -> this.getSubFolders(f))
                .concatWith(Flowable.just(folder));
    }

    private Flowable<AnalyzedFile> analyzeFolder(String folder) {
        return Flowable.just(folder)
                .map(p -> Folder.fromDirectory(new File(String.valueOf(p))))
                .flatMap(f -> this.getSubFolders(f))
                .flatMap(f -> Flowable.fromIterable(f.getDocuments()))
                .map(d -> new AnalyzedFile(d.getPath(), d.countLines()));
    }
}
