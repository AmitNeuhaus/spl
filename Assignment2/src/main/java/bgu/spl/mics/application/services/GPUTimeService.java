package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;

public class GPUTimeService extends MicroService {

    private int currentTime;

    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public GPUTimeService(String name) {
        super(name);
        currentTime = 0;
    }

    public int getTime(){
        return currentTime;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, eventTime -> {
            System.out.println("started tickbroadcast callback");
            currentTime = (int)eventTime.getData();
            System.out.println(eventTime.getData());
            System.out.println(currentTime);
        });
    }
}
