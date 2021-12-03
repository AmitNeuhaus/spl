package bgu.spl.mics;

import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.services.ExampleEventHandlerService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageBusTest {

    MessageBus msgBus;

    @Test
    void subscribeEvent() {
        msgBus = new MessageBusImpl();
        ExampleEvent e = new ExampleEvent("ss");
        msgBus.subscribeEvent(e.getClass(), new ExampleEventHandlerService("ss", new String[]{"sss"}));
    }

    @Test
    void subscribeBroadcast() {
    }

    @Test
    void complete() {
    }

    @Test
    void sendBroadcast() {
    }

    @Test
    void sendEvent() {
    }

    @Test
    void register() {
    }

    @Test
    void unregister() {
    }

    @Test
    void awaitMessage() {
    }
}