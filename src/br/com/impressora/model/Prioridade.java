package br.com.impressora.model;

/**
 * Enum que define os níveis de prioridade dos arquivos na fila de impressão.
 * 
 * A prioridade é usada para ordenar os arquivos na fila:
 * - URGENTE: maior prioridade
 * - POUCO_URGENTE: prioridade média
 * - COMUM: menor prioridade
 */
public enum Prioridade {
    URGENTE(1, "Urgente"),
    POUCO_URGENTE(2, "Pouco Urgente"),
    COMUM(3, "Comum");

    private final int nivel;
    private final String descricao;

    /**
     * Construtor do enum Prioridade.
     *
     * @param nivel número que representa a prioridade (menor = maior prioridade)
     * @param descricao descrição legível da prioridade
     */
    Prioridade(int nivel, String descricao) {
        this.nivel = nivel;
        this.descricao = descricao;
    }

    /**
     * Retorna o nível numérico da prioridade.
     *
     * @return nível da prioridade
     */
    public int getNivel() {
        return nivel;
    }

    /**
     * Retorna a descrição da prioridade.
     *
     * @return descrição legível
     */
    public String getDescricao() {
        return descricao;
    }
}
