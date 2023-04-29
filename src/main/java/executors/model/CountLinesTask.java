package executors.model;

import utils.AnalyzedFile;
import utils.Result;

import java.util.concurrent.RecursiveTask;

public class CountLinesTask extends RecursiveTask<AnalyzedFile> {
    private final Document document;
    private final Result midReport;

    public CountLinesTask(Document document, Result midReport){
        super();
        this.document = document;
        this.midReport = midReport;
    }

    @Override
    protected AnalyzedFile compute() {
        final AnalyzedFile analyzedFile = new AnalyzedFile(document.getPath(), document.countLines());
        midReport.add(analyzedFile);
        return analyzedFile;
    }
}