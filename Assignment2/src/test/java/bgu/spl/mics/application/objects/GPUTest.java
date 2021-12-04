package bgu.spl.mics.application.objects;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

class GPUTest {

    private GPU gpu;


    @BeforeEach
    void setUp(){
        gpu = new GPU();
    }

    @Test
    void insertModel() {
        Model model = new Model();
        int dataSize = model.getDataSize();
        assertNotEquals(model.status, Model.statusEnum.Training);
        assertNotEquals(model.status, Model.statusEnum.Tested);

        assertEquals(gpu.getDiskSize(), 0);
        // inserting PreTrained model -> Expected: Change Gpu model
        model.setStatus(Model.statusEnum.PreTrained);
        gpu.insertModel(model);
        assertEquals(gpu.model, model);
        assertEquals(gpu.model.status, model.status);
        assertEquals(gpu.getDiskSize(), dataSize/1000);

        // inserting PreTrained model -> Expected: Change Gpu model
        model.setStatus(Model.statusEnum.Trained);
        gpu.insertModel(model);
        assertEquals(gpu.model, model);
        assertEquals(gpu.model.status, model.status);
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
        assertThrows("cant insert unprocessed db to vram", Exception.class, ()-> gpu.insertDbToVram(db));

        db.setProcessed(true);
        int vramSize = gpu.getVramSize();
        gpu.insertDbToVram(db);
        assertEquals(gpu.getVramSize(), vramSize +1);
    }

}