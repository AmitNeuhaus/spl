package bgu.spl.mics.example;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Message;
import bgu.spl.mics.example.messages.ExampleEvent;

public class testCallBack implements Callback<ExampleEvent> {
    @Override
    public void call(ExampleEvent c) {
        System.out.println("This Event from: " + c + "is handled");
    }


}
