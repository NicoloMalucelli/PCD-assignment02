package virtual_threads.model;

import utils.Result;
import utils.SetupInfo;

import java.util.concurrent.CompletableFuture;

public class Model {

    private Result result;
    private CompletableFuture<Void> stopExecution;

    public void init(SetupInfo setupInfo){
        stopExecution = new CompletableFuture<>();
        result = new Result(setupInfo.nIntervals(), setupInfo.lastIntervalLowerBound(), setupInfo.nFiles());
    }
    public Result getResult(){
        return this.result;
    }
    public CompletableFuture<Void> getStopExecution(){
        return stopExecution;
    }
}
