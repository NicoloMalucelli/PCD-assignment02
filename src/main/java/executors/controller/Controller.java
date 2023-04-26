package executors.controller;

import executors.model.Folder;
import executors.model.Model;
import utils.SetupInfo;
import utils.Result;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class Controller implements SourceAnalyzer{

    private final Model model;
    public Controller(Model model){
        this.model = model;
    }

    @Override
    public CompletableFuture<Result> getReport(SetupInfo setupInfo) {
        return this.model.getDirectoryScanner().getFinalResult();
    }

    public void startScan(SetupInfo setupInfo) throws IOException {
        this.model.getDirectoryScanner().scan(Folder.fromDirectory(new File(setupInfo.directory())));
    }
}
