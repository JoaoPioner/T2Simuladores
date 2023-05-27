import java.io.File;
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

    public Simulacao(double tempoChegadaMin1, double tempoChegadaMax1, 
        double tempoAtendimentoMin1, double tempoAtendimentoMax1,
        double tempoAtendimentoMin2, double tempoAtendimentoMax2, 
        int numeroServidores1, int numeroServidores2,
        int capacidadeFila1,int capacidadeFila2,
        double tempoInicial, double x0, double a, double m, double c, double n){
        this.fila1 = new Fila("Fila1", capacidadeFila1,numeroServidores1,
            tempoChegadaMin1,tempoChegadaMax1,
            tempoAtendimentoMin1,tempoAtendimentoMax1);
        this.fila2 = new Fila("Fila2", capacidadeFila2,numeroServidores2,
            tempoAtendimentoMin1,tempoAtendimentoMax1,
            tempoAtendimentoMin2,tempoAtendimentoMax2);

        aleatoriosUtilizados = new GeradorNumeros(x0,a,m,c,n).getNumeros();

        tempoTotal = 0.0000f;
        tempoAnterior = 0.0000f;
        aleatorioAtual = 0;
        Estado e = new Estado("CH1", tempoInicial,0.0,contadorEventos);
        contadorEventos++;
        estados.add(e);
        atual = e;
    }


    public Simulacao(
        double tempoChegadaMin1, double tempoChegadaMax1, 
        double tempoAtendimentoMin1, double tempoAtendimentoMax1,
        double tempoAtendimentoMin2, double tempoAtendimentoMax2, 
        int numeroServidores1, int numeroServidores2,
        int capacidadeFila1,int capacidadeFila2,
        double tempoInicial, double x0, double a, double m, double c, double n){
            
        this.tempoChegadaMin1 = tempoChegadaMin1;
        this.tempoChegadaMax1 = tempoChegadaMax1;
        this.tempoAtendimentoMin1 = tempoAtendimentoMin1;
        this.tempoAtendimentoMax1 = tempoAtendimentoMax1;
        this.tempoAtendimentoMin2 = tempoAtendimentoMin2;
        this.tempoAtendimentoMax2 = tempoAtendimentoMax2;
        this.numeroServidores1 = numeroServidores1;
        this.numeroServidores2 = numeroServidores2;
        this.capacidadeFila1 = capacidadeFila1;
        this.capacidadeFila2 = capacidadeFila2;

        aleatoriosUtilizados = new GeradorNumeros(x0,a,m,c,n).getNumeros();

        estadoFila1 = new double[capacidadeFila1+1];
        estadoFila2 = new double[capacidadeFila2+1];

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

    public double contabilizaTempo(double atual, double anterior){
        return atual - anterior;
    }

    public void chegadaFila1(){

        double deltaT = tempoTotal-tempoAnterior;
        
        estadoFila1[tamanhoFila1]+=deltaT;
        estadoFila2[tamanhoFila2]+=deltaT;

        if(tamanhoFila1 < capacidadeFila1){

            tamanhoFila1++;

            if(tamanhoFila1 <= numeroServidores1){

                double sorteioSaida = FormulaConversao(tempoAtendimentoMin1, tempoAtendimentoMax1, aleatoriosUtilizados.get(aleatorioAtual));
                Estado e = new Estado("P12",tempoTotal+sorteioSaida,sorteioSaida,contadorEventos);
                contadorEventos++;
                estados.add(e);
                aleatorioAtual++;

            }

        }else {
            numPerdas++;
        } 

        if (aleatorioAtual < aleatoriosUtilizados.size()) {
            double sorteioChegada = FormulaConversao(tempoChegadaMin1, tempoChegadaMax1, aleatoriosUtilizados.get(aleatorioAtual));
            Estado e2 = new Estado("CH1",tempoTotal+sorteioChegada,sorteioChegada,contadorEventos);
            contadorEventos++;
            estados.add(e2);
            aleatorioAtual++;
        }   

    }

    public void transferFila1ToFila2(){

        double deltaT = tempoTotal-tempoAnterior;
        
        estadoFila1[tamanhoFila1]+=deltaT;
        estadoFila2[tamanhoFila2]+=deltaT;

        tamanhoFila1--;

        if(tamanhoFila1 >= numeroServidores1){
            double sorteioSaida = FormulaConversao(tempoAtendimentoMin1, tempoAtendimentoMax1, aleatoriosUtilizados.get(aleatorioAtual));
            Estado e = new Estado("P12",tempoTotal+sorteioSaida,sorteioSaida,contadorEventos);
            contadorEventos++;
            estados.add(e);
            aleatorioAtual++;
        }

        if(tamanhoFila2 < capacidadeFila2){

            tamanhoFila2++;

            if(tamanhoFila2 <= numeroServidores2){
                double sorteioSaida = FormulaConversao(tempoAtendimentoMin2, tempoAtendimentoMax2, aleatoriosUtilizados.get(aleatorioAtual));
                Estado e = new Estado("SA2",tempoTotal+sorteioSaida,sorteioSaida,contadorEventos);
                contadorEventos++;
                estados.add(e);
                aleatorioAtual++;
            }

        }else{
            numPerdas++;
        }

    }

    public void saidaFila2(){

        double deltaT = tempoTotal-tempoAnterior;
        
        estadoFila1[tamanhoFila1]+=deltaT;
        estadoFila2[tamanhoFila2]+=deltaT;


        tamanhoFila2--;


        if(tamanhoFila2 >= numeroServidores2){
            double sorteioSaida = FormulaConversao(tempoAtendimentoMin2, tempoAtendimentoMax2, aleatoriosUtilizados.get(aleatorioAtual));
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
                    chegadaFila1();
                }else if(atual.getTipo().equals("P12")){
                    transferFila1ToFila2();
                }else if(atual.getTipo().equals("SA2")){
                    saidaFila2();
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

    public static void main(String[] args){

        Fila fila1 = new Fila("fila1", 2, 3, 2.0, 3.0, 2.0, 5.0);
        Fila fila2 = new Fila("fila2", 1, 3, 3.0, 5.0);


        double tempoChegadaMin = 2.0;
        double tempoChegadaMax = 3.0;
        double tempoAtendimentoMin = 2.0;
        double tempoAtendimentoMax = 5.0;
        double tempoAtendimentoMin2 = 3.0;
        double tempoAtendimentoMax2 = 5.0;

        double tempoInicial = 2.5;

        int numeroServidores1 = 2;
        int capacidadeFila1 = 3;
        int numeroServidores2 = 1;
        int capacidadeFila2 = 3;


        Simulacao cenario0_simulacao0 = new Simulacao(tempoChegadaMin, tempoChegadaMax, 
                                            tempoAtendimentoMin, tempoAtendimentoMax,
                                            tempoAtendimentoMin2, tempoAtendimentoMax2, 
                                            numeroServidores1,numeroServidores2, 
                                            capacidadeFila1,capacidadeFila2, 
                                            tempoInicial, 20,21,5000000011L,227,100000);
        cenario0_simulacao0.ExecutaAlgoritmo("resultado.csv");

        // Primeira simulacao:

        /* Simulacao cenario1_simulacao1 = new Simulacao(tempoChegadaMin, tempoChegadaMax, tempoAtendimentoMin, tempoAtendimentoMax, 
                                            numeroServidores, capacidadeFila, tempoInicial,20,21,5000000011L,227,100000);
        cenario1_simulacao1.ExecutaAlgoritmo("G-G-1-5-simulacao1.csv");

        Simulacao cenario1_simulacao2 = new Simulacao(tempoChegadaMin, tempoChegadaMax, tempoAtendimentoMin, tempoAtendimentoMax, 
                                            numeroServidores, capacidadeFila, tempoInicial,30,21,5000000011L,227,100000);
        cenario1_simulacao2.ExecutaAlgoritmo("G-G-1-5-simulacao2.csv");

        Simulacao cenario1_simulacao3 = new Simulacao(tempoChegadaMin, tempoChegadaMax, tempoAtendimentoMin, tempoAtendimentoMax, 
                                            numeroServidores, capacidadeFila, tempoInicial,40,21,5000000011L,227,100000);
        cenario1_simulacao3.ExecutaAlgoritmo("G-G-1-5-simulacao3.csv");

        Simulacao cenario1_simulacao4 = new Simulacao(tempoChegadaMin, tempoChegadaMax, tempoAtendimentoMin, tempoAtendimentoMax, 
                                            numeroServidores, capacidadeFila, tempoInicial,50,21,5000000011L,227,100000);
        cenario1_simulacao4.ExecutaAlgoritmo("G-G-1-5-simulacao4.csv");

        Simulacao cenario1_simulacao5 = new Simulacao(tempoChegadaMin, tempoChegadaMax, tempoAtendimentoMin, tempoAtendimentoMax, 
                                            numeroServidores, capacidadeFila, tempoInicial,60,21,5000000011L,227,100000);
        cenario1_simulacao5.ExecutaAlgoritmo("G-G-1-5-simulacao5.csv"); */



        // Segunda simulação:

       /*  numeroServidores = 2;

        Simulacao cenario2_simulacao1 = new Simulacao(tempoChegadaMin, tempoChegadaMax, tempoAtendimentoMin, tempoAtendimentoMax, 
                                                     numeroServidores, capacidadeFila, tempoInicial, 20,21,5000000011L,227,100000);
        cenario2_simulacao1.ExecutaAlgoritmo("G-G-2-5-simulacao1.csv");

        Simulacao cenario2_simulacao2 = new Simulacao(tempoChegadaMin, tempoChegadaMax, tempoAtendimentoMin, tempoAtendimentoMax, 
                                                     numeroServidores, capacidadeFila, tempoInicial,30,21,5000000011L,227,100000);
        cenario2_simulacao2.ExecutaAlgoritmo("G-G-2-5-simulacao2.csv");

        Simulacao cenario2_simulacao3 = new Simulacao(tempoChegadaMin, tempoChegadaMax, tempoAtendimentoMin, tempoAtendimentoMax, 
                                                    numeroServidores, capacidadeFila, tempoInicial,40,21,5000000011L,227,100000);
        cenario2_simulacao3.ExecutaAlgoritmo("G-G-2-5-simulacao3.csv");

        Simulacao cenario2_simulacao4 = new Simulacao(tempoChegadaMin, tempoChegadaMax, tempoAtendimentoMin, tempoAtendimentoMax, 
                                                    numeroServidores, capacidadeFila, tempoInicial,50,21,5000000011L,227,100000);
        cenario2_simulacao4.ExecutaAlgoritmo("G-G-2-5-simulacao4.csv");

        Simulacao cenario2_simulacao5 = new Simulacao(tempoChegadaMin, tempoChegadaMax, tempoAtendimentoMin, tempoAtendimentoMax, 
                                                    numeroServidores, capacidadeFila, tempoInicial,60,21,5000000011L,227,100000);
        cenario2_simulacao5.ExecutaAlgoritmo("G-G-2-5-simulacao5.csv"); */
       
    }

}