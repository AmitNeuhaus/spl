package bgu.spl.mics.application;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.DataBatch;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.services.CPUService;
import bgu.spl.mics.application.services.GPUService;
import bgu.spl.mics.application.services.GPUTimeService;
import bgu.spl.mics.application.services.TimeService;


/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {
        MessageBusImpl msb = MessageBusImpl.getInstance();
        GPUTimeService gpuTimeService = new GPUTimeService("GPU time service");
        GPU gpu1 = new GPU(gpuTimeService);
        GPUService gpuService = new GPUService("GPU service",gpu1);
        TimeService timeService = new TimeService(25, 1000);
        TrainModelEvent trainModelEvent = new TrainModelEvent(new Model());


        Thread t1 = new Thread(gpuService);
        Thread t2 = new Thread(timeService);
        Thread t3 = new Thread(gpuTimeService);
        t1.start();
        t2.start();
        t3.start();
        try{
            Thread.sleep(3000);
            MessageBusImpl.getInstance().sendEvent(trainModelEvent);
        }catch(Exception ignored){

        }



    }
}
