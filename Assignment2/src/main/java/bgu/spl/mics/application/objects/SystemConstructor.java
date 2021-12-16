package bgu.spl.mics.application.objects;



import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.*;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SystemConstructor {

    FileParser fileParser;
    LinkedList<MicroService> systemServices;
    LinkedList<Thread> systemThreads;
    ExecutorService pool ;

    public SystemConstructor(String fileName){
        fileParser = new FileParser(fileName);
        systemServices = new LinkedList<>();
        systemThreads = new LinkedList<>();
        pool = Executors.newFixedThreadPool(6);
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
        Student[] students = fileParser.getStudents();
        for(Student student : students){
            StudentService studentService = new StudentService(student);
            systemServices.add(studentService);
            Thread thread = new Thread(studentService);
            systemThreads.add(thread);
        }

        //Build conference services
        ConferenceInformation[] conferences = fileParser.getConf();
        for (ConferenceInformation confInfo : conferences){
            ConferenceService confService = new ConferenceService(confInfo.getName(), confInfo.getDate());
            systemServices.add(confService);
            Thread thread = new Thread(confService);
            systemThreads.add(thread);
        }

        //Build system GPUServices
        GPU.Type[] gpuTypes = fileParser.getGPU();
        for (GPU.Type gpuType : gpuTypes){
            GPUTimeService gpuTimeService = new GPUTimeService();
            GPU gpu = new GPU(gpuType,gpuTimeService);
            GPUService gpuService = new GPUService(gpu);
            systemServices.add(gpuService);
            systemServices.add(gpuTimeService);
            Thread thread1 = new Thread(gpuTimeService);
            Thread thread2 = new Thread(gpuService);
            systemThreads.add(thread1);
            systemThreads.add(thread2);
        }

        //Build system CPUServices
        int[] cpuCores = fileParser.getCPU();
        int[] cpuWeghits = calculateCPUWeights(cpuCores);
        int i = 0;
        for(int cores : cpuCores){
            CPUService cpuService = new CPUService();
            CPU cpu = new CPU(cores,cpuService,cpuWeghits[i]);
            CPUManagerService cpuManager = new CPUManagerService(cpu);
            systemServices.add(cpuService);
            systemServices.add(cpuManager);
            Thread thread1 = new Thread(cpuService);
            Thread thread2 = new Thread(cpuManager);
            systemThreads.add(thread1);
            systemThreads.add(thread2);
            i++;
        }

        //Build TimeService
        int tickTime = fileParser.getTickTime();
        int duration = fileParser.getDuration();
        TimeService timeservice = new TimeService(duration,tickTime,this);
        systemServices.add(timeservice);
        Thread thread = new Thread(timeservice);
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

    }




}
