package bgu.spl.mics.example.services;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Message;
import bgu.spl.mics.example.messages.ExampleBroadcast;

public class testExampleCallBack2 implements Callback<ExampleBroadcast> {
    @Override
    public void call(ExampleBroadcast c) {
        System.out.println("This Broadcast from: " + c + "is handled");
    }
}
