package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.GPUTimeService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

class GPUTest {

    private GPU gpu;


    @BeforeEach
    void setUp(){
        GPUTimeService gpuTimeService = new GPUTimeService();
        gpu = new GPU(gpuTimeService);
    }

    @Test
    void insertModel() {
        Model model = new Model();
        int dataSize = model.getDataSize();
        assertNotEquals(model.getStatus(), Model.statusEnum.Training);
        assertNotEquals(model.getStatus(), Model.statusEnum.Tested);

        assertEquals(gpu.getDiskSize(), 0);
        // inserting PreTrained model -> Expected: Change Gpu model
        model.setStatus(Model.statusEnum.PreTrained);
        gpu.insertModel(model);
        assertEquals(gpu.model, model);
        assertEquals(gpu.model.getStatus(), model.getStatus());
        assertEquals(gpu.getDiskSize(), dataSize/1000);

        // inserting PreTrained model -> Expected: Change Gpu model
        model.setStatus(Model.statusEnum.Trained);
        gpu.insertModel(model);
        assertEquals(gpu.model, model);
        assertEquals(gpu.model.getStatus(), model.getStatus());
        assertEquals(gpu.getDiskSize(), dataSize/1000);

        // inserting Tested model ->  Expected: throw Error
        model.setStatus(Model.statusEnum.Tested);
        assertThrows("Should throw exception , model cant be tested when inserted", Exception.class,()->gpu.insertModel(model));

        // inserting training model -> Expected: throw Error
        model.setStatus(Model.statusEnum.Training);
        assertThrows("Should throw exception , model cant be training when inserted", Exception.class,()->gpu.insertModel(model));
    }


    @Test
    void sendData() {
        // if disk is empty -> throw error
       assertThrows("Should throw error if disk is empty - no data to send to process", Exception.class, ()-> gpu.sendData());


       // created a gpu with data
        Model model = new Model();
        int dataSize = model.getDataSize();
        gpu.insertModel(model);
        DataBatch db = gpu.nextDataBatchDisk();
        assertEquals(db.isProcessed(), false);
        gpu.sendData();
        assertEquals(gpu.getDiskSize() , dataSize - 1);
        assertNotEquals(gpu.nextDataBatchDisk(), db);
    }

    @Test
    void insertDbToVram() {
        assertFalse(gpu.isVramFull());
        DataBatch db = new DataBatch();
        //cant insert unporocessed db to vram
        assertThrows("cant insert unprocessed db to vram", Exception.class, ()-> gpu.insertDbToVram(db));

        //cant insert trained db to vram
        db.setTrained(true);
        db.setProcessed(true);
        assertThrows("cant insert trained db to vram", Exception.class, ()-> gpu.insertDbToVram(db));

        //now db is only processed -> valid.
        db.setTrained(false);
        db.setProcessed(true);
        int vramSize = gpu.getVramSize();
        gpu.insertDbToVram(db);
        assertEquals(gpu.getVramSize(), vramSize +1);
    }


    @Test
    void Train(){
        // inserting processed db to vram;
        DataBatch db = new DataBatch();
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
    void clearGpu(){

        // filling all disks:
        // disk - model databatches
        // vram manualy inserted
        // trainedDisk caling Test once
        DataBatch db = new DataBatch();
        db.setProcessed(true);
        Model model = new Model();
        gpu.insertModel(model);
        gpu.insertDbToVram(db);
        gpu.insertDbToVram(db);
        gpu.Train();

        //clearing GPU disks
        gpu.clearGpu();

        //check disks are empty
        assertEquals(gpu.getTrainedDiskSize(),0);
        assertEquals(gpu.getDiskSize(),0);
        assertEquals(gpu.getVramSize(),0);
    }




}