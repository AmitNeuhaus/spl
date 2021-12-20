package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.CPUService;
import bgu.spl.mics.application.services.GPUTimeService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

class CPUTest {

    private CPU cpu;
    private GPU gpu;

    @BeforeEach
    void setUp(){
        cpu = new CPU(32,new CPUService(), 5);
        gpu = new GPU(new GPUTimeService());

    }


    @Test
    void insertDB() {
        int dataSize = cpu.getDataSize();
        DataBatch db = new DataBatch();
        cpu.insertDB(db);
        assertEquals(cpu.getDataSize(), dataSize + 1);
        db.setProcessed(true);
        dataSize = cpu.getDataSize();
        assertEquals(cpu.getDataSize(), dataSize);
    }

    @Test
    void sendProcessedDB() throws InterruptedException {
        int dataSize = gpu.getVramSize();
        DataBatch db = new DataBatch(new Data(Data.Type.Images,10000),0,gpu);
        db.setProcessed(true);
        cpu.sendProcessedDB(db);
        assertEquals(gpu.getVramSize(), dataSize +1 );
        dataSize = gpu.getVramSize();
        db.setProcessed(false);
        assertEquals(gpu.getVramSize(), dataSize);
    }

}