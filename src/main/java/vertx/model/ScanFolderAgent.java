package vertx.model;

import utils.Document;
import utils.Folder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.impl.future.PromiseImpl;
import vertx.controller.Controller;

public class ScanFolderAgent extends AbstractVerticle {

    private final Folder folder;
    private final Controller controller;
    private int deployedAgents = 0;
    private Promise<Void> isCompleted = new PromiseImpl<>();
    private boolean isRoot = false;

    public ScanFolderAgent(Controller controller, Folder folder, boolean isRoot) {
        this(controller, folder);
        this.isRoot = isRoot;
    }

    public ScanFolderAgent(Controller controller, Folder folder) {
        this.folder = folder;
        this.controller = controller;
    }

    @Override
    public void start(Promise<Void> startPromise) throws InterruptedException {

        deployedAgents += folder.getSubFolders().size();
        deployedAgents += folder.getDocuments().size();

        vertx.eventBus().consumer("stop", message -> {
            if(isRoot){
                isCompleted.complete();
            }
        });

        if(deployedAgents == 0){
            startPromise.complete();
            isCompleted.complete();
            return;
        }

        for (Folder subFolder: folder.getSubFolders()) {
            final ScanFolderAgent scanFolderAgent = new ScanFolderAgent(this.controller, subFolder);
            vertx.deployVerticle(scanFolderAgent);
            scanFolderAgent.onComputationEnded().onComplete(res -> {
                deployedAgents--;
                if(deployedAgents == 0){
                    if(!isCompleted.future().isComplete()){
                        isCompleted.complete();
                    }
                }
            });
        }

        for (Document document: folder.getDocuments()) {
            CountLinesAgent countLinesAgent = new CountLinesAgent(controller, document);
            this.vertx.deployVerticle(countLinesAgent).onComplete(res -> {
                deployedAgents--;
                if(deployedAgents == 0){
                    if(!isCompleted.future().isComplete()){
                        isCompleted.complete();
                    }
                }
            });
        }

        startPromise.complete();
    }

    public Future<Void> onComputationEnded(){
        return isCompleted.future();
    }

    private void log(String msg) {
        System.out.println("[SCAN FOLDER AGENT]["+Thread.currentThread()+"] " + msg);
    }

}
