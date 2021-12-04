package bgu.spl.mics.application.objects;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GPUTest {

    private GPU gpu;


    @BeforeEach
    void setUp(){
        gpu = new GPU();
    }

    @Test
    void insertModel() {

        Model model = new Model();

        // inserting PreTrained model -> Expected: Change Gpu model
        model.setStatus(Model.statusEnum.PreTrained);
        gpu.insertModel(model);
        assertEquals(gpu.model.status, model.status);

        // inserting PreTrained model -> Expected: Change Gpu model
        model.setStatus(Model.statusEnum.Trained);
        gpu.insertModel(model);
        assertEquals(gpu.model.status, model.status);

        // inserting Tested model -> Expected: Change Gpu model
        model.setStatus(Model.statusEnum.Tested);
        gpu.insertModel(model);
        assertEquals(gpu.model.status, model.status);


        // inserting training model -> Expected: throw Error
        try {
            model.setStatus(Model.statusEnum.Training);
            gpu.insertModel(model);
            Assert.fail("error was not thrown, should throw an error.");
        }catch (Exception e){}

    }

    @Test
    void splitToBatches() {
        int dataSize = 10000;
        Data data = new Data(Data.Type.Images, dataSize, 0);
        gpu.splitToBatches(data);
        assertEquals(gpu.getDiskSize(), dataSize/1000);

        // trying to insert data when disk isnt clean -> throws error
        try {
            gpu.splitToBatches(data);
            Assert.fail("should have thrown error: current disk isnt empty.");
        }catch (Exception e){}
    }

    @Test
    void sendData() {
        // if disk is empty -> throw error
        try {
            gpu.sendData();
            Assert.fail("Should throw error if disk is empty - no data to send to process");
        }catch(Exception e){}

        // created a gpu with data
        int dataSize = 10000;
        Data data = new Data(Data.Type.Images, dataSize, 0);
        gpu.splitToBatches(data);
        int diskSize = gpu.getDiskSize();
        gpu.sendData();
        assertEquals(gpu.getDiskSize() , diskSize - 1);


        // inserted DataBatch is processed therefore Exception
        //TODO: should i insert a processed databatch to disk to check this?...
        DataBatch db = new DataBatch();
        db.setProcessed(true);
        try {
            gpu.sendData();
            Assert.fail("Should throw Exception because the sent DB is Processed already");
        }catch (Exception e){}
    }

    @Test
    void insertDbToVram() {
        //Todo: i need to use insertDbToVram to check insertDbToVram we need to read how to do it properly.
        // create new databatch and check if vram size = vram size +1
        // check if the db is processed only
        // check if refuse insertion if vram is full.
    }

    @Test
    void insertTrainedDbToDisk() {
        //Todo: i need to use insertDbToVram to check insertDbToVram we need to read how to do it properly.
    }

    @Test
    void isVramFull() { }

    @Test
    void getDiskSize() {
    }

    @Test
    void getTrainedDiskSize() {
    }

    @Test
    void getVramSize() {
    }

    @Test
    void train() {
        //Todo: provate method not test needed.
    }

    @Test
    void test() {
    }

    @Test
    void returnResult() {
        int dataSize = 10000;
        for (int i=0; i < dataSize/1000; i++){
            DataBatch db = new DataBatch();
            db.setProcessed(true);
            gpu.insertDbToVram(db);
            //Train will happen automatically when VRAM is getting filled.
        }
        gpu.returnResult();
        assertEquals(gpu.getDiskSize(),0);
        assertEquals(gpu.getVramSize(),0);
        assertEquals(gpu.getTrainedDiskSize(), 0);
    }
}