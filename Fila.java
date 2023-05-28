import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Fila {

    public String name;

    public PrintWriter output;

    public int servers;
    public int capacity;
    public double minService;
    public double maxService;

    public double minArrival;
    public double maxArrival;

    public int size;
    public int loss;

    public double[] tempos;

    // public Fila(String name, int servers, int capacity, double minService, double maxService) throws FileNotFoundException { 
    //     this.name = name;

    //     output = new PrintWriter(new File(name+".csv"));

    //     this.servers = servers;
    //     this.capacity = capacity;
    //     this.minService = minService;
    //     this.maxService = maxService;
    //     this.tempos = new double[capacity+1];
    // }

    public Fila(String name,int servers, int capacity, double minArrival, double maxArrival, double minService, double maxService) throws FileNotFoundException {
        this.name = name;

        output = new PrintWriter(new File(name+".csv"));
        this.servers = servers;
        this.capacity = capacity;

        this.minArrival = minArrival;
        this.maxArrival = maxArrival;

        this.minService = minService;
        this.maxService = maxService;

        this.tempos = new double[capacity+1];
    }
}
