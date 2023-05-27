import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException{

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
