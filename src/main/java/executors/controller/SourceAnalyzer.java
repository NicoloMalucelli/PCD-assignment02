package executors.controller;

import utils.SetupInfo;
import utils.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinTask;

public interface SourceAnalyzer {

    ForkJoinTask<Result> getReport(SetupInfo setupInfo);

    Result analyzeSources(SetupInfo setupInfo);
}