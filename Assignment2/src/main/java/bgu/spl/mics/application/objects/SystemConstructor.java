package bgu.spl.mics.application.objects;



import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SystemConstructor {

    FileParser fileParser;
    LinkedList<MicroService> systemServices;
    Student[] students;
    ConferenceInformation[] conferences;
    LinkedList<Thread> systemThreads;
//    ExecutorService pool ;

    public SystemConstructor(String fileName){
        fileParser = new FileParser(fileName);
        systemServices = new LinkedList<>();
        systemThreads = new LinkedList<>();
//        pool = Executors.newCachedThreadPool();
        students = new Student[]{};
        conferences = new ConferenceInformation[]{};
    }
    private int[] calculateCPUWeights(int[] allCpuCores){
        if (allCpuCores.length == 0){
            throw new IllegalArgumentException("No cpu cores inserted");
        }

        int[] weights = new int[allCpuCores.length];
        int minCores = allCpuCores[0];
        int maxWeight = 0;
        for (int i =0; i <allCpuCores.length ; i++){
            if (minCores>allCpuCores[i]){
                minCores = allCpuCores[i];
            }
        }

        for (int i =0; i <allCpuCores.length ; i++){
            weights[i] = allCpuCores[i]/ minCores;
            if (weights[i]>maxWeight){
                maxWeight = weights[i];
            }
        }
        Cluster.getInstance().setMaxRounds(maxWeight);
        return weights;
    }

    public void buildSystem(){
        //Build student services
        students = fileParser.getStudents();
        for(Student student : students){
            StudentService studentService = new StudentService(student);
            systemServices.add(studentService);
            Thread thread = new Thread(studentService, "Student Service Thread + " + student.getName());
            systemThreads.add(thread);
        }

        //Build conference services
        conferences = fileParser.getConf();
        for (ConferenceInformation confInfo : conferences){
            ConferenceService confService = new ConferenceService(confInfo);
            systemServices.add(confService);
            Thread thread = new Thread(confService, "Conference Service Thread + " + confInfo.getName());
            systemThreads.add(thread);
        }

        //Build system GPUServices
        GPU.Type[] gpuTypes = fileParser.getGPU();
        GPUTimeService gpuTimeService = new GPUTimeService();
        for (GPU.Type gpuType : gpuTypes){
            GPU gpu = new GPU(gpuType,gpuTimeService);
            GPUService gpuService = new GPUService(gpu);
            systemServices.add(gpuService);
            systemServices.add(gpuTimeService);
            Thread thread1 = new Thread(gpuTimeService, "GPU Time  Service Thread");
            Thread thread2 = new Thread(gpuService, "GPU Service Thread");
            systemThreads.add(thread1);
            systemThreads.add(thread2);
        }

        //Build system CPUServices
        int[] cpuCores = fileParser.getCPU();
        int[] cpuWeghits = calculateCPUWeights(cpuCores);
        int i = 0;
        CPUService cpuService = new CPUService();
        for(int cores : cpuCores){
            CPU cpu = new CPU(cores,cpuService,cpuWeghits[i]);
            CPUManagerService cpuManager = new CPUManagerService(cpu);
            systemServices.add(cpuService);
            systemServices.add(cpuManager);
            Thread thread1 = new Thread(cpuService, "CPU Service Thread");
            Thread thread2 = new Thread(cpuManager, "CPU Manager Thread");
            systemThreads.add(thread1);
            systemThreads.add(thread2);
            i++;
        }

        //Build TimeService
        int tickTime = fileParser.getTickTime();
        int duration = fileParser.getDuration();
        TimeService timeservice = new TimeService(duration,tickTime,this);
        systemServices.add(timeservice);
        Thread thread = new Thread(timeservice, "Time Service Thread");
        systemThreads.addFirst(thread);
    }

    public void runSystem(){
        Thread timeService = systemThreads.removeFirst();
        for (Thread t : systemThreads){
            t.start();
        }
        timeService.start();
        systemThreads.add(timeService);
    }

    public void terminateSystem(){
        for (Thread t : systemThreads){
            t.interrupt();
        }
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        LinkedHashMap map = new LinkedHashMap();
        map.put("Students",students);
        map.put("Conferences",conferences);
        map.put("cpuTimeUsed",Cluster.getInstance().getCpuTimedUsed());
        map.put("gpuTimeUsed",Cluster.getInstance().getGpuTimedUsed());
        map.put("batchesProcessed",Cluster.getInstance().getBatchesProcessed());

        try {
            Writer writer = new FileWriter(FilePath.outputFileName);
            gson.toJson(map,writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Thread t : systemThreads){
            if(t.isAlive()){
                System.out.println(t.getName() + " im alive");
            };
        }
    }





}
