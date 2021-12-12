package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
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

    public GPUService(String name,GPU gpu) {
        super("GPU Service");
        this.gpu = gpu;
    }


    @Override
    protected void initialize() {

        //Train Event:
        subscribeEvent(TrainModelEvent.class, trainEvent -> {
            System.out.println("started training");
            Model model = trainEvent.getModel();
            if (model.status == Model.statusEnum.PreTrained){
                gpu.clearGpu();
                model.setStatus(Model.statusEnum.Training);
                gpu.insertModel(model);
                gpu.splitToBatches(model.getData());
                while(model.getData().getProcessed() < model.getDataSize()){
                    if (!gpu.isVramFull() && gpu.getDiskSize() > 0){
                        //TODO think about immediately sending VRAM capacity data batches for processing
                        gpu.sendData();
                    }
                    if(gpu.getVramSize()>0){
                        gpu.Train();
                    }
                }
                model.setStatus(Model.statusEnum.Trained);
                complete(trainEvent,model);
                gpu.clearGpu();
                System.out.println("finished training");
            }
        });


        //Test Event:
        subscribeEvent(TestModelEvent.class, testEvent -> {
            Model model = testEvent.getModel();
            if (model.status == Model.statusEnum.Trained){
                gpu.clearGpu();
                gpu.insertModel(model);
                boolean result = gpu.testModel();
                model.setStatus(Model.statusEnum.Tested);
                complete(testEvent,result);
                gpu.clearGpu();
            }
        });
    }
}
