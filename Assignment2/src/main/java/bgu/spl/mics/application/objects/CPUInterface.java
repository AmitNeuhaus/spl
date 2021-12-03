package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.objects.DataBatch;

public interface CPUInterface {
     /**
      * This methods process one db from the data Container.
      * @param db
      * @pre isBusy == False
      * @pre db.processed == False
      * @post isBusy == False
      */
     void process(DataBatch db);


     /**
      * This methods get a DataBatch from the cluster and insert it to the data container of the cpu. (Command)
      * @param db
      * @pre db.processed == False;
      * @post data.size() == @pre data.size() +1
      */
     public void insertDB(DataBatch db);


     /**
      * return a batch that the cpu finished processing. (Command)
      * @param db
      * @pre db.processed == True
      * @post data.size() == data.size() - 1
      */
     public DataBatch sendProcessedDB(DataBatch db);


     /**
      * check if CPU is currently processing a batch (Query)
      * @pre none
      * @post none
     */
     public boolean isBusy();


     /**
      * Size of inserted data(Query)
      */
     public int dataSize();
}
