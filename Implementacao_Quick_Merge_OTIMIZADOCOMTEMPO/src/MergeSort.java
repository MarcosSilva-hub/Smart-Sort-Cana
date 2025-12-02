/**
 * Implementação do algoritmo de ordenação Merge Sort.
 * * **Complexidade de Tempo:** O(n log n) em todos os cenários (Melhor, Médio e Pior Caso).
 * É um algoritmo de ordenação estável baseado no paradigma de "Divisão e Conquista" (Divide and Conquer).
 */
public class MergeSort {

    /**
     * Ponto de entrada e fase de **"Divisão" (Divide)** da recursão.
     * Este método divide o array recursivamente até que os subarrays
     * atinjam o caso base (um elemento, que é considerado ordenado).
     * @param arr O array a ser ordenado.
     * @param left O índice inicial do subarray a ser processado.
     * @param right O índice final do subarray a ser processado.
     */
    public static void sort(double[] arr, int left, int right) {
        // Base case: Se left < right, o subarray tem pelo menos dois elementos para dividir
        if (left < right) {
            // Encontra o ponto médio (evitando overflow de inteiros)
            int middle = left + (right - left) / 2;
            
            // 1. DIVIDE: Chama recursivamente para ordenar a primeira metade (left...middle)
            sort(arr, left, middle);
            // 2. DIVIDE: Chama recursivamente para ordenar a segunda metade (middle+1...right)
            sort(arr, middle + 1, right);

            // 3. CONQUER: Mescla as duas metades ordenadas
            merge(arr, left, middle, right);
        }
    }

    /**
     * Mescla (Combina) dois subarrays já ordenados em um único subarray ordenado.
     * Esta é a fase de **"Conquista" (Conquer)** do algoritmo, com complexidade de tempo O(n).
     * O primeiro subarray é arr[left..middle].
     * O segundo subarray é arr[middle+1..right].
     */
    private static void merge(double[] arr, int left, int middle, int right) {
        // Calcula os tamanhos dos dois subarrays que serão mesclados
        int n1 = middle - left + 1; // Tamanho do subarray esquerdo (L)
        int n2 = right - middle;    // Tamanho do subarray direito (R)

        // Cria arrays temporários para armazenar os elementos dos subarrays
        double[] L = new double[n1];
        double[] R = new double[n2];

        // Copia os dados do array principal para o array temporário L[]
        for (int i = 0; i < n1; ++i)
            L[i] = arr[left + i];
        // Copia os dados do array principal para o array temporário R[]
        for (int j = 0; j < n2; ++j)
            R[j] = arr[middle + 1 + j];

        // Índices de controle para a mesclagem:
        int i = 0; // Índice de L[]
        int j = 0; // Índice de R[]
        int k = left; // Índice do array mesclado (principal arr[])

        // Mescla os arrays temporários de volta para arr[left..right] de forma ordenada
        while (i < n1 && j < n2) {
            // Compara os elementos: o menor elemento vai para o array principal
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++; // Avança no array esquerdo
            } else {
                arr[k] = R[j];
                j++; // Avança no array direito
            }
            k++; // Avança no array principal
        }

        // Se o array esquerdo L[] ainda tiver elementos, copia os restantes
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        // Se o array direito R[] ainda tiver elementos, copia os restantes
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }
}