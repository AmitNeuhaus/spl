package bgu.spl.mics;

import bgu.spl.mics.application.objects.DataBatch;

public interface CPUInterface {
     /**
      * @param db
      */
     void proccess(DataBatch db);

     /**
      * @pre isBusy() == false
      * @pre db != null
      * @post this.db == db;
      * @param db
      */
     public void setDB(DataBatch db);

     /**
      * @pre busy == false
      * @post this.db.processed == true
      * @param db
      * @return == this.db
      */
     public DataBatch returnProcessedDB(DataBatch db);

     /**
      *
      * @return == busy
      */
     public boolean isBusy();

}
