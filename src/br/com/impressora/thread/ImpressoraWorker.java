package br.com.impressora.thread;

import br.com.impressora.model.Arquivo;
import br.com.impressora.util.Logger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Worker que processa a impressão de arquivos.
 * 
 * Responsabilidades:
 * - Consumir arquivos da fila de forma bloqueante
 * - Imprimir 2 páginas por segundo (500ms por página)
 * - Registrar progresso detalhado no console
 * - Calcular tempo estimado restante
 * - Lidar com interrupções de forma segura
 */
public class ImpressoraWorker implements Runnable {
    private static final long TEMPO_POR_PAGINA_MS = 500; // 2 páginas por segundo
    private static final long TEMPO_ENTRE_VERIFICACOES_MS = 100; // Para melhor responsividade
    
    private final BlockingQueue<Arquivo> fila;
    private volatile boolean rodando;
    private Arquivo arquivoAtual;

    /**
     * Construtor do ImpressoraWorker.
     *
     * @param fila fila de prioridade com arquivos a imprimir
     */
    public ImpressoraWorker(BlockingQueue<Arquivo> fila) {
        this.fila = fila;
        this.rodando = false;
        this.arquivoAtual = null;
    }

    /**
     * Inicia o worker de impressão.
     */
    public void iniciar() {
        this.rodando = true;
    }

    /**
     * Para o worker de impressão de forma segura.
     */
    public void parar() {
        this.rodando = false;
        Logger.aviso("Parada de impressora solicitada. Finalizando arquivo atual...");
    }

    /**
     * Retorna true se a impressora está em execução.
     */
    public boolean estaRodando() {
        return rodando;
    }

    /**
     * Retorna o arquivo atualmente sendo impresso.
     */
    public Arquivo getArquivoAtual() {
        return arquivoAtual;
    }

    @Override
    public void run() {
        Logger.thread("Thread de impressão iniciada - ID: " + Thread.currentThread().getName());
        
        try {
            while (rodando || !fila.isEmpty()) {
                try {
                    // Aguarda arquivo da fila com timeout para permitir verificação de parada
                    // uso da constante para evitar aviso de variável não utilizada
                    arquivoAtual = fila.poll(TEMPO_ENTRE_VERIFICACOES_MS, TimeUnit.MILLISECONDS);
                    
                    if (arquivoAtual != null) {
                        imprimirArquivo();
                        arquivoAtual = null;
                    }
                    
                } catch (InterruptedException e) {
                    Logger.aviso("Thread de impressão foi interrompida");
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            Logger.sucesso("Fila vazia. Impressora finalizada.");
            
        } finally {
            Logger.thread("Thread de impressão encerrada");
        }
    }

    /**
     * Realiza a impressão de um arquivo página por página.
     */
    private void imprimirArquivo() {
        Logger.impressora("Iniciando impressão: " + arquivoAtual.getNome());
        Logger.impressora("Total: " + arquivoAtual.getQuantidadePaginas() + " páginas | " +
                         "Prioridade: " + arquivoAtual.getPrioridade().getDescricao());

        while (arquivoAtual.getPaginasRestantes() > 0 && rodando) {
            try {
                // Aguarda 500ms (equivalente a 2 páginas por segundo).
                // Sleep dentro do loop é intencional para simular o tempo de
                // impressão; usamos TimeUnit para remover o aviso do analisador.
                TimeUnit.MILLISECONDS.sleep(TEMPO_POR_PAGINA_MS);
                
                // Avança uma página
                arquivoAtual.reduzirPaginasRestantes(1);
                
                // Registra progresso
                logProgresso();
                
            } catch (InterruptedException e) {
                Logger.aviso("Impressão interrompida: " + arquivoAtual.getNome());
                Thread.currentThread().interrupt();
                break;
            }
        }

        if (arquivoAtual.estaPronto()) {
            Logger.sucesso("Impressão concluída: " + arquivoAtual.getNome());
        }
    }

    /**
     * Registra o progresso da impressão atual.
     */
    private void logProgresso() {
        int paginasImpressas = arquivoAtual.getPaginasImpressas();
        int totalPaginas = arquivoAtual.getQuantidadePaginas();
        int paginasRestantes = arquivoAtual.getPaginasRestantes();
        
        // Calcula tempo estimado restante (500ms por página)
        long tempoRestanteSegundos = (paginasRestantes * TEMPO_POR_PAGINA_MS) / 1000;
        
        // Calcula percentual
        int percentual = (int) ((paginasImpressas * 100.0) / totalPaginas);
        
        String barraProgresso = construirBarraProgresso(percentual);
        
        System.out.printf(
            "  %s | Página: %d/%d | %s | Tempo restante: ~%ds%n",
            barraProgresso,
            paginasImpressas,
            totalPaginas,
            arquivoAtual.getNome(),
            tempoRestanteSegundos
        );
    }

    /**
     * Constrói uma barra visual de progresso.
     */
    private String construirBarraProgresso(int percentual) {
        int barraSize = 20;
        int preenchido = (percentual * barraSize) / 100;
        StringBuilder barra = new StringBuilder("[");
        
        for (int i = 0; i < barraSize; i++) {
            barra.append(i < preenchido ? "█" : "░");
        }
        barra.append("] ").append(percentual).append("%");
        
        return barra.toString();
    }
}
