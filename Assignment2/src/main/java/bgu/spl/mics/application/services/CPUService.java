package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;


/**
 * CPU service is responsible for handling the {@link //DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class CPUService extends MicroService {

    private int currentTime;


    public CPUService() {
        super("CPU Service");
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
