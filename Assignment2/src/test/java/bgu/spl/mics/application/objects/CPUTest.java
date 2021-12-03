package bgu.spl.mics.application.objects;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPUTest {


    private CPU cpu;

    @BeforeEach
    void setUp(){
        cpu = new CPU();
    }


    @Test
    void process() {
        assertFalse(cpu.isBusy());
        DataBatch db = new DataBatch();
        assertFalse(db.isProcessed());
        cpu.process(db);
        assertFalse(cpu.isBusy());
        assertTrue(db.isProcessed());
    }

    @Test
    void insertDB() {
        int dataSize = cpu.dataSize();
        DataBatch db = new DataBatch();
        assertFalse(db.isProcessed());
        cpu.insertDB(db);
        assertEquals(cpu.dataSize(), dataSize + 1);
    }

    @Test
    //Todo: Should we check error throwing in different scenarios? --yes
    void sendProcessedDB() {
        int dataSize = cpu.dataSize();
        cpu.sendProcessedDB(new DataBatch());
        assertEquals(cpu.dataSize(), dataSize - 1);
    }

    @Test
    void isBusy() {
    }
}