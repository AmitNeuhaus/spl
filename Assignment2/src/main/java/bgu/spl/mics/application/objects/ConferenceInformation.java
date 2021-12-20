package bgu.spl.mics.application.objects;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Passive object representing information on a conference.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class ConferenceInformation {

    private String name;
    private int date;
    private boolean didPublish;
    CopyOnWriteArrayList<Model> modelsToPublish;

    public ConferenceInformation(String name, int date){
        this.name = name;
        this.date = date;
        didPublish = false;
        modelsToPublish = new CopyOnWriteArrayList<>();
    }

    public String getName(){return name;}

    public int getDate(){return date;}

    public boolean didPublish(){
        return didPublish;
    }

    public void publish(){
        didPublish = true;
    }

    public void addModel(Model model){
        modelsToPublish.add(model);
    }

    public CopyOnWriteArrayList<Model> getModelsToPublish(){
        return modelsToPublish;
    }

    public String toString(){
        String output = "name: "+ name+"\n"+"date: "+date+"\n"+"publications: "+"\n";
        if (didPublish){
            for (Model model:modelsToPublish){
                output += model.toString();
                output+= "\n";
            }
        }else{
            output +="NONE";
        }
        return  output;
    }
}