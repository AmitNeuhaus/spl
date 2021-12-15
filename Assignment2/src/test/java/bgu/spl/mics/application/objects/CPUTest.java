package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.CPUService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

class CPUTest {

    private CPU cpu;

    @BeforeEach
    void setUp(){
        cpu = new CPU(32,new CPUService(), 5);
    }


    @Test
    void insertDB() {
        int dataSize = cpu.getDataSize();
        DataBatch db = new DataBatch();
        cpu.insertDB(db);
        assertEquals(cpu.getDataSize(), dataSize + 1);
        db.setProcessed(true);
        dataSize = cpu.getDataSize();
        assertThrows("Should throw exception because db is already processed",Exception.class,()->cpu.insertDB(db));
        assertEquals(cpu.getDataSize(), dataSize);
    }

    @Test
    void sendProcessedDB() {
        int dataSize = cpu.getDataSize();
        DataBatch db = new DataBatch();
        db.setProcessed(true);
//        cpu.sendProcessedDB(db);
        assertEquals(cpu.getDataSize(), dataSize - 1);

        dataSize = cpu.getDataSize();
        db.setProcessed(false);
//        assertThrows("Should throw error because sent out DB isnt proceseed", Exception.class,()-> cpu.sendProcessedDB(db));
        assertEquals(cpu.getDataSize(), dataSize);
    }

}