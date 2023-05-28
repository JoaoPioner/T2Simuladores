import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.Locale;



public class Simulacao{

    private class Estado{

        private String tipo;
        private double tempo, sorteio;
        public int numEvento;


        public Estado(String tipo, double tempo, double sorteio, int numEvento){
            this.tipo = tipo;
            this.tempo = tempo;
            this.sorteio = sorteio;
            this.numEvento = numEvento;
        }

        public String getTipo(){
            return this.tipo;
        }

        public double getTempo(){
            return this.tempo;
        }

        public String toString(){
            return "Tipo: "+tipo+", Tempo: "+tempo+", Sorteio: "+sorteio;
        }
    }


    public Fila fila1;
    public Fila fila2;

    public double tempoTotal = 0.0;
    public double tempoAnterior = 0.0;

    public List<Double> aleatoriosUtilizados = new ArrayList<Double>();
    public int aleatorioAtual = 0;

    public List<Estado> estados = new ArrayList<>();
    public Estado atual = null;

    public int contadorEventos = 1;

    private ArrayList<Fila> filaLst = new ArrayList<>();

    public Simulacao(ArrayList<Fila> filaLst,double tempoInicial, double x0, double a, double m, double c, double n) throws FileNotFoundException{
        
        Locale.setDefault(Locale.US);

        this.fila1 = filaLst.get(0);
        this.fila2 = filaLst.get(1);

        this.filaLst = filaLst;
        
        aleatoriosUtilizados = new GeradorNumeros(x0,a,m,c,n).getNumeros();

        // aleatoriosUtilizados = Arrays.asList(
        // 0.9921,
        // 0.0004,
        // 0.5534,
        // 0.2761,
        // 0.3398,
        // 0.8963,
        // 0.9023,
        // 0.0132,
        // 0.4569,
        // 0.5121,
        // 0.9208,
        // 0.0171,
        // 0.2299,
        // 0.8545,
        // 0.6001,
        // 0.2921);

        tempoTotal = 0.0000f;
        tempoAnterior = 0.0000f;
        aleatorioAtual = 0;
        Estado e = new Estado("CH1", tempoInicial,0.0,contadorEventos);
        contadorEventos++;
        estados.add(e);
        atual = e;
    }

    public double FormulaConversao(double a, double b, double rand){
        return ((b-a)*rand+a);
    }

    public void contabilizaTempo(){
        double deltaT = tempoTotal-tempoAnterior;
        for (Fila fila : filaLst) {
            fila.tempos[fila.size] += deltaT; 
        }        
    }

    public void chegada(Fila fila){

        contabilizaTempo();

        if(fila.size < fila.capacity){

            fila.size++;

            if(fila.size <= fila.servers){

                double sorteioSaida = FormulaConversao(fila.minService, fila.maxService, aleatoriosUtilizados.get(aleatorioAtual));
                //System.out.println("sorteio: " + sorteioSaida + " - " + fila.minService + " - " + fila.maxService + " - " + aleatoriosUtilizados.get(aleatorioAtual));
                Estado e = new Estado("P12",tempoTotal+sorteioSaida,sorteioSaida,contadorEventos);
                contadorEventos++;
                estados.add(e);
                aleatorioAtual++;

            }

        }else {
            fila.loss++;
        } 

        if (aleatorioAtual < aleatoriosUtilizados.size()) {
            double sorteioChegada = FormulaConversao(fila.minArrival, fila.maxArrival, aleatoriosUtilizados.get(aleatorioAtual));
            Estado e2 = new Estado("CH1",tempoTotal+sorteioChegada,sorteioChegada,contadorEventos);
            contadorEventos++;
            estados.add(e2);
            aleatorioAtual++;
        }   

    }

    public void transferFila1ToFila2(Fila aFila1, Fila aFila2){

        contabilizaTempo();

        aFila1.size--;

        if(aFila1.size >= aFila1.servers){
            double sorteioSaida = FormulaConversao(aFila1.minService, aFila1.maxService, aleatoriosUtilizados.get(aleatorioAtual));
            //System.out.println("sorteio: " + sorteioSaida + " - " + aFila1.minService + " - " + aFila1.maxService + " - " + aleatoriosUtilizados.get(aleatorioAtual));
            Estado e = new Estado("P12",tempoTotal+sorteioSaida,sorteioSaida,contadorEventos);
            contadorEventos++;
            estados.add(e);
            aleatorioAtual++;
        }

        if(aFila2.size < aFila2.capacity){

            aFila2.size++;

            if(aFila2.size <= aFila2.servers){
                double sorteioSaida = FormulaConversao(aFila2.minService, aFila2.maxService, aleatoriosUtilizados.get(aleatorioAtual));
                Estado e = new Estado("SA2",tempoTotal+sorteioSaida,sorteioSaida,contadorEventos);
                contadorEventos++;
                estados.add(e);
                aleatorioAtual++;
            }

        }else{
            aFila2.loss++;
        }

    }

    public void saidaFila2(Fila aFila){

        contabilizaTempo();


        aFila.size--;


        if(aFila.size >= aFila.servers){
            double sorteioSaida = FormulaConversao(aFila.minService, aFila.maxService, aleatoriosUtilizados.get(aleatorioAtual));
            Estado e = new Estado("SA2",tempoTotal+sorteioSaida,sorteioSaida,contadorEventos);
            contadorEventos++;
            estados.add(e);
            aleatorioAtual++;
        }

    }

