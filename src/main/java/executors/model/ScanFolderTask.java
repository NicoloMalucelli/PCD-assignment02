package executors.model;

import utils.AnalyzedFile;
import utils.Result;

import javax.print.Doc;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ScanFolderTask extends RecursiveTask<Result> {

    private final Folder folder;

    public ScanFolderTask(Folder folder){
        super();
        this.folder = folder;
    }


    @Override
    protected Result compute() {
        final List<RecursiveTask<AnalyzedFile>> countLinesTasks = new LinkedList<>();
        final List<RecursiveTask<Result>> scanFolderTasks = new LinkedList<>();

        for(Document document : folder.getDocuments()){
            CountLinesTask task = new CountLinesTask(document);
            countLinesTasks.add(task);
            task.fork();
        }

        for(Folder folder : folder.getSubFolders()){
            ScanFolderTask task = new ScanFolderTask(folder);
            scanFolderTasks.add(task);
            task.fork();
        }

        final Result result = new Result(10, 100);
        for(RecursiveTask<AnalyzedFile> task : countLinesTasks){
            result.add(task.join());
        }

        for(RecursiveTask<Result> task : scanFolderTasks){
            result.merge(task.join());
        }

        return result;
    }
}