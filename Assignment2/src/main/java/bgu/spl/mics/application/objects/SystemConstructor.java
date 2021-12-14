package bgu.spl.mics.application.objects;

import bgu.spl.mics.application.services.ConferenceService;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;


public class SystemConstructor {

    JsonObject fileObject;



    public SystemConstructor(String fileName){
        File input = new File(fileName);
        JsonElement jsonElement = null;
        try {
            jsonElement = JsonParser.parseReader(new FileReader(input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.fileObject = jsonElement.getAsJsonObject();
    }



    public int getTickTime(){
        return fileObject.get("TickTime").getAsInt();
    }

    public int getDuration(){
        return fileObject.get("Duration").getAsInt();
    }

    public GPU.Type[] getGPU(){
        JsonArray jsonGPUS =  fileObject.get("GPUS").getAsJsonArray();
        GPU.Type[] gpusType = new GPU.Type[jsonGPUS.size()];
        int i = 0;
        for(JsonElement gpu : jsonGPUS){
            GPU.Type type = getType(gpu.getAsString());
            if (type!=null){
                gpusType[i] = type;
                i++;
            }

        }
        return gpusType;
    }

    public Integer[] getCPU(){
        JsonArray jsonCPUS = fileObject.get("CPUS").getAsJsonArray();
        Integer[] cpuCores = new Integer[jsonCPUS.size()];
        int i = 0;
        for (JsonElement cores : jsonCPUS){
            cpuCores[i] = cores.getAsInt();
            i++;
        }
        Arrays.sort(cpuCores, Collections.reverseOrder());
        return cpuCores;
    }

    public ConfrenceInformation[] getConf(){
        JsonArray jsonConf = fileObject.get("Conferences").getAsJsonArray();
        ConfrenceInformation[] conferences = new ConfrenceInformation[jsonConf.size()];
        int i = 0;
        for (JsonElement conference : jsonConf){
            JsonObject confObject = conference.getAsJsonObject();
            String name = confObject.get("name").getAsString();
            int date = confObject.get("date").getAsInt();
            ConfrenceInformation conf = new ConfrenceInformation(name,date);
            conferences[i] = conf;
            i++;
        }
        return conferences;

    }

    public Student[] getStudents(){
        JsonArray jsonStudentsArray = fileObject.get("Students").getAsJsonArray();
        Student[] students = new Student[jsonStudentsArray.size()];
        int i =0;
        for (JsonElement studentElement : jsonStudentsArray){
            JsonObject studentObject= studentElement.getAsJsonObject();
            String name = studentObject.get("name").getAsString();
            String department = studentObject.get("department").getAsString();
            Student.Degree degree = (studentObject.get("status").getAsString().equals("MSc")) ? Student.Degree.MSc : Student.Degree.PhD;
            Student student = new Student(name,department,degree);
            for (JsonElement modelElement : studentObject.get("models").getAsJsonArray()){
                JsonObject modelObject = modelElement.getAsJsonObject();
                String modelName = modelObject.get("name").getAsString();
                Data.Type type = getDataType(modelObject.get("type").getAsString());
                int size = modelObject.get("size").getAsInt();
                student.addModel(new Model(modelName, new Data(type,size),student));
            }
            students[i] =student;
            i++;
        }
        return students;
    }

    private Data.Type getDataType(String type) {
        switch(type){
            case("images"):
                return Data.Type.Images;
            case("text"):
                return Data.Type.Text;
            case("Tabular"):
                return Data.Type.Tabular;
            default:
                return null;
        }
    }


    private GPU.Type getType(String type){
        switch (type) {
            case "RTX3090":
                return GPU.Type.RTX3090;
            case "RTX2080":
                return GPU.Type.RTX2080;
            case "GTX1080":
                return GPU.Type.GTX1080;
            default:
                return null;
        }
    }




    public static void main(String[] args) {
        String fileName = "/home/tomcooll/Desktop/Personal/Computer Science/Semester c/SPL/spl-course/Assignment2/example_input.json";
        SystemConstructor con = new SystemConstructor(fileName);
        for (ConfrenceInformation conf : con.getConf()){
            System.out.println("name: " + conf.getName() + " date: " + conf.getDate());
        }
        for (GPU.Type type : con.getGPU()){
            System.out.println(type);
        }

        for (int cpu : con.getCPU()){
            System.out.println(cpu);
        }

        for (Student student : con.getStudents()){
            System.out.println(student.toString());
        }

    }



}
