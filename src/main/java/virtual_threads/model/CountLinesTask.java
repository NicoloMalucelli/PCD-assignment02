package virtual_threads.model;

import utils.AnalyzedFile;
import utils.Document;
import utils.Folder;
import utils.Result;

import java.util.concurrent.CompletableFuture;

public class CountLinesTask implements Runnable{

    private final Document document;
    private final Result result;

    public CountLinesTask(Document document, Result result, CompletableFuture<Void> stopExecution) {
        this.document = document;
        this.result = result;
        stopExecution.thenRun(() -> {
            Thread.currentThread().interrupt();
        });
    }

    @Override
    public void run() {
        final AnalyzedFile analyzedFile = new AnalyzedFile(document.getPath(), document.countLines());
        result.add(analyzedFile);
    }



}
