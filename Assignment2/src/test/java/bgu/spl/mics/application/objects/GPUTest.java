package bgu.spl.mics.application.objects;


import bgu.spl.mics.application.services.CPUService;
import bgu.spl.mics.application.services.GPUService;
import bgu.spl.mics.application.services.GPUTimeService;
import bgu.spl.mics.application.services.TimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;

class GPUTest {

    private GPU gpu;
    private CPU cpu;
    GPUTimeService gpuTimeService;


    @BeforeEach
    void setUp(){
        gpuTimeService = new GPUTimeService();
        gpu = new GPU(gpuTimeService);
        cpu = new CPU(32,new CPUService(),5);

    }

    @Test
    void insertModel() {
        Model model = new Model();

        assertEquals(model.getStatus(), Model.statusEnum.PreTrained);


        // inserting PreTrained model -> Expected: Change Gpu model
        model.setStatus(Model.statusEnum.PreTrained);
        gpu.insertModel(model);
        assertEquals(gpu.model, model);
        assertEquals(gpu.model.getStatus(), model.getStatus());

        // inserting PreTrained model -> Expected: Change Gpu model
        model.setStatus(Model.statusEnum.Trained);
        gpu.insertModel(model);
        assertEquals(gpu.model, model);
        assertEquals(gpu.model.getStatus(), model.getStatus());

        // inserting Tested model ->  Expected: throw Error
        Model oldModel = gpu.model;
        model = new Model();
        model.setStatus(Model.statusEnum.Tested);
        gpu.insertModel(model);
        assertEquals(gpu.model,oldModel);



        // inserting training model -> Expected: throw Error
        model.setStatus(Model.statusEnum.Training);
        gpu.insertModel(model);
        assertEquals(gpu.model,oldModel);
    }


    @Test
    void sendData() throws InterruptedException {

       // created a gpu with data
        Model model = new Model();
        gpu.insertModel(model);
        gpu.splitToBatches();
        int diskSize = gpu.getDiskSize();
        DataBatch db = gpu.nextDataBatchDisk();
        assertEquals(db.isProcessed(), false);
        gpu.sendData();
        assertEquals(gpu.getDiskSize() , diskSize - 1);
        assertNotEquals(gpu.nextDataBatchDisk().getStartIndex(), db.getStartIndex());
    }

    @Test
    void insertDbToVram() {
        assertFalse(gpu.isVramFull());
        DataBatch db = new DataBatch();
        //cant insert unporocessed db to vram
//        assertThrows("cant insert unprocessed db to vram", Exception.class, ()-> gpu.insertDbToVram(db));

        //cant insert trained db to vram
        db.setTrained(true);
        db.setProcessed(true);
//        assertThrows("cant insert trained db to vram", Exception.class, ()-> gpu.insertDbToVram(db));

        //now db is only processed -> valid.
        db.setTrained(false);
        db.setProcessed(true);
        int vramSize = gpu.getVramSize();
        gpu.insertDbToVram(db);
        assertEquals(gpu.getVramSize(), vramSize +1);
    }


    @Test
    void Train() throws InterruptedException {
        TimeService timeService = new TimeService(8,1000);
        Thread t1 = new Thread(timeService);
        Thread t2 = new Thread(gpuTimeService);
        t1.start();
        t2.start();
        Thread.sleep(1000);
        // inserting processed db to vram;
        DataBatch db = new DataBatch();
        Model model = new Model();
        gpu.insertModel(model);
        db.setProcessed(true);
        gpu.insertDbToVram(db);
        int vramSize = gpu.getVramSize();
        int trainedDiskSize = gpu.getTrainedDiskSize();
        gpu.Train();
        //check that db is now trained & processed
        assertTrue(db.isTrained());
        assertTrue(db.isProcessed());
        //check that vram size is now smaller by 1
        assertEquals(gpu.getVramSize(),vramSize-1);

        //check that trained disk in now +1
        assertEquals(gpu.getTrainedDiskSize(),trainedDiskSize + 1);

    }

    @Test
    void clearGpu() throws InterruptedException {

        // filling all disks:
        // disk - model databatches
        // vram manualy inserted
        // trainedDisk caling Test once
        DataBatch db1 = new DataBatch();
        db1.setProcessed(true);
        Model model = new Model();
        gpu.insertModel(model);
        gpu.splitToBatches();
        gpu.insertDbToVram(db1);
        gpu.insertDbToVram(db1);
        DataBatch db2 = new DataBatch();
        db2.setTrained(true);
        gpu.insertTrainedDbToDisk(db2);

        assertNotEquals(gpu.getTrainedDiskSize(),0);
        assertNotEquals(gpu.getDiskSize(),0);
        assertNotEquals(gpu.getVramSize(),0);
        //clearing GPU disks
        gpu.clearGpu();

        //check disks are empty
        assertEquals(gpu.getTrainedDiskSize(),0);
        assertEquals(gpu.getDiskSize(),0);
        assertEquals(gpu.getVramSize(),0);
    }




}