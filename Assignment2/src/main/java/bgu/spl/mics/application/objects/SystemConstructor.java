package bgu.spl.mics.application.objects;



import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.services.*;

import java.util.LinkedList;

public class SystemConstructor {

    FileParser fileParser;
    LinkedList<MicroService> systemServices;
    LinkedList<Thread> systemThreads;

    public SystemConstructor(String fileName){
        fileParser = new FileParser(fileName);
        systemServices = new LinkedList<>();
        systemThreads = new LinkedList<>();
    }

    public void buildSystem(){
        //Build student services
        Student[] students = fileParser.getStudents();
        for(Student student : students){
            StudentService studentService = new StudentService(student.getName());
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
        Integer[] cpuCores = fileParser.getCPU();
        for(Integer cores : cpuCores){
            CPUService cpuService = new CPUService();
            CPU cpu = new CPU(cores,cpuService);
            CPUManager cpuManager = new CPUManager(cpu);
            systemServices.add(cpuService);
            systemServices.add(cpuManager);
            Thread thread1 = new Thread(cpuService);
            Thread thread2 = new Thread(cpuManager);
            systemThreads.add(thread1);
            systemThreads.add(thread2);
        }

        //Build TimeService
        int tickTime = fileParser.getTickTime();
        int duration = fileParser.getDuration();
        TimeService timeservice = new TimeService(duration,tickTime,this);
        systemServices.add(timeservice);
        Thread thread = new Thread(timeservice);
        systemThreads.add(thread);
    }

    public void runSystem(){

    }

    public void terminateSystem(){
        for (Thread t : systemThreads){
            t.interrupt();
        }
    }


}
