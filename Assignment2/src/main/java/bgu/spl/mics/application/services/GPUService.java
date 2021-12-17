package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.FinishedModelTesting;
import bgu.spl.mics.application.messages.FinishedModelTraining;
import bgu.spl.mics.application.messages.TestModelEvent;
import bgu.spl.mics.application.messages.TrainModelEvent;
import bgu.spl.mics.application.objects.GPU;
import bgu.spl.mics.application.objects.Model;

/**
 * GPU service is responsible for handling the
 * {@link TrainModelEvent} and {@link TestModelEvent},
 * in addition to sending the {@link //DataPreProcessEvent}.
 * This class may not hold references for objects which it is not responsible for.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class GPUService extends MicroService {

    private GPU gpu;

    public GPUService(GPU gpu) {
        super("GPU Service");
        this.gpu = gpu;
    }


    @Override
    protected void initialize() {
        //Train Event:
        subscribeEvent(TrainModelEvent.class, trainEvent -> {
            System.out.println("Started Training model: " + trainEvent.getModel().getName());
            Model model = trainEvent.getModel();
            if (model.getStatus() == Model.statusEnum.PreTrained){
                gpu.insertModel(model);
                model.setStatus(Model.statusEnum.Training);;
                //TODO split to batches send and train policy
                gpu.splitToBatches();
                while(model.getData().getProcessed() < model.getDataSize()){
                    if(Thread.currentThread().isInterrupted()){
                        throw new InterruptedException();
                    }
                    if (gpu.getNumOfBatchesToSend() > 0 && gpu.getDiskSize() > 0){
                        gpu.sendData();
                    }
                    if(gpu.getVramSize()>0){
                        gpu.Train();
                    }
                }
                model.setStatus(Model.statusEnum.Trained);
//                complete(trainEvent,model);
                System.out.println("finished training!!!!!!!!!!");
                gpu.clearGpu();
                sendBroadcast(new FinishedModelTraining(model));
            }
        });


        //Test Event:
        subscribeEvent(TestModelEvent.class, testEvent -> {
            Model model = testEvent.getModel();
            if (model.getStatus() == Model.statusEnum.Trained && model.getResult() == Model.results.None){
                System.out.println("started testing model");
                gpu.insertModel(model);
                Model.results result = gpu.testModel();
                model.setStatus(Model.statusEnum.Tested);
                model.setResult(result);
                complete(testEvent,result);
                gpu.clearGpu();
                sendBroadcast(new FinishedModelTesting(model));
                System.out.println("finished testing model result is: " + result);
            }
        });
    }
}
