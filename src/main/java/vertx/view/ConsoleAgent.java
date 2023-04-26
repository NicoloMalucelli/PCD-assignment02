package vertx.view;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import utils.AnalyzedFile;
import utils.Interval;
import utils.SetupInfo;
import vertx.controller.Controller;

import java.util.Map;

public class ConsoleAgent extends AbstractVerticle {

    private final Controller controller;
    private final SetupInfo setupInfo;
    public ConsoleAgent(Controller controller, SetupInfo setupInfo){
        this.controller = controller;
        this.setupInfo = setupInfo;
    }

    @Override
    public void start(){
        controller.getReport(setupInfo, vertx).onComplete(res -> {
            for(AnalyzedFile analyzedFile : res.result().getRanking()){
                System.out.println(analyzedFile);
            }
            System.out.println();
            for(Map.Entry<Interval, Integer> analyzedFile : res.result().getDistribution().entrySet()){
                System.out.println(analyzedFile.getKey() + " = " + analyzedFile.getValue());
            }
            vertx.close();
        });
    }

    private void log(String msg) {
        System.out.println("[CONSOLE AGENT]["+Thread.currentThread()+"] " + msg);
    }
}