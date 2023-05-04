package executors.model;

import utils.Folder;
import utils.Result;
import utils.SetupInfo;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

public class DirectoryScanner {
    private ForkJoinPool forkJoinPool = new ForkJoinPool();
    private Result midReport;

    public Result getMidReport(SetupInfo setupInfo) {
        try {
            this.resetMidReport(setupInfo);
            forkJoinPool.execute(new ScanFolderTask(Folder.fromDirectory(new File(setupInfo.directory())), setupInfo, midReport));
            return this.midReport;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void resetMidReport(SetupInfo setupInfo){
        midReport = new Result(setupInfo.nIntervals(), setupInfo.lastIntervalLowerBound(), setupInfo.nFiles());
    }

    public ForkJoinTask<Result> getFinalResult(SetupInfo setupInfo){
        try {
            this.resetMidReport(setupInfo);
            return forkJoinPool.submit(new ScanFolderTask(Folder.fromDirectory(new File(setupInfo.directory())), setupInfo, midReport));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopExecution(){
        this.forkJoinPool.shutdownNow();
        forkJoinPool = new ForkJoinPool();
    }
}