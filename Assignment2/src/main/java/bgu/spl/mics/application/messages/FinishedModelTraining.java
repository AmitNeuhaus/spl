package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;


public class FinishedModelTraining implements Broadcast {

    Student student;
    Model model;

    public FinishedModelTraining(Model model) {
        this.student = model.getStudent();
        this.model = model;
    }

    public FinishedModelTraining() {
    }



    public Student getStudent(){
        return student;
    }
    public Model getModel(){
        return model;
    }



}
