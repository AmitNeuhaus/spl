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

   public Model(String name, Data data, Student student){
       this.name = name;
       this.data = data;
       this.student = student;
       status = statusEnum.PreTrained;
   }

   public Model(){
       name = "Test model";
       data = new Data(Data.Type.Images,10000,0);
       student = new Student();
       status = statusEnum.PreTrained;
   }

    public int getDataSize(){
        return 0;
    }
    public Data getData(){return data;}
    public void setStatus(statusEnum modelStatus){}
    public Student getStudent(){return student;}

}
