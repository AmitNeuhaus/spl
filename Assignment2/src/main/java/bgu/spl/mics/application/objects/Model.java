package bgu.spl.mics.application.objects;

import java.sql.ResultSet;

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
     public enum results{
        None,
        Good,
        Bad
     }

   private String name;
   private Data data;
   private Student student;
   private statusEnum status;
   private results result;

   public Model(String name, Data data, Student student){
       this.name = name;
       this.data = data;
       this.student = student;
       status = statusEnum.PreTrained;
       result = results.None;
   }

   public Model(){
       name = "Test model";
       data = new Data(Data.Type.Images,10000);
       student = new Student();
       status = statusEnum.PreTrained;
   }

    public int getDataSize(){return data.getSize();}

    public Data getData(){return data;}

    public void setStatus(statusEnum modelStatus){status = modelStatus;}

    public statusEnum getStatus(){return status;}

    public Student getStudent(){return student;}

    public String getName(){return name;}

    public results getResult() {return result;}

    public void setResult(results result){this.result = result;}

    public String toString(){
       return "Name: "+name+'\n'+ "Student: "+ student.getName()+'\n'+"Status: "+status+'\n'+ "Result: "+result+'\n'+"Data: "+data.toString();
    }
}
