/**
 * Implementação simplificada do algoritmo de ordenação Quick Sort.
 * Baseado no paradigma "Divisão e Conquista" (Divide and Conquer).
 * * * **Seleção de Pivot:** Utiliza o último elemento como pivot.
 * * **Importância para o Benchmark:** Essa escolha *não aleatória* garante que entradas 
 * reversamente ordenadas atinjam a complexidade $\text{O}(n^2)$ (Pior Caso),
 * o que é essencial para o nosso teste de comparação assintótica no projeto.
 * * * **Complexidade de Tempo:** * - Melhor Caso (e Médio): $\text{O}(n \log n)$
 * - Pior Caso: $\text{O}(n^2)$
 */
public class QuickSort {

    /**
     * Ponto de entrada recursivo da fase de **"Divisão"** do Quick Sort.
     * @param arr O array a ser ordenado.
     * @param low O índice inicial do subarray (limite inferior).
     * @param high O índice final do subarray (limite superior).
     */
    public static void sort(double[] arr, int low, int high) {
        // Caso base da recursão: continua enquanto o subarray for válido (low < high)
        if (low < high) {
            // Chama a Partição para reordenar o subarray e obter o índice final do pivot
            int pi = partition(arr, low, high); // pi é o índice de partição, arr[pi] está agora no lugar certo
            
            // 1. DIVIDE: Ordena recursivamente os elementos à esquerda do pivot
            sort(arr, low, pi - 1);
            // 2. DIVIDE: Ordena recursivamente os elementos à direita do pivot
            sort(arr, pi + 1, high);
        }
    }

    /**
     * Método auxiliar para realizar a troca de dois elementos no array.
     * @param arr O array.
     * @param i Índice do primeiro elemento.
     * @param j Índice do segundo elemento.
     */
    private static void swap(double[] arr, int i, int j) {
        double temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Fase de **"Conquista" (Partição de Lomuto)**.
     * Move elementos menores que o pivot para a esquerda e maiores para a direita.
     * O pivot é o último elemento do subarray.
     * @param arr O array contendo o subarray a ser particionado.
     * @param low O índice inicial do subarray.
     * @param high O índice final (que é também o pivot).
     * @return O índice onde o pivot está alocado após a partição (sua posição final).
     */
    private static int partition(double[] arr, int low, int high) {
        // Seleciona o pivot (o último elemento do subarray)
        double pivot = arr[high];
        // i é o índice do menor elemento encontrado, atuando como o limite da área "menor que o pivot"
        int i = (low - 1); 

        // Itera sobre todos os elementos do subarray, exceto o pivot (que é 'high')
        for (int j = low; j < high; j++) {
            // Se o elemento atual for menor ou igual ao pivot (condição para movê-lo para a esquerda)
            if (arr[j] <= pivot) {
                i++; // Incrementa o índice do menor elemento
                
                // Troca arr[i] e arr[j] para mover o elemento menor para a posição correta
                swap(arr, i, j);
            }
        }

        // Finalmente, troca o pivot (arr[high]) com o elemento arr[i+1].
        // Isso coloca o pivot na sua posição final correta.
        swap(arr, i + 1, high);

        // Retorna o índice final do pivot
        return i + 1;
    }
}