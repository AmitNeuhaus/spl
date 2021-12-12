package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;

public class TrainModelEvent implements Event<Model> {
    private Model model;
    private Future<Model> future = null;

    public TrainModelEvent(Model model){
        this.model = model;
    }
    public Future<Model> getFuture(){
        if (future == null){
            future = new Future<Model>();
        }
        return future;
    }

    public Model getModel(){
        return model;
    }
}
