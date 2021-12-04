package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class FutureTest {


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
        assertEquals(future.get(), results);

    }

    @Test
    void isDone() {
    }

    @Test
    void testGetWithinTimeout() throws InterruptedException {
        Integer expectedResult =1000;

        class Resolver implements Runnable {
            public void run(){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                future.resolve(expectedResult);
            }
        }

        class Getter implements Runnable {

            public Integer result;
            public void run(){
               result = future.get(2000,TimeUnit.MILLISECONDS);
            }
        }
        Getter g = new Getter();
        Thread t1 = new Thread (new Resolver());
        Thread t2 = new Thread (g);
        t1.start();
        t2.start();

//        scenario 1 task takes less than timeout
        System.out.println("before");
        System.out.println("ok");
        assertEquals(g.result, expectedResult);
    }


    @Test
    void testGetLongerThanTimeout() throws InterruptedException {

        Integer expectedResult = 1000;

        class Resolver implements Runnable {
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                future.resolve(expectedResult);
            }
        }

        class Getter implements Runnable {

            public Integer result;

            public void run() {
                result = future.get(2000, TimeUnit.MILLISECONDS);
            }
        }
        Getter g = new Getter();
        Thread t1 = new Thread(new Resolver());
        Thread t2 = new Thread(g);
        t1.start();
        t2.start();

//        scenario 1 task takes less than timeout
        System.out.println("before");
        System.out.println("ok");
        assertNull(g.result);
    }
}