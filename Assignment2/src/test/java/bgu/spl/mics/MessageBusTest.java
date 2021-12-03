package bgu.spl.mics;

import java.lang.reflect.Field;
import java.util.*;

import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusTest {

    private MessageBusImpl messageBus;
    private MicroService microService;
    private ExampleEvent event;
    private ExampleBroadcast broadcast;



    @BeforeEach
    void setUp(){
       messageBus = new MessageBusImpl();
        microService = new MicroService("testMicroservice" ) {
            @Override
            protected void initialize() {
                System.out.println("initialized "+getName());
            }
        };
        event = new ExampleEvent("event");
        broadcast = new ExampleBroadcast("broadcast");
    }

    @Test
    void subscribeEvent() {
        int numOfSubs  = messageBus.getNumOfEventListeners(event.getClass());
        assertEquals(messageBus.isListeningToEvent(event.getClass(),microService),false);
        messageBus.subscribeEvent(event.getClass(),microService);
        assertEquals(messageBus.isListeningToEvent(event.getClass(),microService),true);
        assertEquals(messageBus.getNumOfEventListeners(event.getClass()),numOfSubs +1);

    }

    @Test
    void subscribeBroadcast() {
        int numOfSubs  = messageBus.getNumOfBroadcastListeners(broadcast.getClass(),microService);
        assertEquals(messageBus.isListeningToBroadcast(broadcast.getClass()),false);
        messageBus.subscribeEvent(event.getClass(),microService);
        assertEquals(messageBus.isListeningToBroadcast(broadcast.getClass()),true);
        assertEquals(messageBus.getNumOfBroadcastListeners(broadcast.getClass(),microService),numOfSubs +1);
    }

    @Test
    void complete() {
    }

    @Test
    void sendBroadcast() {
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
    void sendEvent() {
        Iterable<Queue<Object>> eventListeners = messageBus.getEventListeners(event.getClass());
        Iterator<Queue<Object>> iterator= eventListeners.iterator();
        Queue<Object> firstListener = iterator.hasNext() ? iterator.next(): null;
        if (firstListener!= null){
            for(Queue<Object> listener : eventListeners){
                assertEquals(listener.contains(event),false);
            }
            messageBus.sendEvent(event);
            assertEquals(firstListener.contains(event),true);
            iterator = messageBus.getEventListeners(event.getClass()).iterator();
            assertTrue(iterator.next() != firstListener); //TODO check @pre first = @post last
        }
    }


    @Test
    void register() { //TODO test register to a ms that is already registered
        int queuesSize = messageBus.getNumberOfMicroServices();
        assertEquals(messageBus.isMicroServiceRegistered(microService),false);
        messageBus.register(microService);
        assertEquals(messageBus.isMicroServiceRegistered(microService),true);
        assertEquals(messageBus.getNumberOfMicroServices(),queuesSize+1);

    }

    @Test
    void unregister() {//TODO use register try to un register an unregistered MS
        int queuesSize = messageBus.getNumberOfMicroServices();
        assertEquals(messageBus.isMicroServiceRegistered(microService),true);
        messageBus.unregister(microService);
        assertEquals(messageBus.isMicroServiceRegistered(microService),false);
        assertEquals(messageBus.getNumberOfMicroServices(),queuesSize - 1);

    }

    @Test
    void awaitMessage() {
    }
}