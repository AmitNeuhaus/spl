package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class FutureTest<T> {


    private Future<Integer> future;

    @BeforeEach
    void setUp(){
        future = new Future<Integer>();
    }

    @Test
    void get() {}

    @Test
    void resolve() {
        assertFalse(future.isDone());
        Integer results = 25;
        future.resolve(results);
        assertTrue(future.isDone());
        assertNotEquals(future.get(), null);
    }

    @Test
    void isDone() {
    }

    @Test
    void testGetWithinTimeout() throws InterruptedException {

        long timeout = 50;
        TimeUnit units = TimeUnit.MILLISECONDS;
        Integer expectedResult =1000;
        //scenario 1 task takes less than timeout
        Thread.sleep(15);
        Integer result = future.get(timeout,units);
        assertEquals(result, expectedResult);
    }


    @Test
    void testGetLongerThanTimeout() throws InterruptedException {

        long timeout = 50;
        TimeUnit units = TimeUnit.MILLISECONDS;

        //scenario 1 task takes less than timeout
        Thread.sleep(70);
        Integer result = future.get(timeout,units);
        assertNull(result);
    }

}