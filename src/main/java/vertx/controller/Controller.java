package vertx.controller;

import utils.Folder;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.future.PromiseImpl;
import utils.AnalyzedFile;
import utils.Result;
import utils.SetupInfo;
import vertx.model.Model;
import vertx.model.ScanFolderAgent;

import java.io.File;
import java.io.IOException;

public class Controller implements SourceAnalyzer {

    private final Model model;
    private String rootId;

    public Controller(Model model) {
        this.model = model;
    }

    @Override
    public Future<Result> getReport(SetupInfo setupInfo, Vertx vertx) {
        Promise<Result> result = new PromiseImpl<>();
        try {
            this.model.setResult(new Result(setupInfo.nIntervals(), setupInfo.lastIntervalLowerBound(), setupInfo.nFiles()));
            ScanFolderAgent ab = new ScanFolderAgent(this, Folder.fromDirectory(new File(setupInfo.directory())));
            //vertx.deployVerticle(ab).onComplete(res -> {
                //System.out.println("completed");
            //});
            ab.onComputationEnded().onComplete(res -> {
                result.complete(model.getResult());
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.future();
    }

    @Override
    public Result analyzeSources(SetupInfo setupInfo, Vertx vertx) {
        try {
            this.model.setResult(new Result(setupInfo.nIntervals(), setupInfo.lastIntervalLowerBound(), setupInfo.nFiles()));
            ScanFolderAgent rootAgent = new ScanFolderAgent(this, Folder.fromDirectory(new File(setupInfo.directory())), true);
            vertx.deployVerticle(rootAgent).onComplete(res -> {
                rootId = rootAgent.deploymentID();
                System.out.println("A " + rootId );
                rootAgent.onComputationEnded().onComplete(res2 -> {
                    System.out.println("COMPLETE ROOT");
                    vertx.eventBus().publish("computation-ended", "");
                    vertx.undeploy(rootId).onFailure(res3 -> {
                        res3.printStackTrace();
                    });
                });
            });
            return model.getResult();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRootId(){
        return this.rootId;
    }

    public void addAnalyzedFile(AnalyzedFile analyzedFile){
        this.model.getResult().add(analyzedFile);
    }

    @Override
    public void processEvent(Runnable runnable){
        new Thread(runnable).start();
    }

}
