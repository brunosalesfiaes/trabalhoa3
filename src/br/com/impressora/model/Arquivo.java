package br.com.impressora.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Classe que representa um arquivo a ser impresso.
 * 
 * Cada arquivo possui:
 * - Nome: identificação do arquivo
 * - Quantidade de páginas: total de páginas a imprimir
 * - Prioridade: nível de urgência na fila
 * - Páginas restantes: rastreamento do progresso
 * - Timestamp: quando foi adicionado à fila
 */
public class Arquivo implements Comparable<Arquivo> {
    private final String nome;
    private final int quantidadePaginas;
    private final Prioridade prioridade;
    private int paginasRestantes;
    private final LocalDateTime dataAdicao;

    /**
     * Construtor do Arquivo.
     *
     * @param nome nome do arquivo
     * @param quantidadePaginas número de páginas
     * @param prioridade nível de prioridade
     */
    public Arquivo(String nome, int quantidadePaginas, Prioridade prioridade) {
        this.nome = Objects.requireNonNull(nome, "Nome do arquivo não pode ser nulo");
        this.quantidadePaginas = quantidadePaginas;
        this.prioridade = Objects.requireNonNull(prioridade, "Prioridade não pode ser nula");
        this.paginasRestantes = quantidadePaginas;
        this.dataAdicao = LocalDateTime.now();
    }

    /**
     * Retorna o nome do arquivo.
     *
     * @return nome do arquivo
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna a quantidade total de páginas.
     *
     * @return quantidade de páginas
     */
    public int getQuantidadePaginas() {
        return quantidadePaginas;
    }

    /**
     * Retorna a prioridade do arquivo.
     *
     * @return prioridade
     */
    public Prioridade getPrioridade() {
        return prioridade;
    }

    /**
     * Retorna o número de páginas ainda a imprimir.
     *
     * @return páginas restantes
     */
    public int getPaginasRestantes() {
        return paginasRestantes;
    }

    /**
     * Retorna o número de páginas já impressas.
     *
     * @return páginas impressas
     */
    public int getPaginasImpressas() {
        return quantidadePaginas - paginasRestantes;
    }

    /**
     * Reduz o número de páginas restantes.
     *
     * @param quantidade número de páginas a reduzir
     */
    public void reduzirPaginasRestantes(int quantidade) {
        this.paginasRestantes = Math.max(0, paginasRestantes - quantidade);
    }

    /**
     * Verifica se o arquivo foi completamente impresso.
     *
     * @return true se não há mais páginas, false caso contrário
     */
    public boolean estaPronto() {
        return paginasRestantes == 0;
    }

    /**
     * Retorna a data e hora em que foi adicionado à fila.
     *
     * @return data de adição
     */
    public LocalDateTime getDataAdicao() {
        return dataAdicao;
    }

    /**
     * Compara dois arquivos por prioridade e ordem de adição (FIFO para mesma prioridade).
     *
     * @param outro arquivo para comparação
     * @return resultado da comparação
     */
    @Override
    public int compareTo(Arquivo outro) {
        // Primeiro compara por prioridade
        int comparacaoPrioridade = Integer.compare(
            this.prioridade.getNivel(),
            outro.prioridade.getNivel()
        );

        // Se mesma prioridade, respeita ordem de adição (FIFO)
        if (comparacaoPrioridade == 0) {
            return this.dataAdicao.compareTo(outro.dataAdicao);
        }

        return comparacaoPrioridade;
    }

    @Override
    public String toString() {
        return String.format(
            "%s | Prioridade: %s | Páginas: %d/%d | Adicionado: %s",
            nome,
            prioridade.getDescricao(),
            getPaginasImpressas(),
            quantidadePaginas,
            dataAdicao.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arquivo arquivo = (Arquivo) o;
        return Objects.equals(nome, arquivo.nome) &&
               Objects.equals(dataAdicao, arquivo.dataAdicao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, dataAdicao);
    }
}
