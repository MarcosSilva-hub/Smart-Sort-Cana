import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * Ponto de entrada (Main Class) - Agora com Benchmark de TEMPO e MEMÓRIA.
 */
public class App {
    
    // =======================================================
    // MÉTODOS AUXILIARES
    // =======================================================

    private static void shuffleArray(double[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            double temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
    
    private static void reverseArray(double[] array) {
        for (int i = 0; i < array.length / 2; i++) {
            double temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
    }
    
    private static void sortArray(double[] array) {
        Arrays.sort(array);
    }
    
    // =======================================================
    // EXECUÇÃO DOS TESTES (TEMPO E MEMÓRIA)
    // =======================================================

    /**
     * Executa o teste e retorna um array com [Tempo(ms), Memoria(Bytes)].
     */
    private static double[] executarTesteCompleto(double[] array, String tipoAlgo) {
        double[] arrayTeste = array.clone(); 
        
        // 1. Limpeza de Memória Pré-Teste (Tentativa de isolamento)
        System.gc();
        try { Thread.sleep(10); } catch (InterruptedException e) {} // Pausa para GC atuar
        
        // Medição Inicial
        long memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();
        
        // Execução
        switch (tipoAlgo) {
            case "MERGE":
                MergeSort.sort(arrayTeste, 0, arrayTeste.length - 1);
                break;
            case "QUICK":
                QuickSort.sort(arrayTeste, 0, arrayTeste.length - 1);
                break;
            case "SMART":
                SmartSort.sort(arrayTeste);
                break;
        }
        
        // Medição Final
        long endTime = System.nanoTime();
        long memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        double tempoMs = (endTime - startTime) / 1_000_000.0;
        
        // Cálculo de memória usada (pode ser negativo se o GC rodar no meio, então tratamos com Math.max)
        double memoriaBytes = Math.max(0, memoryAfter - memoryBefore);

        return new double[]{tempoMs, memoriaBytes};
    }

    /**
     * Retorna médias de [Tempo, Memoria].
     */
    private static double[] executarCenario(double[] arrayBase, double[] arraySorted, int repeticoes, String scenario) {
        double somaTempo = 0;
        double somaMemoria = 0;
        
        boolean isShuffled = scenario.contains("SHUFFLED");
        boolean isReversed = scenario.contains("REVERSED");
        
        String tipoAlgo = "";
        if (scenario.contains("MERGE")) tipoAlgo = "MERGE";
        else if (scenario.contains("QUICK")) tipoAlgo = "QUICK";
        else if (scenario.contains("SMART")) tipoAlgo = "SMART";
        
        for (int i = 0; i < repeticoes; i++) {
            double[] arrayTeste;

            if (isShuffled) {
                arrayTeste = arrayBase.clone();
                shuffleArray(arrayTeste); 
            } else if (isReversed) {
                arrayTeste = arraySorted.clone();
                reverseArray(arrayTeste);
            } else { 
                arrayTeste = arraySorted; 
            }
            
            double[] resultados = executarTesteCompleto(arrayTeste, tipoAlgo);
            somaTempo += resultados[0];
            somaMemoria += resultados[1];
        }
        
        return new double[]{somaTempo / repeticoes, somaMemoria / repeticoes};
    }
    
    // =======================================================
    // MAIN
    // =======================================================
    
    public static void main(String[] args) {
        String filePath = "CSV/winequality-white.csv";
        int columnIndex = 10;

        // Tenta carregar, se falhar cria dados fake para não quebrar o teste
        double[] data;
        try {
            data = DatasetLoader.loadColumn(filePath, columnIndex);
            if(data.length == 0) throw new Exception("Vazio");
        } catch (Exception e) {
            System.out.println("Aviso: CSV não encontrado ou vazio. Gerando dados aleatórios para teste.");
            data = new double[5000];
            Random r = new Random();
            for(int i=0; i<5000; i++) data[i] = r.nextDouble() * 100;
        }

        System.out.println("Total de registros base: " + data.length);

        int[] tamanhos = {500, 1500, 3000, 4000, 4898}; 
        int repeticoes = 50; // Reduzi levemente para não demorar tanto com o GC

        try (FileWriter writer = new FileWriter("CSV/resultados_completo.csv")) { 
            // Cabeçalho expandido: Tempo (T) e Memória (M)
            writer.write("tamanho," +
                         "melhor_merge_t,melhor_merge_m," +
                         "melhor_quick_t,melhor_quick_m," +
                         "melhor_smart_t,melhor_smart_m," +
                         
                         "medio_merge_t,medio_merge_m," +
                         "medio_quick_t,medio_quick_m," +
                         "medio_smart_t,medio_smart_m," +
                         
                         "pior_merge_t,pior_merge_m," +
                         "pior_quick_t,pior_quick_m," +
                         "pior_smart_t,pior_smart_m\n"); 

            for (int n : tamanhos) {
                if (n > data.length) continue;

                System.out.println("\n=== Testando N = " + n + " ===");
                
                double[] arrayBase = Arrays.copyOfRange(data, 0, n);
                double[] arraySorted = arrayBase.clone();
                sortArray(arraySorted);
                
                // --- 1. MELHOR CASO ---
                double[] bestMerge = executarCenario(arrayBase, arraySorted, repeticoes, "MELHOR_MERGE_SORTED");
                double[] bestQuick = executarCenario(arrayBase, arraySorted, repeticoes, "MELHOR_QUICK_SHUFFLED");
                double[] bestSmart = executarCenario(arrayBase, arraySorted, repeticoes, "MELHOR_SMART_SHUFFLED");
                
                // --- 2. CASO MÉDIO ---
                double[] avgMerge = executarCenario(arrayBase, arraySorted, repeticoes, "MEDIO_MERGE_SHUFFLED");
                double[] avgQuick = executarCenario(arrayBase, arraySorted, repeticoes, "MEDIO_QUICK_SHUFFLED");
                double[] avgSmart = executarCenario(arrayBase, arraySorted, repeticoes, "MEDIO_SMART_SHUFFLED");

                // --- 3. PIOR CASO ---
                double[] worstMerge = executarCenario(arrayBase, arraySorted, repeticoes, "PIOR_MERGE_REVERSED");
                double[] worstQuick = executarCenario(arrayBase, arraySorted, repeticoes, "PIOR_QUICK_REVERSED");
                double[] worstSmart = executarCenario(arrayBase, arraySorted, repeticoes, "PIOR_SMART_REVERSED");

                // Log simplificado
                System.out.printf("Pior Caso (Tempo): Merge=%.2fms | Quick=%.2fms | Smart=%.2fms%n", 
                    worstMerge[0], worstQuick[0], worstSmart[0]);
                System.out.printf("Pior Caso (Memória): Merge=%.0fB | Quick=%.0fB | Smart=%.0fB%n", 
                    worstMerge[1], worstQuick[1], worstSmart[1]);

                // Gravação no CSV (Muitas colunas!)
                writer.write(String.format(java.util.Locale.US, 
                    "%d," +
                    "%.4f,%.0f,%.4f,%.0f,%.4f,%.0f," + // Melhor
                    "%.4f,%.0f,%.4f,%.0f,%.4f,%.0f," + // Medio
                    "%.4f,%.0f,%.4f,%.0f,%.4f,%.0f%n", // Pior
                    n,
                    bestMerge[0], bestMerge[1], bestQuick[0], bestQuick[1], bestSmart[0], bestSmart[1],
                    avgMerge[0], avgMerge[1], avgQuick[0], avgQuick[1], avgSmart[0], avgSmart[1],
                    worstMerge[0], worstMerge[1], worstQuick[0], worstQuick[1], worstSmart[0], worstSmart[1]
                ));
            }
            System.out.println("\n✅ Benchmark (Tempo+Memória) salvo em CSV/resultados_completo.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}