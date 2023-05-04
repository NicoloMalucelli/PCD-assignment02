package virtual_threads.model;

import utils.AnalyzedFile;
import utils.Document;
import utils.Folder;
import utils.Result;

public class CountLinesTask implements Runnable{

    private final Document document;
    private final Result result;

    public CountLinesTask(Document document, Result result) {
        this.document = document;
        this.result = result;
    }

    @Override
    public void run() {
        final AnalyzedFile analyzedFile = new AnalyzedFile(document.getPath(), document.countLines());
        result.add(analyzedFile);
    }

}
