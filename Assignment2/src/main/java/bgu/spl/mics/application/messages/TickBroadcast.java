package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    int tick;

    public TickBroadcast(int currentTick){
        tick = currentTick;
    }

    @Override
    public String getSenderId() {
        return null;
    }

    @Override
    public Object getData() {
        return tick;
    }
}