    public void ExecutaAlgoritmo(){
        int perdas = fila1.loss+fila2.loss;

        try{
            //PrintWriter pw = new PrintWriter(new File(nomeArquivo));

            for (Fila fila : filaLst) {
                fila.output.print("Evento,F1,F2,Tempo,");
                for (int i = 0; i <= fila.capacity; i++) {
                    fila.output.print(i+",");
                }
                fila.output.println();
            } 

            while(aleatorioAtual < aleatoriosUtilizados.size()){
            
                if (tempoTotal == 0) {
                    for (Fila fila : filaLst) {
                        fila.output.printf("-,%d,%d,%.4f,", fila1.size,fila2.size, tempoTotal);
                        for (int i = 0; i < fila.tempos.length; i++) {
                            fila.output.printf("%.4f,", fila.tempos[i]);
                        }
                        fila.output.println();
                    }
                } else {
                    for (Fila fila : filaLst) {
                        fila.output.printf("(%d) %s,%d,%d,%.4f,", atual.numEvento, atual.tipo, fila1.size,fila2.size, tempoTotal);    
                        for (int i = 0; i < fila.tempos.length; i++) {
                            fila.output.printf("%.4f,", fila.tempos[i]);
                        }
                        fila.output.println();
                    }
                    
                }

                Estado aux = estados.get(0);
                
                for(int i = 1; i < estados.size(); i++){
                    if(aux.getTempo() > estados.get(i).getTempo()){
                        aux = estados.get(i);
                    }
                }

                atual = aux;
                tempoAnterior = tempoTotal;
                tempoTotal = atual.getTempo();
                estados.remove(aux);

                if(atual.getTipo().equals("CH1")){
                    chegada(fila1);
                }else if(atual.getTipo().equals("P12")){
                    transferFila1ToFila2(fila1, fila2);
                }else if(atual.getTipo().equals("SA2")){
                    saidaFila2(fila2);
                }
            
            }


            for (Fila fila : filaLst) {
                fila.output.printf("(%d) %s,%d,%d,%.4f,", atual.numEvento, atual.tipo, fila1.size,fila2.size, tempoTotal);
                for (int i = 0; i < fila.tempos.length; i++) {
                    fila.output.printf("%.4f,", fila.tempos[i]);
                }
                fila.output.println();
            
                fila.output.println("Numero de perdas: " + fila.loss);
            

                fila.output.println("Estado "+ fila.name+",Tempo,Probabilidade");
                for(int i = 0; i <= fila.capacity; i++){    
                    double porcentagem = fila.tempos[i] / tempoTotal * 100;       
                    fila.output.printf("%d", i);
                    fila.output.printf(",%.4f", fila.tempos[i]);
                    // System.out.println("porcentegem antes do arrendondamento: " + porcentagem);
                    // porcentagem = Math.round(porcentagem * 100.0) / 100.0;
                    // System.out.println("porcentegem depois do arrendondamento: " + porcentagem);
                    fila.output.printf(",%.2f", porcentagem);
                    fila.output.println("%");
                }

                fila.output.printf("Total,%.4f,100.00",tempoTotal);
                fila.output.println("%");

                fila.output.close();

            }


        }catch(Exception e){
            e.printStackTrace();
        }

        printResultadoFinal();

    }

    public void printResultadoFinal(){
        int perdas = fila1.loss+fila2.loss;
        System.out.println("\n[Resultado da simulacao]\n");
        System.out.printf("Fila 1: G/G/%d/%d\n", fila1.servers, fila1.capacity);
        System.out.printf("Fila 2: G/G/%d/%d\n", fila2.servers, fila2.capacity);
        System.out.printf("CH1: %.1f ... %.1f\n", fila1.minArrival, fila1.maxArrival);
        System.out.printf("P12: %.1f ... %.1f\n", fila1.minService, fila1.maxService);
        System.out.printf("SA2: %.1f ... %.1f\n", fila2.minService, fila2.maxService);
        System.out.println();
        System.out.println("Estados F1\tTempos F1\tProbabilidades F1");
        for(int i = 0; i <= fila1.capacity; i++){    
            double porcentagem = fila1.tempos[i] / tempoTotal * 100;       
            System.out.printf("%d", i);
            System.out.printf("\t\t%.4f", fila1.tempos[i]);
            System.out.printf("\t\t%.2f", porcentagem);
            System.out.println("%");
        }
        System.out.println();
        System.out.println("Estados F2\tTempos F2\tProbabilidades F2");
        for(int i = 0; i <= fila2.capacity; i++){    
            double porcentagem = fila2.tempos[i] / tempoTotal * 100;       
            System.out.printf("%d", i);
            System.out.printf("\t\t%.4f", fila2.tempos[i]);
            System.out.printf("\t\t%.2f", porcentagem);
            System.out.println("%");
        }

        System.out.println("\nNumero de perdas: " + perdas);
        System.out.printf("\nTempo total da simulacao: %.4f\n\n", tempoTotal);

    }

}