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


    ConferenceInformation conInfo;



    public ConferenceService(ConferenceInformation confInfo) {
        super("Conference service");
        this.conInfo = confInfo;
    }


    public ConferenceInformation getConInfo(){return conInfo;}

    @Override
    protected void initialize() {
       subscribeBroadcast(TickBroadcast.class, tickBroadcast -> {
           if(tickBroadcast.getData() == conInfo.getDate()){
//               System.out.println("started publication on: "+tickBroadcast.getData() );
               for (Model model : conInfo.getModelsToPublish()){
                   sendBroadcast(new PublishConferenceBroadcast(conInfo,model));
//                   System.out.println("publish model: "+ model.getName());
               }
               conInfo.publish();
               MessageBusImpl.getInstance().unregister(this);
               terminate();
           }
       });


       subscribeEvent(PublishResultsEvent.class, publishEvent -> {
           conInfo.addModel(publishEvent.getModel());
//           System.out.println("model was added to conference model list: " + publishEvent.getModel().getName());
       });




    }
}
