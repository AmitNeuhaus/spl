package bgu.spl.mics.application.objects;

import bgu.spl.mics.Future;

public interface GPUInterface {

    /**
     * Gets a model from the GpuService.
     * @param model
     * @pre diskSize() == 0
     * @pre model.status != "Training" (gpu is already working on a model)
     * @post this.model == model
     * @post diskSize() == data.size / 1000
     *
     */
    void insertModel(Model model);


    /**
     * Split model's data into DataBatches of 1000 samples , and stores it in the disk.
     */
    void splitToBatches(Data data);


    /**
     * Todo: Talk if we should send only one DataBatch or multiple of them.
     * Send DataBatch to the Cluster.
     * @pre diskSize() > 0;
     * @pre nextDataBatchDisk().isProcessed == false;
     * @post diskSize() == {@pre} diskSize() -1
     * @post nextDataBatchDisk() != {@pre} nextDataBatchDisk()
     */
    void sendData();


    /**
     * Insert's a db from the cluster to the vRam (after processed by a CPU)
     * @param db
     * @pre isVramFull == False;
     * @pre db.processed  == True;
     * @post vram.size() == @pre vram.size() +1
     */
    void insertDbToVram(DataBatch db);


    /**
     * Insert's a db from the cluster to the vRam (after processed by a CPU)
     * @pre db.processed == true;
     * @post trainedDisk.size() == @pre trainedDiskSize() +1
     */
    void insertTrainedDbToDisk(DataBatch db);


    /**
     * Train a specific DataBatch
     * @pre nextDataBatchVram().isProcessed = True;
     * @pre nextDataBatchVram().isTrained = False;
     * @post nextDataBatchVram().isProcessed = True;
     * @post nextDataBatchVram().isTrained = True;
     * @post getVramSize() == {@pre} getVramSize() -1
     * @post getTrainedDiskSize() == {@pre} getTrainedDiskSize() + 1
     */
    void Train();


    /**
     * Runs a Test of the Model
     * @pre gpu.model.getStatus == Trained;
     */
    boolean testModel();


    /**
     * Clear all gpu disks.
     * @post getDiskSize() ,getTrainedDiskSize(), getVramSize() ==0
     */
    void clearGpu();
    // ---------- Queries

    /**
     * check if the vRam is full (we will send unprocessed data from the disk only if we have place to get it back in the vram) (Query)
     * @return vramSize == vramCapacity
     */
    boolean isVramFull();


    /**
     * size of the disk.
     */
    int getDiskSize();


    /**
     * size of the disk.
     */
    int getTrainedDiskSize();

    /**
     * size of the vram.
     */
    int getVramSize();


    DataBatch nextDataBatchDisk();

    DataBatch nextDataBatchVram();
}
