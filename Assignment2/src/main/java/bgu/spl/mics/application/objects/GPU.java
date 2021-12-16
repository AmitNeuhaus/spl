package bgu.spl.mics.application.objects;


import bgu.spl.mics.application.services.GPUTimeService;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Passive object representing a single GPU.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class GPU implements GPUInterface{

    /**
     * Enum representing the type of the GPU.
     */
    enum Type {RTX3090, RTX2080, GTX1080}
    enum TestResults {BAD, GOOD, NONE}


    private Type type;
    public Model model;
    Cluster cluster;
    private GPUTimeService gpuTimeService;
    private int numOfBatchesToSend;
    private int numberOfBatches;
    private int currentBatchNum;

    // Memory:
    int vramCapacity;
    LinkedBlockingQueue<DataBatch> disk;
    LinkedBlockingQueue<DataBatch> trainedDisk;
    LinkedBlockingQueue<DataBatch> vRam;

    public GPU(Type type,GPUTimeService gpuTimeService){
        this.type = type;
        this.gpuTimeService = gpuTimeService;
        this.vramCapacity = getVramCapacity();
        cluster = Cluster.getInstance();
        cluster.registerGPUToCluster(this);
        disk = new LinkedBlockingQueue<>();
        trainedDisk = new LinkedBlockingQueue<>();
        vRam = new LinkedBlockingQueue<>(vramCapacity);
        numOfBatchesToSend = vramCapacity;
    }

    public GPU(GPUTimeService gpuTimeService){
        this.type = Type.RTX3090;
        this.gpuTimeService = gpuTimeService;
        this.vramCapacity = getVramCapacity();
        cluster = Cluster.getInstance();
        disk = new LinkedBlockingQueue<>();
        trainedDisk = new LinkedBlockingQueue<>();
        vRam = new LinkedBlockingQueue<>(vramCapacity);
        numOfBatchesToSend = vramCapacity;
        cluster.registerGPUToCluster(this);

    }

    @Override
    public void insertModel(Model model) {
        if (disk.size() == 0 && model.getStatus() != Model.statusEnum.Training){
            this.model = model;
            this.numberOfBatches = model.getDataSize()/1000;
            this.currentBatchNum = 0;
        }
    }

    @Override
    public void splitToBatches() {
        while(numberOfBatches>currentBatchNum){
            disk.add(new DataBatch(model.getData(),currentBatchNum*1000,this));
            currentBatchNum++;
        }
    }

    @Override
    public void sendData() {
        if (disk.size()>0){
            cluster.insertUnProcessedData(disk.poll());
            numOfBatchesToSend--;
        }
    }

    @Override
    public void insertDbToVram(DataBatch db) {
        if (db.isProcessed()){
           try{
               vRam.put(db);
           }catch(Exception ignored){

           }
        }
    }

    @Override
    public void insertTrainedDbToDisk(DataBatch db) {
        if (db.isTrained()){
            trainedDisk.add(db);
        }
    }



    @Override
    public boolean isVramFull() {
        return getVramSize() == vramCapacity;
    }

    @Override
    public int getDiskSize() {
        return disk.size();
    }

    @Override
    public int getTrainedDiskSize() {
        return trainedDisk.size();
    }

    @Override
    public int getVramSize() {
        return vRam.size();
    }

    @Override
    public DataBatch nextDataBatchDisk() {
        return disk.peek();
    }

    @Override
    public DataBatch nextDataBatchVram() {
        return vRam.peek();
    }

    public int getNumOfBatchesToSend(){return numOfBatchesToSend;}

    public int getNumberOfBatches(){return numberOfBatches;}

    public int getCurrentBatchNum(){return currentBatchNum;}


    @Override
    public void Train() throws InterruptedException {
        if (!vRam.isEmpty()){
            DataBatch db = vRam.poll();
            numOfBatchesToSend++;
            int trainTime = calculateTrainTime();
            gpuTimeService.remindMeIn(trainTime);
            db.setTrained(true);
            insertTrainedDbToDisk(db);
            model.getData().addProcessedData();
        }
    }

    @Override
    public Model.results testModel() {
        Random random = new Random();
        int num = 1 + random.nextInt(10);
        if (model.getStudent().getDegree() == Student.Degree.MSc && num <= 6){
            return Model.results.Good;
        }else if (model.getStudent().getDegree() == Student.Degree.PhD && num <= 8){
            return Model.results.Good;
        }
        return Model.results.Bad;
    }

    @Override
    public void clearGpu() {
        disk.clear();
        trainedDisk.clear();
        vRam.clear();
    }

    private int calculateTrainTime(){
        if (type == Type.RTX3090){
            return 1;
        }else if(type == Type.RTX2080){
            return 2;
        }else{
            return 4;
        }
    }

    private int getVramCapacity(){
        if (type == Type.RTX3090){
            return 32;
        }else if(type == Type.RTX2080){
            return 16;
        }else{
            return 8;
        }
    }

}
