package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.ConferenceInformation;

import bgu.spl.mics.application.objects.Model;

import java.util.concurrent.CopyOnWriteArrayList;

public class PublishConferenceBroadcast implements Broadcast {
    ConferenceInformation conInfo;
    CopyOnWriteArrayList<Model> modelsToPublish;

    public PublishConferenceBroadcast(ConferenceInformation conInfo, CopyOnWriteArrayList<Model> modelsToPublish){
        this.conInfo = conInfo;
        this.modelsToPublish = modelsToPublish;
    }


    @Override
    public String getSenderId() {
        return conInfo.getName();
    }

    @Override
    public Object getData() {
        return conInfo;
    }

    public CopyOnWriteArrayList<Model> getPublishedModels(){return modelsToPublish;}
}
