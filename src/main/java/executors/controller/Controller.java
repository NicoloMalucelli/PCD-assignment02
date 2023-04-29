package executors.controller;

import executors.model.Folder;
import executors.model.Model;
import utils.SetupInfo;
import utils.Result;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinTask;

public class Controller implements SourceAnalyzer{

    private final Model model;
    public Controller(Model model){
        this.model = model;
    }

    @Override
    public ForkJoinTask<Result> getReport(SetupInfo setupInfo) {
        return this.model.getDirectoryScanner().getFinalResult(setupInfo);
    }

    @Override
    public Result analyzeSources(SetupInfo setupInfo) {
        return this.model.getDirectoryScanner().getMidReport(setupInfo);
    }

    public void stopExecution(){
        this.model.getDirectoryScanner().stopExecution();
    }

    public void processEvent(Runnable runnable){
        new Thread(runnable).start();
    }
}
