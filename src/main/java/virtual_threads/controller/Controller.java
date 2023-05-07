package virtual_threads.controller;

import utils.Folder;
import utils.Result;
import utils.SetupInfo;
import virtual_threads.model.Model;
import virtual_threads.model.ScanFolderTask;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class Controller implements SourceAnalyzer{

    private final Model model;

    public Controller(Model model){
        this.model = model;
    }

    @Override
    public CompletableFuture<Result> getReport(SetupInfo setupInfo) {
        model.init(setupInfo);
        final CompletableFuture<Result> result = new CompletableFuture<>();
        Thread.ofVirtual().start(() -> {
            Thread rootScanner = null;
            try {
                rootScanner = Thread.ofVirtual().unstarted(new ScanFolderTask(Folder.fromDirectory(new File(setupInfo.directory())), model.getResult(), model.getStopExecution()));
                rootScanner.start();
                try {
                    rootScanner.join();
                    result.complete(model.getResult());
                } catch (InterruptedException e) {

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return result;
    }

    @Override
    public Result analyzeSources(SetupInfo setupInfo, CompletableFuture<Void> executionEnded){
        model.init(setupInfo);
        Thread.ofVirtual().start(() -> {
            Thread rootScanner = null;
            try {
                rootScanner = Thread.ofVirtual().unstarted(new ScanFolderTask(Folder.fromDirectory(new File(setupInfo.directory())), model.getResult(), model.getStopExecution()));
                rootScanner.start();
                try {
                    rootScanner.join();
                } catch (InterruptedException e) {

                }
                executionEnded.complete(null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return model.getResult();
    }

    public void processEvent(Runnable runnable) {
        Thread.ofVirtual().start(runnable);
    }

    public void stopExecution() {
        this.model.getStopExecution().complete(null);
    }
}
