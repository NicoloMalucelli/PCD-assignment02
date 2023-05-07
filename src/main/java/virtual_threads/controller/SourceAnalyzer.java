package virtual_threads.controller;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import utils.Result;
import utils.SetupInfo;

import java.util.concurrent.CompletableFuture;

public interface SourceAnalyzer {
    CompletableFuture<Result> getReport(SetupInfo setupInfo);

    Result analyzeSources(SetupInfo setupInfo, CompletableFuture<Void> executionEnded);

}
