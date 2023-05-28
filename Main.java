import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException{


        for (int i = 1; i <= 5; i++) {
            Fila fila1 = new Fila("fila1", 2, 3, 2.0, 3.0, 2.0, 5.0);
            Fila fila2 = new Fila("fila2", 1, 3, 0, 0, 3.0, 5.0);
        
            double tempoInicial = 2.5;

            ArrayList<Fila> filaLst = new ArrayList<>();
            filaLst.add(fila1);
            filaLst.add(fila2);

            Simulacao simulacao = new Simulacao( filaLst,tempoInicial, (i+1)*10,21,5000000011L,227,100000);
            simulacao.ExecutaAlgoritmo();
        }      
       
    }
}
