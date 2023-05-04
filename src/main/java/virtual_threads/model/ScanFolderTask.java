package virtual_threads.model;

import utils.Document;
import utils.Folder;
import utils.Result;

import java.util.LinkedList;
import java.util.List;

public class ScanFolderTask implements Runnable{

    private final Folder folder;
    private final Result result;

    public ScanFolderTask(Folder folder, Result result){
        this.folder = folder;
        this.result = result;
    }

    @Override
    public void run() {
        final List<Thread> threads = new LinkedList<>();

        for (Folder subFolder : folder.getSubFolders()) {
            Thread t = Thread.ofVirtual().unstarted(new ScanFolderTask(subFolder, result));
            threads.add(t);
            t.start();
        }

        for (Document document : folder.getDocuments()) {
            Thread t = Thread.ofVirtual().unstarted(new CountLinesTask(document, result));
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
