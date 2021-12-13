package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;

public class PublishResultsEvent implements Event<Model> {

    Model model;
    Student student;


    @Override
    public Future<Model> getFuture() {
        return null;
    }

    public Model getModel(){return model;}

    public Student getStudent(){return student;}
}
