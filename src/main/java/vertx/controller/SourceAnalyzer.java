package vertx.controller;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import utils.Result;
import utils.SetupInfo;

public interface SourceAnalyzer {
    Future<Result> getReport(SetupInfo setupInfo, Vertx vertx);

    Result analyzeSources(SetupInfo setupInfo, Vertx vertx);

    void processEvent(Runnable runnable);
}
