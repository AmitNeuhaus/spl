package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.ConferenceInformation;

import bgu.spl.mics.application.objects.Model;

import java.util.concurrent.CopyOnWriteArrayList;

public class PublishConferenceBroadcast implements Broadcast {
    ConferenceInformation conInfo;
    Model model;

    public PublishConferenceBroadcast(ConferenceInformation conInfo, Model model){
        this.conInfo = conInfo;
        this.model= model;
    }



    public String getSenderId() {
        return conInfo.getName();
    }

    public Object getData() {
        return conInfo;
    }

    public Model getPublishedModel(){return model;}
}
