package executors.controller;

import utils.SetupInfo;
import utils.Result;

import java.util.concurrent.CompletableFuture;

public interface SourceAnalyzer {

    CompletableFuture<Result> getReport(SetupInfo setupInfo);

    //void analyzeSources(String directory);
}