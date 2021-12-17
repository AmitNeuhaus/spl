package bgu.spl.mics.application.objects;


import bgu.spl.mics.MessageBusImpl;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the cluster.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Cluster {


	/**
     * Retrieves the single instance of this class.
     */

	CopyOnWriteArrayList<CPU> cpuList = new CopyOnWriteArrayList<>();
    ConcurrentHashMap<GPU,GPU> gpuHash = new ConcurrentHashMap<>();

    //statistics fields:

    private AtomicInteger cpuTimedUsed= new AtomicInteger(0);
    private AtomicInteger gpuTimedUsed=new AtomicInteger(0);
    private  AtomicInteger batchesProcessed=new AtomicInteger(0);

    // Params for continous Weighted Round Robin.
    int maxRounds;
    int currentRound=1;
    int currentCpu=0;

    private static class ClusterHolder{
        private static Cluster instance = new Cluster();
    }

    public Cluster(){
        maxRounds = calculateMaxCpuWeight();
    }
	public static Cluster getInstance() {
        return ClusterHolder.instance;
	}

	public void registerGPUToCluster(GPU gpu){
        //TODO: check if needed.
        gpuHash.put(gpu,gpu);
    }

    public void registerCPUToCluster(CPU cpu){
        // We will Construct the CPUS by their power order (DESCENDING).
        cpuList.add(cpu);
    }

    public synchronized void insertUnProcessedData(DataBatch db) {
        // Implementing Weighted RoundRobin Algorithm.
        boolean inserted = false;
        while (!inserted) {
            currentCpu = currentCpu % cpuList.size();
            CPU currentCpuInstance = cpuList.get(currentCpu);
            if (currentRound <= currentCpuInstance.getWeight()) {
                currentCpuInstance.insertDB(db);
                inserted = true;
            }
            currentCpu++;

            if (currentCpu == cpuList.size() - 1)
                currentRound++;
            if (currentRound == maxRounds) {
                currentRound = 1;
            }
        }
    }


    public void insertProcessedData(DataBatch db) {
        GPU sender = db.getGpuSender();
        // TODO: might be unecessary if holding GPU instance in databatch;
        GPU gpuInstance = gpuHash.get(sender);
        gpuInstance.insertDbToVram(db);
    }


    public int calculateMaxCpuWeight(){
        // max Weight
        int max = 0;
        for (CPU cpu: cpuList){
            if (cpu.getWeight() > max)
                max = cpu.getWeight();
        }
        return max;
    }

    public void setMaxRounds(int max){
        maxRounds = max;
    }


    public  void incrementCpuTimedUsed(int processTime) throws InterruptedException{
        int expected;
        do {
           expected = cpuTimedUsed.intValue();
            if(Thread.currentThread().isInterrupted()){
                throw new InterruptedException();
            }
        } while  (!cpuTimedUsed.compareAndSet(expected, expected + processTime));
    }

    public void incrementGpuTimedUsed(int processTime) throws InterruptedException{
        int expected;
        do{
            expected = gpuTimedUsed.intValue();
            if(Thread.currentThread().isInterrupted()){
                throw new InterruptedException();
            }
        }while(!gpuTimedUsed.compareAndSet(expected,expected+processTime));
    }

    public void  incrementBatchesProcessed() throws InterruptedException{
        int expected;
        do {
            expected = batchesProcessed.intValue();
            if(Thread.currentThread().isInterrupted()){
                throw new InterruptedException();
            }
        }while(batchesProcessed.compareAndSet(expected,expected+1));
    }


    public int getCpuTimedUsed(){return cpuTimedUsed.intValue();}

    public int getGpuTimedUsed(){return gpuTimedUsed.intValue();}

    public int getBatchesProcessed() {
        return batchesProcessed.intValue();
    }
}
