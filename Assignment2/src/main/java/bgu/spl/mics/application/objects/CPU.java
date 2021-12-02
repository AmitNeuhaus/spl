package bgu.spl.mics.application.objects;

import bgu.spl.mics.CPUInterface;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU implements CPUInterface {

    private int cores;
    private DataBatch db;
    private boolean busy;
    private Cluster cluster;

    /**
     * @param db
     */
    @Override
    public void proccess(DataBatch db) {

    }

    @Override
    public void setDB(DataBatch db) {

    }

    @Override
    public DataBatch returnProcessedDB(DataBatch db) {
        return null;
    }

    @Override
    public boolean isBusy() {
        return false;
    }
}
