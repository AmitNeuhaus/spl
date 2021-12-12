package bgu.spl.mics.application.objects;

/**
 * Passive object representing single student.
 * Add fields and methods to this class as you see fit (including public methods and constructors).
 */
public class Student {
    /**
     * Enum representing the Degree the student is studying for.
     */
    enum Degree {
        MSc, PhD
    }

    private String name;
    private String department;
    private Degree status;
    private int publications;
    private int papersRead;

    public Student(String name,String department, Degree status){
        this.name = name;
        this.department = department;
        this.status = status;
        publications = 0;
        papersRead = 0;
    }


    public Student(){
        name = "test student";
        department = "Computer Science";
        status = Degree.MSc;
        publications = 0;
        papersRead = 0;
    }



    public Degree getDegree(){
        return status;
    }

    public String getName(){return name;}

    public String getDepartment(){return department;}

    public int getPublications(){return publications;}

    public int getPapersRead(){return papersRead;}

    public void addPublication(){publications++;}

    public void addPapersRead(){papersRead++;}

}
