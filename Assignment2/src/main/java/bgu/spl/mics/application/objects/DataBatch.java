package bgu.spl.mics.application.objects;



/**
 * Passive object representing a data used by a model.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */

public class DataBatch implements DataBatchInterface {

    private Data data;
    private boolean processed;
    private boolean trained;


    DataBatch(){
        processed = false;
        trained = false;
    }
    public boolean isProcessed(){
        return processed;
    };

    public void setProcessed(boolean status){
        processed = status;
    };

    public boolean isTrained(){
        return trained;
    };

    public void setTrained(boolean status){
        trained = status ;
    };

    public Data.Type getDataType(){
        return data.getType();
    }
}
