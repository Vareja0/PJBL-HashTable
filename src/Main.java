import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;

public class Main {
    static final long SEED = 42;
    static final int[] TAMANHOS_TABELA = {10007, 100003, 1000003};
    static final int[] TAMANHOS_DADOS = {10000, 100000, 1000000};

    public static void main(String[] args) throws IOException {
        Random rand = new Random(43);
        System.out.println("=== TESTE DE TABELAS HASH ===\n");

        int[] dados1 = new int[TAMANHOS_DADOS[0]];
        int[] dados2 = new int[TAMANHOS_DADOS[1]];
        int[] dados3 = new int[TAMANHOS_DADOS[2]];

        // Inicializa os FileWriters para cada arquivo resultado
        FileWriter csv10007 = new FileWriter("resultado10007.csv");
        FileWriter csv100003 = new FileWriter("resultado100003.csv");
        FileWriter csv1000003 = new FileWriter("resultado1000003.csv");

        // Escreve os headers
        String header = "Metodo,TamTabela,TamDados,TempoInsercao(ms),TempoBusca(ms),Colisoes,Lista1,Lista2,Lista3,GapMin,GapMax,GapMedia\n";
        csv10007.write(header);
        csv100003.write(header);
        csv1000003.write(header);

        int[][] dadosLista = {dados1, dados2, dados3};

        // Popula os arrays que serao usados com numeros aleatorios
        for (int i = 0; i < TAMANHOS_DADOS[0]; i++) {
            dados1[i] = rand.nextInt(1, 1000000000);
        }
        for (int i = 0; i < TAMANHOS_DADOS[1]; i++) {
            dados2[i] = rand.nextInt(1, 1000000000);
        }
        for (int i = 0; i < TAMANHOS_DADOS[2]; i++) {
            dados3[i] = rand.nextInt(1, 1000000000);
        }

        for (int tamTabela : TAMANHOS_TABELA) {
            // Seleciona o FileWriter a partir do tamanho da tabela
            FileWriter csv = tamTabela == 10007 ? csv10007 : tamTabela == 100003 ? csv100003 : csv1000003;

            for (int i = 0; i < 3; i++) {
                int tamDados = TAMANHOS_DADOS[i];
                int[] dataset = dadosLista[i];

                System.out.println("\nTabela: " + tamTabela + " | Dados: " + tamDados);

                // Hash Encadeado
                System.out.println("  Testando Encadeamento...");
                HashEncadeamento enc = new HashEncadeamento(tamTabela);

                long inicio = System.nanoTime();
                for (int j = 0; j < tamDados; j++) {
                    enc.inserir(dataset[j]);
                }
                long tempoIns = System.nanoTime() - inicio;


                inicio = System.nanoTime();
                for (int j = 0; j < tamDados; j++) {
                    enc.buscar(dataset[j]);
                }
                long tempoBus = System.nanoTime() - inicio;

                int[] listas = enc.maioresListas();
                int[] gaps = enc.calcularGaps();


                csv.write(String.format(Locale.US, "Encadeamento,%d,%d,%f,%f,%d,%d,%d,%d,%d,%d,%d\n",
                        tamTabela, tamDados, tempoIns / 1000000.f, tempoBus / 1000000.f, enc.colisoes,
                        listas[0], listas[1], listas[2], gaps[0], gaps[1], gaps[2]));

                // Hash Linear
                System.out.println("  Testando Linear Probing...");
                HashLinear lin = new HashLinear(tamTabela);

                inicio = System.nanoTime();
                for (int j = 0; j < tamDados; j++) {
                    lin.inserir(dataset[j]);
                }
                tempoIns = System.nanoTime() - inicio;

                inicio = System.nanoTime();
                for (int j = 0; j < tamDados; j++) {
                    lin.buscar(dataset[j]);
                }
                tempoBus = System.nanoTime() - inicio;

                gaps = lin.calcularGaps();

                csv.write(String.format(Locale.US, "LinearProbing,%d,%d,%f,%f,%d,0,0,0,%d,%d,%d\n",
                        tamTabela, tamDados, tempoIns / 1000000.f, tempoBus / 1000000.f, lin.colisoes,
                        gaps[0], gaps[1], gaps[2]));

                // Hash Duplo
                System.out.println("  Testando Double Hashing...");
                HashDuplo dup = new HashDuplo(tamTabela);

                inicio = System.nanoTime();
                for (int j = 0; j < tamDados; j++) {
                    dup.inserir(dataset[j]);
                }
                tempoIns = System.nanoTime() - inicio;

                inicio = System.nanoTime();
                for (int j = 0; j < tamDados; j++) {
                    dup.buscar(dataset[j]);
                }
                tempoBus = System.nanoTime() - inicio;

                gaps = dup.calcularGaps();

                csv.write(String.format(Locale.US, "DoubleHashing,%d,%d,%f,%f,%d,0,0,0,%d,%d,%d\n",
                        tamTabela, tamDados, tempoIns / 1000000.f, tempoBus / 1000000.f, dup.colisoes,
                        gaps[0], gaps[1], gaps[2]));
            }
        }

        // Fecha os FileWriters
        csv10007.close();
        csv100003.close();
        csv1000003.close();
        System.out.println("\n=== CONCLUÍDO! Resultados em resultado10007.csv, resultado100003.csv. resultado1000003.csv ===");
    }
}