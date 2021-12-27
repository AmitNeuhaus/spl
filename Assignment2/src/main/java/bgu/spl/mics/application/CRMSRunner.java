package bgu.spl.mics.application;


import bgu.spl.mics.application.objects.*;





/** This is the Main class of Compute Resources Management System application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output a text file.
 */
public class CRMSRunner {
    public static void main(String[] args) {
        String  inputFileName = args[0];
        String  outputFileName = args[1];
        FilePath.setPathes(inputFileName,outputFileName);
        SystemConstructor sysCon = new SystemConstructor(FilePath.inputFileNamePath);
        sysCon.buildSystem();
        sysCon.runSystem();
    }
//        MessageBusImpl msb = MessageBusImpl.getInstance();
//        GPUTimeService gpuTimeService = new GPUTimeService();
//        GPU gpu1 = new GPU(gpuTimeService);
//        CPUService cpuService = new CPUService();
//        CPU cpu1 = new CPU(32 ,cpuService,5);
//
//        GPUService gpuService = new GPUService(gpu1);
//        TimeService timeService = new TimeService(1000, 1000);
//        TrainModelEvent trainModelEvent = new TrainModelEvent(new Model());
//        CPUManagerService cpuManager = new CPUManagerService(cpu1);
//
//        ExecutorService executor = Executors.newCachedThreadPool();
//        executor.execute(gpuService);
//        executor.execute(timeService);
//        executor.execute(gpuTimeService);
//        executor.execute(cpuService);
//        executor.execute(cpuManager);
//        executor.shutdown();
////        Thread t1 = new Thread(gpuService);
////        Thread t2 = new Thread(timeService);
////        Thread t3 = new Thread(gpuTimeService);
////        Thread t4 = new Thread(cpuService);
////        Thread t5 = new Thread(cpuManager);
//
////        t1.start();
////        t2.start();
////        t3.start();
////        t4.start();
////        t5.start();
//
//
//        try{
//            Thread.sleep(3000);
//            MessageBusImpl.getInstance().sendEvent(trainModelEvent);
//        }catch(Exception ignored){
//
//        }
//
//
//
//    }
//
//
//
//    public static void CalculateCPUWeights(int[] allCpuCores){
//
//        if (allCpuCores.length == 0){
//            throw new IllegalArgumentException("No cpu cores inserted");
//        }
//
//        int[] weights = new int[allCpuCores.length];
//        int overAllCores=0;
//        for (int i =0; i <allCpuCores.length ; i++){
//            overAllCores += allCpuCores[i];
//        }
//
//        for (int i =0; i <allCpuCores.length ; i++){
//            weights[i] = allCpuCores[i]/ overAllCores;
//        }
//    }
}
