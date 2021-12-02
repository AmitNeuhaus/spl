package bgu.spl.mics.application.objects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CPUTest {
    CPU cpu;

    @BeforeEach
    void setUp(){
        cpu = new CPU();
    }
    @Test
    void setDB() {
        Assertions.assertEquals(cpu.isBusy(),false);
        Assertions.assertThrows(IllegalArgumentException.class,()->cpu.setDB(null));
    }

    @Test
    void returnProcessedDB() {
    }

    @Test
    void isBusy() {
    }
}