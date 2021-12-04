package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.objects.DataBatch;

public interface CPUInterface {
     /**
      * This methods process one db from the data Container.
      * @param db
      * @pre isBusy == False
      * @pre db.processed == False
      * @post isBusy == False
      * @post db.processed == True
      */
     void process(DataBatch db);


     /**
      * This methods get a DataBatch from the cluster and insert it to the data container of the cpu. (Command)
      * @param db
      * @pre db.processed == False;
      * @post data.size() == @pre data.size() +1
      */
      void insertDB(DataBatch db);


     /**
      * return a batch that the cpu finished processing. (Command)
      * @param db
      * @pre db.processed == True
      * @post data.size() == data.size() - 1
      */
      DataBatch sendProcessedDB(DataBatch db);

     /**
      * Size of inserted data(Query)
      */
      int getDataSize();
}
