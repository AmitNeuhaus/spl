package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;


public class TestModelEvent implements Event<Model.results> {
    private Model model;
    private Future<Model.results> future = null;


    @Override
    public Future<Model.results> getFuture() {
        if (future == null){
            future = new Future<Model.results>();
        }
        return future;
    }

    public Model getModel(){
        return model;
    }

}
