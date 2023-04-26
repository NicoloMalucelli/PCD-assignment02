package executors.model;

import utils.AnalyzedFile;

import java.util.concurrent.RecursiveTask;

public class CountLinesTask extends RecursiveTask<AnalyzedFile> {
    private final Document document;

    public CountLinesTask(Document document){
        super();
        this.document = document;
    }

    @Override
    protected AnalyzedFile compute() {
        return new AnalyzedFile(document.getPath(), document.countLines());
    }
}