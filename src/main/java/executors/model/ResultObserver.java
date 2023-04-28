package executors.model;

import utils.Result;

public interface ResultObserver {

    void resultUpdated(Result updatedMidReport);

}