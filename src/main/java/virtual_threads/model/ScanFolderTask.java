package virtual_threads.model;

import utils.Document;
import utils.Folder;
import utils.Result;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ScanFolderTask implements Runnable{

    private final Folder folder;
    private final Result result;
    private final CompletableFuture<Void> stopExecution;

    public ScanFolderTask(Folder folder, Result result, CompletableFuture<Void> stopExecution){
        this.folder = folder;
        this.result = result;
        this.stopExecution = stopExecution;
    }

    @Override
    public void run() {
        stopExecution.thenRun(() -> {
            Thread.currentThread().interrupt();
        });

        final List<Thread> threads = new LinkedList<>();

        for (Folder subFolder : folder.getSubFolders()) {
            Thread t = Thread.ofVirtual().unstarted(new ScanFolderTask(subFolder, result, stopExecution));
            threads.add(t);
            t.start();
        }

        for (Document document : folder.getDocuments()) {
            Thread t = Thread.ofVirtual().unstarted(new CountLinesTask(document, result, stopExecution));
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {

            }
        }
    }


}
