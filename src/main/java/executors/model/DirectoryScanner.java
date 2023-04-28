package executors.model;

import utils.Result;
import utils.SetupInfo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

public class DirectoryScanner {
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();
    private final CompletableFuture<Result> finalResult = new CompletableFuture<>();
    private Result midReport;

    public Result getMidReport() {
        return midReport;
    }
    public void resetMidReport(SetupInfo setupInfo){
        midReport = new Result(setupInfo.nIntervals(), setupInfo.lastIntervalLowerBound());
    }

    public void scan(Folder folder, SetupInfo setupInfo){
        this.finalResult.complete(forkJoinPool.invoke(new ScanFolderTask(folder, setupInfo, midReport)));
    }
    public CompletableFuture<Result> getFinalResult(){
        return this.finalResult;
    }
}