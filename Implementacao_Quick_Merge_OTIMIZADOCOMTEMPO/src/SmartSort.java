/**
 * SmartSort (Híbrido - Introsort Simplificado).
 * * ESTRATÉGIA:
 * 1. Inicia com a lógica do Quick Sort (rápido e eficiente em memória).
 * 2. Monitora a profundidade da recursão.
 * 3. Se a profundidade exceder um limite seguro (2 * log2(n)), detecta que 
 * o pivô está ruim (caminhando para O(n²)) e troca para Merge Sort.
 * * RESULTADO:
 * - Mantém a velocidade do Quick Sort no caso médio.
 * - Garante a estabilidade O(n log n) do Merge Sort no pior caso.
 */
public class SmartSort {

    /**
     * Ponto de entrada do algoritmo.
     * Calcula o limite de profundidade e inicia a recursão.
     */
    public static void sort(double[] arr) {
        // Calcula log2(n)
        int depthLimit = (int) (2 * Math.floor(Math.log(arr.length) / Math.log(2)));

        smartSortRecursive(arr, 0, arr.length - 1, 0, depthLimit);
    }

    /**
     * Método recursivo que decide qual algoritmo usar baseando-se na profundidade.
     */
    private static void smartSortRecursive(double[] arr, int low, int high, int depth, int depthLimit) {
        if (low < high) {
            // 1. A OTIMIZAÇÃO "INTELIGENTE":
            // Se a recursão for muito profunda, aborta o Quick Sort e usa Merge Sort
            // neste subarray para garantir O(n log n).
            if (depth > depthLimit) {
                MergeSort.sort(arr, low, high);
                return;
            }

            // 2. Se estiver dentro do limite, segue com a partição do Quick Sort
            int pi = partition(arr, low, high);

            // Chamadas recursivas aumentando a profundidade
            smartSortRecursive(arr, low, pi - 1, depth + 1, depthLimit);
            smartSortRecursive(arr, pi + 1, high, depth + 1, depthLimit);
        }
    }

    // --- Métodos Auxiliares do Quick Sort (Partição de Lomuto) ---

    private static int partition(double[] arr, int low, int high) {
        double pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static void swap(double[] arr, int i, int j) {
        double temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}