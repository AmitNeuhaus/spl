package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;


public class FinishedModelTesting implements Broadcast {

    Student student;
    Model model;

    public FinishedModelTesting(Model model) {
        this.student = model.getStudent();
        this.model = model;
    }

    public FinishedModelTesting() {
    }



    public Student getStudent(){
        return student;
    }


    public Model getModel() {
        return model;
    }
}
