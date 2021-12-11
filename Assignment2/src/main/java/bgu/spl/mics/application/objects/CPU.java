package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.CPUService;


import java.util.LinkedList;
import java.util.Queue;

/**
 * Passive object representing a single CPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class CPU implements CPUInterface {

    private CPUService cpuService;
    private int cores;
    private Queue<DataBatch> data;
    private boolean busy;
    private Cluster cluster;

    public CPU(int cores, CPUService cpuService){
        this.cpuService = cpuService;
        this.cores = cores;
        data = new LinkedList<DataBatch>();
        busy = false;
        cluster = Cluster.getInstance();
    }

    @Override
    public void process(DataBatch db) {
        int start = cpuService.getTime();
        System.out.println("started processing at: " + start);
        busy = true;
        int processTime = calculateProcessTime(db);
        while(cpuService.getTime() - start < processTime){}
        db.setProcessed(true);
        sendProcessedDB(db);
        busy = false;
        System.out.println("finsihed processing time is " + cpuService.getTime());
    }

    @Override
    public void insertDB(DataBatch db) {
        if(!db.isProcessed()) {
            data.add(db);
        }
    }

    @Override
    public void sendProcessedDB(DataBatch db) {
        if (db.isProcessed()){cluster.insertProcessedData(db);}
    }

    @Override
    public int getDataSize() {
        return data.size();
    }

    private int calculateProcessTime(DataBatch db){
        Data.Type type = db.getDataType();
        if (type == Data.Type.Images){
            return (32/cores)*4;
        }else if(type == Data.Type.Text){
            return (32/cores)*2;
        }else{
           return (32/cores);
        }
    }
}
