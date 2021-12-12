package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;


public class TestModelEvent implements Event<Boolean> {
    private Model model;
    private Future<Boolean> future = null;


    @Override
    public Future<Boolean> getFuture() {
        if (future == null){
            future = new Future<Boolean>();
        }
        return future;
    }

    public Model getModel(){
        return model;
    }

}
