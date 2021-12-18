package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;

public class GPUTimeService extends MicroService {

    private int currentTime;

    /**
     */
    public GPUTimeService() {
        super("GPU time service");
        currentTime = 0;
    }

    public synchronized void remindMeIn(int processTime) throws InterruptedException {
        int start = currentTime;
            while (currentTime - start < processTime) {
                wait();
            }
    }


    public int getTime(){
        return currentTime;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TickBroadcast.class, eventTime -> {
            synchronized (this) {
                currentTime = eventTime.getData();
                notifyAll();
            }
        });
    }
}
