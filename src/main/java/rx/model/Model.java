package rx.model;

import java.util.concurrent.atomic.AtomicBoolean;

public class Model {

    private AtomicBoolean shouldStop;

    public AtomicBoolean getShouldStop(){
        return shouldStop;
    }

    public void resetShouldStop(){
        shouldStop = new AtomicBoolean();
    }

}
