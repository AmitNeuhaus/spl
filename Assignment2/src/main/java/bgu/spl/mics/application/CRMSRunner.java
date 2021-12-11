package bgu.spl.mics.application;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;

/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {
        MessageBusImpl msb = MessageBusImpl.getInstance();
        class testMicroservice extends MicroService{

            /**
             * @param name the micro-service name (used mainly for debugging purposes -
             *             does not have to be unique)
             */
            public testMicroservice(String name) {
                super(name);
            }

            @Override
            protected void initialize() {
                msb.register(this);
                msb.subscribeEvent(ExampleEvent.class,this);
                msb.subscribeBroadcast(ExampleBroadcast.class,this);
                subscribeEvent(ExampleEvent.class , ev -> {
                    System.out.println("Im handling an event");
                });

                subscribeBroadcast(ExampleBroadcast.class, br ->{
                    System.out.println("Im handling a broadcast");
                });
            }

        }

        class testSenderMicroservice extends MicroService{

            /**
             * @param name the micro-service name (used mainly for debugging purposes -
             *             does not have to be unique)
             */
            public testSenderMicroservice(String name) {
                super(name);
            }

            @Override
            protected void initialize() throws InterruptedException {
                ExampleEvent ev = new ExampleEvent("test event");
                ExampleBroadcast br = new ExampleBroadcast("test broadcast");
                msb.sendEvent(ev);
                msb.sendBroadcast(br);
            }
        }
        testMicroservice msHandler = new testMicroservice("Handler");
        testSenderMicroservice msSender = new testSenderMicroservice("Sender");
        Runnable target;
        Thread t1 = new Thread(msSender);
        Thread t2 = new Thread(msHandler);
        t1.start();
        t2.start();
    }
}
