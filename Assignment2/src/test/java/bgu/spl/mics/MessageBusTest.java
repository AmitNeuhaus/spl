package bgu.spl.mics;


import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



class MessageBusTest {

    private MessageBusImpl messageBus;
    private MicroService microService;
    private TrainModelEvent event;
    private TickBroadcast broadcast;




    @BeforeEach
    void setUp(){
       messageBus = MessageBusImpl.getInstance();
        microService = new MicroService("testMicroservice" ) {
            @Override
            protected void initialize() {
                subscribeEvent(TrainModelEvent.class,train -> {
                    System.out.println("Received new event");
                });

                subscribeBroadcast(TickBroadcast.class, tick ->{
                    System.out.println("Received broadcast");
                });
            }

        };
        event = new TrainModelEvent(new Model());
        broadcast = new TickBroadcast(1);
    }
    @AfterEach
    void cleanUp(){
        messageBus.unregister(microService);
        messageBus.clearBroadcastListeners(broadcast.getClass());
        messageBus.clearEventListeners(event.getClass());
    }


    @Test
    void register() {
        //assert the microservice was un registered.
        int numOfMicroServices = messageBus.getNumberOfMicroServices();
        assertEquals(messageBus.isMicroServiceRegistered(microService),false);
        messageBus.register(microService);
        assertEquals(messageBus.isMicroServiceRegistered(microService),true);
        assertEquals(messageBus.getNumberOfMicroServices(),numOfMicroServices+1);

        //assert that we can't double register.
        numOfMicroServices = messageBus.getNumberOfMicroServices();
        messageBus.register(microService);
        assertEquals(messageBus.getNumberOfMicroServices(),numOfMicroServices);


    }

    @Test
    void unregister() {
        //assert the microservice was un unregistered.
        messageBus.register(microService);
        int numOfMicroServices = messageBus.getNumberOfMicroServices();
        assertEquals(messageBus.isMicroServiceRegistered(microService),true);
        messageBus.unregister(microService);
        assertEquals(messageBus.isMicroServiceRegistered(microService),false);
        assertEquals(messageBus.getNumberOfMicroServices(),numOfMicroServices - 1);

        //assert double unregister don't raise an error.
        numOfMicroServices = messageBus.getNumberOfMicroServices();
        messageBus.unregister(microService);
        assertEquals(messageBus.getNumberOfMicroServices(),numOfMicroServices);


    }



    @Test
    void subscribeEvent() {
        //subscribe an un registered microservice-> should raise exception
        int numOfSubs = messageBus.getNumOfEventListeners(event.getClass());
        assertThrows("Should throw exception(microservice was never registered)",IllegalArgumentException.class,()->messageBus.subscribeEvent(TrainModelEvent.class,microService));
        assertEquals(messageBus.getNumOfEventListeners(event.getClass()),numOfSubs);
        //assert the microservice was subscribed
        messageBus.register(microService);
        numOfSubs  = messageBus.getNumOfEventListeners(event.getClass());
        assertEquals(messageBus.isListeningToEvent(event.getClass(),microService),false);
        messageBus.subscribeEvent(TrainModelEvent.class,microService);
        assertEquals(messageBus.isListeningToEvent(event.getClass(),microService),true);
        assertEquals(messageBus.getNumOfEventListeners(event.getClass()),numOfSubs +1);

        //assert that we can't double subscribe to the same event
        numOfSubs  = messageBus.getNumOfEventListeners(event.getClass());
        messageBus.subscribeEvent(event.getClass(),microService);
        assertEquals(messageBus.isListeningToEvent(event.getClass(),microService),true);
        assertEquals(messageBus.getNumOfEventListeners(event.getClass()),numOfSubs);

    }

    @Test
    void subscribeBroadcast() {
        //subscribe an un registered microservice-> should raise exception
        int numOfSubs = messageBus.getNumOfBroadcastListeners(broadcast.getClass());
        assertThrows("Should throw exception(microservice was never registered)",IllegalArgumentException.class,()->messageBus.subscribeBroadcast(TickBroadcast.class,microService));
        assertEquals(messageBus.getNumOfBroadcastListeners(broadcast.getClass()),numOfSubs);
        //assert the microservice was subscribed
        messageBus.register(microService);
        numOfSubs  = messageBus.getNumOfBroadcastListeners(broadcast.getClass());
        assertEquals(messageBus.isListeningToBroadcast(broadcast.getClass(),microService),false);
        messageBus.subscribeBroadcast(TickBroadcast.class,microService);
        assertEquals(messageBus.isListeningToBroadcast(broadcast.getClass(),microService),true);
        assertEquals(messageBus.getNumOfBroadcastListeners(broadcast.getClass()),numOfSubs +1);

        //assert that we can't double subscribe to the same broadcast
        numOfSubs  = messageBus.getNumOfBroadcastListeners(broadcast.getClass());
        messageBus.subscribeBroadcast(broadcast.getClass(),microService);
        assertEquals(messageBus.isListeningToBroadcast(broadcast.getClass(),microService),true);
        assertEquals(messageBus.getNumOfBroadcastListeners(broadcast.getClass()),numOfSubs );
    }

    @Test
    void complete() throws InterruptedException {
        messageBus.register(microService);
        messageBus.subscribeEvent(event.getClass(),microService);
        messageBus.sendEvent(event);
        Model result = event.getModel();
        messageBus.complete(event,result);
        assertEquals(event.getFuture().get(),result);
        assertEquals(event.getFuture().isDone(),true);
    }

    @Test
    void sendBroadcast() throws InterruptedException {
        messageBus.register(microService);
        microService.initialize();
        Iterable<Queue<Object>> broadcastListeners = messageBus.getBroadcastListeners(broadcast.getClass()) ;
        for (Queue<Object> listener : broadcastListeners){
            assertEquals(listener.contains(broadcast),false);
        }
        messageBus.sendBroadcast(broadcast);
        for (Queue<Object> listener : broadcastListeners){
            assertEquals(listener.contains(broadcast),true);
        }
    }

    @Test
    void sendEvent() throws InterruptedException {
        messageBus.register(microService);
        microService.initialize();
        Iterable<LinkedBlockingQueue<Object>> eventListeners = messageBus.getEventListeners(event.getClass());
        Iterator<LinkedBlockingQueue<Object>> iterator= eventListeners.iterator();
        Queue<Object> firstListener = iterator.hasNext() ? iterator.next(): null;
        if (firstListener!= null){
            for(Queue<Object> listener : eventListeners){
                assertEquals(listener.contains(event),false);
            }
            messageBus.sendEvent(event);
            assertEquals(firstListener.contains(event),true);
            iterator = messageBus.getEventListeners(event.getClass()).iterator();
            assertTrue(iterator.next() != firstListener ||messageBus.getNumOfEventListeners(event.getClass()) == 1 );
        }
    }




    @Test
    void awaitMessage() throws InterruptedException {
        messageBus.register(microService);
        messageBus.subscribeEvent(event.getClass(),microService);
        messageBus.sendEvent(event);
        int queueSize = messageBus.getMicroserviceQueueSize(microService);
        Message message = messageBus.awaitMessage(microService);
        assertEquals(messageBus.getMicroserviceQueueSize(microService),queueSize - 1);
        assertEquals(message,event);



    }
}