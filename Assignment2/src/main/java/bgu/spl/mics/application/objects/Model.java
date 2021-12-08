package bgu.spl.mics.application.objects;

/**
 * Passive object representing a Deep Learning model.
 * Add all the fields described in the assignment as private fields.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Model {

    public enum statusEnum {
        PreTrained,
        Training,
        Trained,
        Tested
    }

   private String name;
   private Data data;
   private Student student;
   public statusEnum status;

    public int getDataSize(){
        return 0;
    }
    public void setStatus(statusEnum modelStatus){}

}
