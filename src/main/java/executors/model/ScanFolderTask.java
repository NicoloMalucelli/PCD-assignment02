package executors.model;

import utils.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ScanFolderTask extends RecursiveTask<Result> {

    private final Folder folder;
    private final SetupInfo setupInfo;
    private final Result midReport;

    public ScanFolderTask(Folder folder, SetupInfo setupInfo, Result midReport){
        super();
        this.folder = folder;
        this.setupInfo = setupInfo;
        this.midReport = midReport;
    }


    @Override
    protected Result compute() {
        final List<RecursiveTask<AnalyzedFile>> countLinesTasks = new LinkedList<>();
        final List<RecursiveTask<Result>> scanFolderTasks = new LinkedList<>();

        for(Document document : folder.getDocuments()){
            CountLinesTask task = new CountLinesTask(document, midReport);
            countLinesTasks.add(task);
            task.fork();
        }

        for(Folder folder : folder.getSubFolders()){
            ScanFolderTask task = new ScanFolderTask(folder, setupInfo, midReport);
            scanFolderTasks.add(task);
            task.fork();
        }

        final Result result = new Result(setupInfo.nIntervals(), setupInfo.lastIntervalLowerBound(), setupInfo.nFiles());
        for(RecursiveTask<AnalyzedFile> task : countLinesTasks){
            result.add(task.join());
        }

        for(RecursiveTask<Result> task : scanFolderTasks){
            result.merge(task.join());
        }

        return result;
    }
}