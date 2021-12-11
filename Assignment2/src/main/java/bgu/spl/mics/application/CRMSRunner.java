package bgu.spl.mics.application;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.objects.CPU;
import bgu.spl.mics.application.objects.DataBatch;
import bgu.spl.mics.application.services.CPUService;
import bgu.spl.mics.application.services.TimeService;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {
        MessageBusImpl msb = MessageBusImpl.getInstance();
        CPUService cpuService = new CPUService("CPUService");
        CPU cpu1 = new CPU(8, cpuService);
        TimeService timeService = new TimeService(7, 1000);
        DataBatch dataBatch = new DataBatch();


        Thread t1 = new Thread(cpuService);
        Thread t2 = new Thread(timeService);
        t1.start();
        t2.start();
        cpu1.process(dataBatch);

    }
}
