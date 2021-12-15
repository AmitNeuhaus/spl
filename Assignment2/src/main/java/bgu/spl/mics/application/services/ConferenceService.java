package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.PublishConferenceBroadcast;
import bgu.spl.mics.application.messages.PublishResultsEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.ConferenceInformation;
import bgu.spl.mics.application.objects.Model;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Conference service is in charge of
 * aggregating good results and publishing them via the {@link PublishConferenceBroadcast},
 * after publishing results the conference will unregister from the system.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ConferenceService extends MicroService {

    CopyOnWriteArrayList<Model> modelsToPublish;
    ConferenceInformation conInfo;


    public ConferenceService(String name,int date) {
        super("Conference service ");
        conInfo = new ConferenceInformation(name,date);
        modelsToPublish = new CopyOnWriteArrayList<>();
    }

    @Override
    protected void initialize() {
       subscribeBroadcast(TickBroadcast.class, tickBroadcast -> {
           if((int)tickBroadcast.getData() == conInfo.getDate()){
               for (Model model : modelsToPublish){
                   sendBroadcast(new PublishConferenceBroadcast(conInfo,model));
               }
               MessageBusImpl.getInstance().unregister(this);
               terminate();
           }
       });


       subscribeEvent(PublishResultsEvent.class, publishEvent -> {
           modelsToPublish.add(publishEvent.getModel());
       });




    }
}
