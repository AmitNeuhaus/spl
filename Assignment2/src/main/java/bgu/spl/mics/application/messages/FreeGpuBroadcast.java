package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;


public class FreeGpuBroadcast implements Broadcast {

    Student student;

    public FreeGpuBroadcast(Model model) {
        this.student = model.getStudent();
    }

    public Student getStudent(){
        return student;
    }



}
