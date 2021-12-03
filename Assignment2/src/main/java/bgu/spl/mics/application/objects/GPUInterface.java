package bgu.spl.mics.application.objects;

import bgu.spl.mics.Future;

public interface GPUInterface {

    /**
     * Gets a model from the GpuService.
     * @param model
     * @pre model.status != "Training" (gpu is already working on a model)
     * @post this.model == model
     */
    void insertModel(Model model);


    /**
     * Split model's data into DataBatches of 100 samples , and stores it in the disk.
     * @pre diskSize() == 0
     * @post diskSize() == data.size / 1000
     */
    void splitToBatches(Data data);


    /**
     * Todo: Talk if we should send only one DataBatch or multiple of them.
     * Send DataBatch to the Cluster.
     * @pre diskSize() > 0;
     * @pre db.processed = False;
     * @post diskSize() == @pre diskSize() -1
     */
    void sendData(DataBatch db);


    /**
     * Insert's a db from the cluster to the vRam (after processed by a CPU)
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
     * @pre db.processed = True;
     * @pre db.trained = False;
     * @post db.trained = True;
     */
    void Train(DataBatch db);


    /**
     * Runs a Test of the Model
     */
    void Test(DataBatch db);


    /**
     * Return a result of the model after finished {@Train} for ALL the data batches.
     * @pre trainedDiskSize() == data.size / 1000
     * @pre vram.size() == 0
     * @post trainedDiskSize() == 0
     */
    Future returnResult();


    // ---------- Queries

    /**
     * check if the vRam is full (we will send unprocessed data from the disk only if we have place to get it back in the vram) (Query)
     * @return vramSize == vramCapacity
     */
    boolean isVramFull(DataBatch db);


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
}
