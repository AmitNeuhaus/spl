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
            protected void initialize()  {
                try{

                    Thread.sleep(3000);
                    msb.register(this);
                    System.out.println("started thread 1");
                    ExampleEvent ev = new ExampleEvent("test event");
                    ExampleBroadcast br = new ExampleBroadcast("test broadcast");
                    msb.sendEvent(ev);
                    msb.sendBroadcast(br);
                }catch(Exception error){
                    System.out.println("I printed this shit: "+error);
                }

            }
        }
        testMicroservice msHandler = new testMicroservice("Handler");
        testSenderMicroservice msSender = new testSenderMicroservice("Sender");
        Runnable target;
        Thread t1 = new Thread(msSender);
        Thread t2 = new Thread(msHandler);
        t2.start();
        t1.start();

    }
}
