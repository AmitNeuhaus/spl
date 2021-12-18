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

        subscribeBroadcast(StartSendModels.class, startSendModels -> {
            if (student.getModels().size() > 0) {
                currentModel = student.getModels().removeFirst();
                System.out.println( student.getName() + " [Started - First Time] Sent new model for training : " + currentModel.getName());
                future = sendEvent(new TrainModelEvent(currentModel));
            }
        });

        subscribeBroadcast(FinishedModelTraining.class, finishedModelTraining -> {
            System.out.println(finishedModelTraining.getModel().getName() +" " +finishedModelTraining.getModel().getStudent().getName() + " " + student.getName());
            if (finishedModelTraining.getStudent() == student) {
                student.addTrainedModel(finishedModelTraining.getModel());
                System.out.println(student.getName() + " sent test model event about model: " + finishedModelTraining.getModel().getName());
                future = sendEvent(new TestModelEvent(finishedModelTraining.getModel()));
            }
        });

        subscribeBroadcast(FinishedModelTesting.class, finishedModelTesting -> {
            if (finishedModelTesting.getStudent() == student) {
                Model.results result = (Model.results) future.get();
                if (result == Model.results.Good) {
                    sendEvent(new PublishResultsEvent(currentModel, student));
                    student.addPublication();
                }
                System.out.println("Saving Model " + currentModel.getName() +" to trained models of student " + student.getName());
                System.out.println(student.getName() + " Finished training model: " + currentModel.getName());
                if (student.getModels().size() > 0) {
                    currentModel = student.getModels().removeFirst();
                    System.out.println( student.getName() + " Sent new model for training : " + currentModel.getName());
                    future = sendEvent(new TrainModelEvent(currentModel));
                }
            }
        });

        subscribeBroadcast(PublishConferenceBroadcast.class, publishBroadcast -> {
            System.out.println("received a conference broadcast with model: "+publishBroadcast.getPublishedModel().getName());
            if (!(publishBroadcast.getPublishedModel().getStudent() ==student)){
                student.addPapersRead();
                System.out.println(student.getPapersRead());
            }
        });






    }
}
