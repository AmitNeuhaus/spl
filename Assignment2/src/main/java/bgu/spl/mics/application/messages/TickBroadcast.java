package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    int tick;

    public TickBroadcast(int currentTick){
        tick = currentTick;
    }


    public String getSenderId() {
        return null;
    }


    public int getData() {
        return tick;
    }
}
