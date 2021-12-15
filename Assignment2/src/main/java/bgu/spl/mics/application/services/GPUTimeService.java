package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;

public class GPUTimeService extends MicroService {

    private int currentTime;

    /**
     * @param name the micro-service name (used mainly for debugging purposes -
     *             does not have to be unique)
     */
    public GPUTimeService() {
        super("GPU time service");
        currentTime = 0;
    }


    public int getTime(){
        return currentTime;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, eventTime -> {
            currentTime = (int)eventTime.getData();
        });
    }
}
