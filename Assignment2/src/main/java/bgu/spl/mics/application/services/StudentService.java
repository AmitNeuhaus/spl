package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.objects.Model;
import bgu.spl.mics.application.objects.Student;
import junit.framework.Test;

import java.util.LinkedList;

/**
 * Student is responsible for sending the {@link TrainModelEvent},
 * {@link TestModelEvent} and {@link PublishResultsEvent}.
 * In addition, it must sign up for the conference publication broadcasts.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class StudentService extends MicroService {
    Future future;
    Student student;
    Model currentModel;

    public StudentService(Student student) {
        super("Student service");
        this.student = student;
        currentModel = null;
        future = null;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(PublishConferenceBroadcast.class, publishBroadcast -> {
            System.out.println("received a conference broadcast with model: "+publishBroadcast.getPublishedModel().getName());
            if (!(publishBroadcast.getPublishedModel().getStudent() ==student)){
                student.addPapersRead();
                System.out.println(student.getPapersRead());
            }
        });

        subscribeBroadcast(FreeGpuBroadcast.class, freeGpuBroadcast -> {

                if((freeGpuBroadcast.getStudent() == student && future == null && student.getModels().size()>0) || freeGpuBroadcast.getStudent() == null){
                    System.out.println("Student service received start broadcast");
                    currentModel = student.getModels().removeFirst();
                    future =  sendEvent(new TrainModelEvent(currentModel));
                    //sent for training
                    future.get();
                    future = sendEvent(new TestModelEvent(currentModel));
                    //sent for testing
                    Model.results result = (Model.results)future.get();
                    if (result == Model.results.Good){
                        sendEvent(new PublishResultsEvent(currentModel,student));
                        student.addPublication();
                    }
                    future = null;
                    System.out.println(currentModel.toString());
                }


        });

    }
}
