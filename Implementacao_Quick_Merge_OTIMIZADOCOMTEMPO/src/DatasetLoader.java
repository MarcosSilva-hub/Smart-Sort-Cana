import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária responsável por carregar dados de um arquivo CSV.
 * É essencial para fornecer os vetores de entrada (datasets) para os algoritmos
 * de ordenação (Merge Sort e Quick Sort) no projeto de análise.
 */
public class DatasetLoader {

    /**
     * Carrega todos os valores de uma coluna específica de um arquivo CSV.
     * O separador esperado para o CSV é ponto e vírgula (`;`).
     * @param filePath O caminho completo do arquivo CSV.
     * @param columnIndex O índice da coluna (base zero) a ser extraída.
     * @return Um array de `double` contendo todos os valores da coluna.
     */
    public static double[] loadColumn(String filePath, int columnIndex) {
        // Inicializa uma lista dinâmica para armazenar os valores lidos da coluna
        List<Double> values = new ArrayList<>();
        
        // Uso de try-with-resources para garantir que o BufferedReader seja fechado
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            // Lê e ignora a primeira linha, assumida como o cabeçalho (header) do CSV
            br.readLine(); 
            
            // Loop de leitura: continua enquanto houver linhas no arquivo
            while ((line = br.readLine()) != null) {
                // Divide a linha usando ponto e vírgula (;) como delimitador
                String[] parts = line.split(";");
                
                // Validação: verifica se a linha tem o número de colunas necessário
                if (parts.length > columnIndex) {
                    try {
                        // Tenta converter o valor da coluna desejada para double
                        // e adiciona à lista de valores
                        values.add(Double.parseDouble(parts[columnIndex]));
                    } catch (NumberFormatException e) {
                        // Tratamento de erro caso o valor não seja um número (pode ser um erro nos dados)
                        System.err.println("Erro de formato numérico na linha: " + line);
                        // Continua para a próxima linha
                    }
                }
            }
        } catch (Exception e) {
            // Tratamento genérico de I/O, como arquivo não encontrado ou erro de leitura
            System.err.println("Erro ao carregar o arquivo: " + filePath);
            e.printStackTrace();
        }

        // Converte a lista dinâmica (ArrayList) para um array primitivo de double,
        // que é o formato esperado pelos algoritmos de ordenação
        double[] result = new double[values.size()];
        for (int i = 0; i < values.size(); i++)
            result[i] = values.get(i);
            
        return result;
    }
}