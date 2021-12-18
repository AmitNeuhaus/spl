package bgu.spl.mics.application.objects;

public class FilePath {
    public static String folderPath = System.getProperty("user.dir");
    public static String inputFileNamePath;
    public static String outputFileNamePath;
    public static void setPathes(String inputFileName, String outputFileName){
        inputFileNamePath = folderPath+ "/" + inputFileName;
        outputFileNamePath = folderPath+ "/" + outputFileName;
    }

}
