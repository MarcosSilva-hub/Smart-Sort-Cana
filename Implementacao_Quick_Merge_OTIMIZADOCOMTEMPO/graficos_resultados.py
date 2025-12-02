import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import os

# --- Configurações ---
CSV_FILE = 'CSV/resultados_completo.csv' 
OUTPUT_DIR = 'graficos_otimizados'
os.makedirs(OUTPUT_DIR, exist_ok=True)

print(f"Lendo dados de: {CSV_FILE}")

try:
    df = pd.read_csv(CSV_FILE)

    # Conversão de Bytes para KB para facilitar leitura dos gráficos de memória
    cols_memoria = [c for c in df.columns if c.endswith('_m')]
    for col in cols_memoria:
        df[col] = df[col] / 1024.0  # Converte para KB

    sns.set_style("whitegrid")
    
    # Cores padronizadas
    COLOR_MERGE = '#28a745'  # Verde (Merge - Seguro)
    COLOR_QUICK = '#dc3545'  # Vermelho (Quick - Risco)
    COLOR_SMART = '#007bff'  # Azul (Smart - Otimizado)

    # =======================================================
    # GRÁFICO 1: ANÁLISE DE TEMPO - MELHOR CASO
    # =======================================================
    plt.figure(figsize=(10, 6))
    plt.plot(df['tamanho'], df['melhor_merge_t'], marker='o', label='Merge Sort', color=COLOR_MERGE)
    plt.plot(df['tamanho'], df['melhor_quick_t'], marker='x', label='Quick Sort', color=COLOR_QUICK, linestyle='--')
    plt.plot(df['tamanho'], df['melhor_smart_t'], marker='D', label='Smart Sort', color=COLOR_SMART, linestyle='-.')
    
    plt.title('1. MELHOR CASO (Tempo): Array Ordenado/Aleatório\nSmart Sort acompanha o Quick Sort?')
    plt.xlabel('Tamanho da Entrada (N)')
    plt.ylabel('Tempo (ms)')
    plt.legend()
    plt.grid(True, linestyle=':', alpha=0.7)
    plt.tight_layout()
    plt.savefig(os.path.join(OUTPUT_DIR, '01_tempo_melhor_caso.png'))
    print("Salvo: 01_tempo_melhor_caso.png")

    # =======================================================
    # GRÁFICO 2: ANÁLISE DE TEMPO - CASO MÉDIO
    # =======================================================
    plt.figure(figsize=(10, 6))
    plt.plot(df['tamanho'], df['medio_merge_t'], marker='o', label='Merge Sort', color=COLOR_MERGE)
    plt.plot(df['tamanho'], df['medio_quick_t'], marker='x', label='Quick Sort', color=COLOR_QUICK, linestyle='--')
    plt.plot(df['tamanho'], df['medio_smart_t'], marker='D', label='Smart Sort', color=COLOR_SMART, linestyle='-.')

    plt.title('2. CASO MÉDIO (Tempo): Input Aleatório\nEficiência geral dos algoritmos')
    plt.xlabel('Tamanho da Entrada (N)')
    plt.ylabel('Tempo (ms)')
    plt.legend()
    plt.grid(True, linestyle=':', alpha=0.7)
    plt.tight_layout()
    plt.savefig(os.path.join(OUTPUT_DIR, '02_tempo_caso_medio.png'))
    print("Salvo: 02_tempo_caso_medio.png")

    # =======================================================
    # GRÁFICO 3: A PROVA DA OTIMIZAÇÃO (TEMPO PIOR CASO)
    # =======================================================
    plt.figure(figsize=(10, 6))
    plt.plot(df['tamanho'], df['pior_merge_t'], marker='o', label='Merge Sort', color=COLOR_MERGE, alpha=0.6)
    plt.plot(df['tamanho'], df['pior_quick_t'], marker='x', label='Quick Sort (O(n²))', color=COLOR_QUICK, linestyle='--')
    plt.plot(df['tamanho'], df['pior_smart_t'], marker='D', label='Smart Sort (Otimizado)', color=COLOR_SMART, linewidth=3)

    plt.title('3. PIOR CASO (Tempo): A Prova da Otimização\nNote como o Smart Sort evita o pico exponencial')
    plt.xlabel('Tamanho da Entrada (N)')
    plt.ylabel('Tempo (ms)')
    
    # Anotação visual
    ultimo_n = df['tamanho'].iloc[-1]
    diff = df['pior_quick_t'].iloc[-1] - df['pior_smart_t'].iloc[-1]
    plt.annotate(f'Redução de ~{diff:.1f}ms', 
                 xy=(ultimo_n, df['pior_smart_t'].iloc[-1]), 
                 xytext=(ultimo_n - 1500, df['pior_quick_t'].iloc[-1] - 0.5),
                 arrowprops=dict(facecolor='black', shrink=0.05))

    plt.legend()
    plt.grid(True, linestyle=':', alpha=0.7)
    plt.tight_layout()
    plt.savefig(os.path.join(OUTPUT_DIR, '03_tempo_pior_caso_prova.png'))
    print("Salvo: 03_tempo_pior_caso_prova.png")

    # =======================================================
    # GRÁFICO 4: ESTABILIDADE INTERNA DO SMART SORT
    # =======================================================
    plt.figure(figsize=(10, 6))
    plt.plot(df['tamanho'], df['melhor_smart_t'], marker='o', label='Melhor Caso', color='#28a745')
    plt.plot(df['tamanho'], df['medio_smart_t'], marker='s', label='Caso Médio', color='#ffc107')
    plt.plot(df['tamanho'], df['pior_smart_t'], marker='^', label='Pior Caso', color='#dc3545')

    plt.title('4. SMART SORT (Interno): Consistência\nEle mantém performance alta em todos os cenários?')
    plt.xlabel('Tamanho da Entrada (N)')
    plt.ylabel('Tempo (ms)')
    plt.legend(title="Cenário")
    plt.grid(True, linestyle=':', alpha=0.7)
    plt.tight_layout()
    plt.savefig(os.path.join(OUTPUT_DIR, '04_estabilidade_smart_sort.png'))
    print("Salvo: 04_estabilidade_smart_sort.png")

    # =======================================================
    # GRÁFICO 5: TEMPO VS ESPAÇO (VISÃO HOLÍSTICA PIOR CASO)
    # =======================================================
    # Gráfico duplo (lado a lado)
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 6))
    
    # Subplot Tempo
    ax1.plot(df['tamanho'], df['pior_merge_t'], marker='o', label='Merge', color=COLOR_MERGE)
    ax1.plot(df['tamanho'], df['pior_quick_t'], marker='x', label='Quick', color=COLOR_QUICK, linestyle='--')
    ax1.plot(df['tamanho'], df['pior_smart_t'], marker='D', label='Smart', color=COLOR_SMART, linewidth=2.5)
    ax1.set_title("5A. Tempo no Pior Caso (ms)")
    ax1.set_xlabel("Tamanho (N)")
    ax1.set_ylabel("Tempo (ms)")
    ax1.legend()
    ax1.grid(True, linestyle=':')

    # Subplot Memória
    ax2.plot(df['tamanho'], df['pior_merge_m'], marker='o', label='Merge', color=COLOR_MERGE)
    ax2.plot(df['tamanho'], df['pior_quick_m'], marker='x', label='Quick', color=COLOR_QUICK, linestyle='--')
    ax2.plot(df['tamanho'], df['pior_smart_m'], marker='D', label='Smart', color=COLOR_SMART, linewidth=2.5)
    ax2.set_title("5B. Memória no Pior Caso (KB)")
    ax2.set_xlabel("Tamanho (N)")
    ax2.set_ylabel("Memória (KB)")
    ax2.legend()
    ax2.grid(True, linestyle=':')

    plt.suptitle("5. ANÁLISE DE OTIMIZAÇÃO: Custo de Tempo vs Custo de Memória", fontsize=16)
    plt.tight_layout()
    plt.savefig(os.path.join(OUTPUT_DIR, '05_analise_tempo_vs_espaco.png'))
    print("Salvo: 05_analise_tempo_vs_espaco.png")

    # =======================================================
    # GRÁFICO 6: MEMÓRIA - CASO MÉDIO (BARRAS)
    # =======================================================
    plt.figure(figsize=(10, 6))
    plt.bar(df['tamanho'] - 100, df['medio_merge_m'], width=100, label='Merge', color=COLOR_MERGE, alpha=0.7)
    plt.bar(df['tamanho'],      df['medio_quick_m'], width=100, label='Quick', color=COLOR_QUICK, alpha=0.7)
    plt.bar(df['tamanho'] + 100, df['medio_smart_m'], width=100, label='Smart', color=COLOR_SMART, alpha=0.9)

    plt.title('6. EFICIÊNCIA DE MEMÓRIA (Caso Médio)\nSmart Sort economiza tanto quanto o Quick?')
    plt.xlabel('Tamanho (N)')
    plt.ylabel('Memória Alocada (KB)')
    plt.legend()
    
    plt.tight_layout()
    plt.savefig(os.path.join(OUTPUT_DIR, '06_memoria_barras.png'))
    print("Salvo: 06_memoria_barras.png")

except Exception as e:
    print(f"Erro: {e}")