package bgu.spl.mics.application.objects;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MicroServiceArray<E> {
    private final CopyOnWriteArrayList<E> arr;
    private AtomicInteger ctr;

    public MicroServiceArray(){
        arr = new CopyOnWriteArrayList<E>();
        ctr = new AtomicInteger(-1);
    }

    public int getNextIndex(){
        if (arr.size()>0){
            return ctr.incrementAndGet() % arr.size();

        }
        throw new ArithmeticException("ERROR: in class microServiceArray");
    }

    public CopyOnWriteArrayList<E> getArray(){
        return arr;
    }
}
