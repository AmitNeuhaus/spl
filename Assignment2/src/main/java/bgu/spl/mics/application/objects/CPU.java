package bgu.spl.mics.application.objects;

import java.util.Collection;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU implements CPUInterface {

    private int cores;
    private Collection<DataBatch> data;
    private boolean busy;
    private Cluster cluster;


    @Override
    public void process(DataBatch db) {

    }

    @Override
    public void insertDB(DataBatch db) {

    }

    @Override
    public DataBatch sendProcessedDB(DataBatch db) {
        return null;
    }

    @Override
    public int dataSize() {
        return 0;
    }

}
