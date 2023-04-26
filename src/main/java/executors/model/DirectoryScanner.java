package executors.model;

import utils.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

public class DirectoryScanner {
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();
    private final CompletableFuture<Result> finalResult = new CompletableFuture<>();

    public void scan(Folder folder){
        this.finalResult.complete(forkJoinPool.invoke(new ScanFolderTask(folder)));
    }
    public CompletableFuture<Result> getFinalResult(){
        return this.finalResult;
    }
}