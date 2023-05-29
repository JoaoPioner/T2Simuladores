
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.io.PrintWriter;
import java.io.File;

public class GeradorNumeros {
    
    private double x0 = 28;
    private double a = 21;
    private double m = 5000000011L;
    private double c = 227;

    private double n = 1000;
    private List<Double> numeros = new ArrayList<Double>();

    public GeradorNumeros(double x0,double a,double m, double c, double n){
        this.x0 = x0;
        this.a = a;
        this.m = m;
        this.c = c;
        this.n = n;
        GeraNumeros();
        GravaNumeros();
    }

    public List<Double> getNumeros(){
        return numeros;
    }

    public void GravaNumeros() {
        try {
            PrintWriter pw = new PrintWriter(new File("Aleatorios_Utilizados.txt"));
            for(double num : numeros) {
                pw.println("- " + num);
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GeraNumeros(){
        double xi = x0;
        for(int i = 1; i <= n; i++){
            xi = (a * xi + c) % m;
            Locale.setDefault(Locale.US);
            double ui = xi / m;
            ui = Double.parseDouble(String.format("%.4f", ui));
            numeros.add(ui);
            //System.out.println(ui);
        }
    }

    public static void main(String[] args){
        
        GeradorNumeros g = new GeradorNumeros(28,21,5000000011L,227,1000);
    }

}
