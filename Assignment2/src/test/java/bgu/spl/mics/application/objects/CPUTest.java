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
    void insertDB() {
        int dataSize = cpu.getDataSize();
        DataBatch db = new DataBatch();
        assertFalse(db.isProcessed());
        cpu.insertDB(db);
        assertEquals(cpu.getDataSize(), dataSize + 1);
    }

    @Test
    void sendProcessedDB() {
        int dataSize = cpu.getDataSize();
        cpu.sendProcessedDB(new DataBatch());
        assertEquals(cpu.getDataSize(), dataSize - 1);
    }

}