package rx.controller;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import utils.Result;
import utils.SetupInfo;

public interface SourceAnalyzer {
    Single<Result> getReport(SetupInfo setupInfo);
    Flowable<Result> analyzeSources(SetupInfo setupInfo);
}
