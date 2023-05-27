import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;



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
            return "Tipo: "+tipo+"; Tempo: "+tempo+"; Sorteio: "+sorteio;
        }
    }


    public Fila fila1;
    public Fila fila2;

    public double tempoChegadaMin1;// = 2.0;
    public double tempoChegadaMax1;// = 3.0;
    public double tempoAtendimentoMin1;// = 2.0;
    public double tempoAtendimentoMax1;// = 5.0;  
    
    public double tempoAtendimentoMin2;// = 3.0;
    public double tempoAtendimentoMax2;// = 5.0;  


    public int numeroServidores1;// = 2;
    public int capacidadeFila1;// = 3;
    public int numeroServidores2;// = 1;
    public int capacidadeFila2;// = 3;

    public int tamanhoFila1 = 0;
    public int tamanhoFila2 = 0;

    public double tempoTotal = 0.0;
    public double tempoAnterior = 0.0;

    public int numPerdas = 0;

    public List<Double> aleatoriosUtilizados = new ArrayList<Double>();
    public int aleatorioAtual = 0;

    public List<Estado> estados = new ArrayList<>();
    public Estado atual = null;

    public double[] estadoFila1;
    public double[] estadoFila2;

    public int contadorEventos = 1;

    private ArrayList<Fila> filaLst = new ArrayList<>();

    public Simulacao(double tempoChegadaMin1, double tempoChegadaMax1, 
        double tempoAtendimentoMin1, double tempoAtendimentoMax1,
        double tempoAtendimentoMin2, double tempoAtendimentoMax2, 
        int numeroServidores1, int numeroServidores2,
        int capacidadeFila1,int capacidadeFila2,
        double tempoInicial, double x0, double a, double m, double c, double n) throws FileNotFoundException{
        
        this.fila1 = new Fila("Fila1", numeroServidores1, capacidadeFila1, tempoChegadaMin1, tempoChegadaMax1, tempoAtendimentoMin1, tempoAtendimentoMax1);
        this.fila2 = new Fila("Fila2", numeroServidores2, capacidadeFila2, tempoAtendimentoMin2, tempoAtendimentoMax2);
        
        filaLst.add(fila1);
        filaLst.add(fila2);

        aleatoriosUtilizados = new GeradorNumeros(x0,a,m,c,n).getNumeros();

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

                double sorteioSaida = FormulaConversao(fila.maxService, fila.maxService, aleatoriosUtilizados.get(aleatorioAtual));
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

    public void ExecutaAlgoritmo(String nomeArquivo){

        try{
            PrintWriter pw = new PrintWriter(new File(nomeArquivo));

            pw.print("Evento;FILA1;FILA2;Tempo;");
            for (int i = 0; i <= capacidadeFila1; i++) {
                pw.print(i+";");
            }
            pw.println();

            for (int i = 0; i <= capacidadeFila2; i++) {
                pw.print(i+";");
            }

            pw.println();

            while(aleatorioAtual < aleatoriosUtilizados.size()){
            
                if (tempoTotal == 0) {
                    pw.printf("-;%d;%d;%.4f;", tamanhoFila1,tamanhoFila2, tempoTotal);
                } else {
                    pw.printf("(%d) %s;%d;%d;%.4f;", atual.numEvento, atual.tipo, tamanhoFila1,tamanhoFila2, tempoTotal);
                }

                for(int i = 0; i <= capacidadeFila1; i++){

                    pw.printf("%.4f;",estadoFila1[i]);

                }
                pw.println();

                for(int i = 0; i <= capacidadeFila2; i++){

                    pw.printf("%.4f;",estadoFila2[i]);

                }
                pw.println();

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

            pw.printf("(%d) %s;%d;%d;%.4f;", atual.numEvento, atual.tipo, tamanhoFila1,tamanhoFila2, tempoTotal);
            for(int i = 0; i <= capacidadeFila1; i++){            
                pw.printf("%.4f;",estadoFila1[i]);
            }
            pw.println();
            for(int i = 0; i <= capacidadeFila2; i++){            
                pw.printf("%.4f;",estadoFila2[i]);
            }
            pw.println();

            pw.println("Numero de perdas: " + numPerdas);
            

            pw.close();

        }catch(Exception e){
            e.printStackTrace();
        }

        printResultadoFinal();

    }

    public void printResultadoFinal(){
        System.out.println("\n[Resultado da simulacao]\n");
        System.out.printf("Fila 1: G/G/%d/%d\n", numeroServidores1, capacidadeFila1);
        System.out.printf("Fila 2: G/G/%d/%d\n", numeroServidores2, capacidadeFila2);
        System.out.printf("CH1: %.1f ... %.1f\n", tempoChegadaMin1, tempoChegadaMax1);
        System.out.printf("P12: %.1f ... %.1f\n", tempoAtendimentoMin1, tempoAtendimentoMax1);
        System.out.printf("SA2: %.1f ... %.1f\n", tempoAtendimentoMin2, tempoAtendimentoMax2);
        System.out.println("Estado 1\t\tTempo 1\t\tProbabilidade 1");
        for(int i = 0; i <= capacidadeFila1; i++){    
            double porcentagem = estadoFila1[i] / tempoTotal * 100;       
            System.out.printf("%d", i);
            System.out.printf("\t\t%.4f", estadoFila1[i]);
            System.out.printf("\t\t%.2f", porcentagem);
            System.out.println("%");
        }
        System.out.println("Estado 2\t\tTempo 2\t\tProbabilidade 2");
        for(int i = 0; i <= capacidadeFila2; i++){    
            double porcentagem = estadoFila2[i] / tempoTotal * 100;       
            System.out.printf("%d", i);
            System.out.printf("\t\t%.4f", estadoFila2[i]);
            System.out.printf("\t\t%.2f", porcentagem);
            System.out.println("%");
        }

        System.out.println("\nNumero de perdas: " + numPerdas);
        System.out.printf("\nTempo total da simulacao: %.4f\n\n", tempoTotal);

    }

}