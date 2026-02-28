package br.com.impressora.service;

import br.com.impressora.model.Arquivo;
import br.com.impressora.model.Prioridade;
import br.com.impressora.thread.ImpressoraWorker;
import br.com.impressora.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Serviço central que gerencia:
 * - Fila de impressão com prioridades
 * - Lifecycle da thread worker
 * - Operações de cadastro e listagem
 * - Cálculos de tempo estimado
 * 
 * Thread-safe: utiliza PriorityBlockingQueue e sincronização adequada.
 */
public class ImpressoraService {
    private final BlockingQueue<Arquivo> fila;
    private final ImpressoraWorker worker;
    // executor pode ser recriado em cada start para permitir reinícios
    private ExecutorService executorService;
    private volatile boolean iniciado;

    /**
     * Construtor do serviço.
     * Inicializa a fila de prioridade thread-safe.
     */
    public ImpressoraService() {
        // PriorityBlockingQueue é thread-safe e usa Comparable
        this.fila = new PriorityBlockingQueue<>();
        this.worker = new ImpressoraWorker(fila);
        // executor criado somente quando necessário ao iniciar
        this.executorService = null;
        this.iniciado = false;
    }

    /**
     * Adiciona um novo arquivo à fila de impressão.
     *
     * @param nome nome do arquivo
     * @param quantidadePaginas número de páginas
     * @param prioridade nível de urgência
     * @return true se adicionado com sucesso
     */
    public boolean adicionarArquivo(String nome, int quantidadePaginas, Prioridade prioridade) {
        if (quantidadePaginas <= 0) {
            Logger.erro("Número de páginas deve ser maior que 0");
            return false;
        }

        try {
            Arquivo arquivo = new Arquivo(nome, quantidadePaginas, prioridade);
            boolean adicionado = fila.offer(arquivo);

            if (adicionado) {
                Logger.sucesso("Arquivo adicionado à fila: " + arquivo);
                Logger.info("Fila agora tem " + fila.size() + " arquivo(s)");
            } else {
                Logger.erro("Não foi possível adicionar arquivo à fila");
            }

            return adicionado;

        } catch (Exception e) {
            Logger.erro("Erro ao adicionar arquivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inicia a thread de impressão.
     */
    public void iniciarImpressora() {
        if (iniciado) {
            Logger.aviso("Impressora já está em execução");
            return;
        }

        // cria novo executor se for a primeira vez ou se o anterior foi finalizado
        if (executorService == null || executorService.isShutdown() || executorService.isTerminated()) {
            executorService = Executors.newSingleThreadExecutor(r -> {
                Thread t = new Thread(r, "ImpressoraWorker-Thread");
                t.setDaemon(false);
                return t;
            });
        }

        Logger.info("Iniciando impressora...");
        worker.iniciar();
        executorService.execute(worker);
        iniciado = true;
        Logger.sucesso("Impressora iniciada com sucesso");
    }

    /**
     * Para a impressora de forma segura.
     */
    public void pararImpressora() {
        if (!iniciado) {
            Logger.aviso("Impressora não está em execução");
            return;
        }

        Logger.info("Parando impressora...");
        worker.parar();

        // sinaliza encerramento ao executor e aguarda
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    Logger.aviso("Timeout ao parar impressora. Forçando encerramento...");
                    executorService.shutdownNow();
                }
                Logger.sucesso("Impressora parada");
            } catch (InterruptedException e) {
                Logger.erro("Interrupção durante parada: " + e.getMessage());
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        iniciado = false;
    }

    /**
     * Retorna true se a impressora está em execução.
     */
    public boolean estaEmExecucao() {
        return iniciado && worker.estaRodando();
    }

    /**
     * Lista todos os arquivos atualmente na fila.
     *
     * @return lista de arquivos
     */
    public List<Arquivo> listarFila() {
        return new ArrayList<>(fila);
    }

    /**
     * Retorna o tamanho atual da fila.
     */
    public int getTamanhoFila() {
        return fila.size();
    }

    /**
     * Calcula o tempo estimado total para processar toda a fila (em segundos).
     * Usa 0.5 segundos por página (2 páginas/segundo)
     */
    public long calcularTempoEstimadoTotal() {
        return fila.stream()
            .mapToLong(a -> a.getPaginasRestantes() * 500L)
            .sum() / 1000;
    }

    /**
     * Calcula o tempo estimado para um arquivo específico.
     */
    public long calcularTempoEstimadoArquivo(Arquivo arquivo) {
        return (arquivo.getPaginasRestantes() * 500L) / 1000;
    }

    /**
     * Retorna o arquivo atualmente sendo impresso.
     */
    public Arquivo getArquivoAtual() {
        return worker.getArquivoAtual();
    }

    /**
     * Exibe informações detalhadas sobre o status da impressora.
     */
    public void exibirStatus() {
        Logger.secao("STATUS DA IMPRESSORA");

        System.out.println("Estado: " + (estaEmExecucao() ? "EM EXECUÇÃO" : "PARADA"));
        System.out.println("Arquivos na fila: " + getTamanhoFila());

        Arquivo atual = getArquivoAtual();
        if (atual != null) {
            System.out.println("\n>>> Arquivo em impressão:");
            System.out.println("    Nome: " + atual.getNome());
            System.out.println("    Prioridade: " + atual.getPrioridade().getDescricao());
            System.out.println("    Progresso: " + atual.getPaginasImpressas() + "/" +
                             atual.getQuantidadePaginas() + " páginas");
            System.out.println("    Tempo estimado: " +
                             calcularTempoEstimadoArquivo(atual) + "s");
        }

        if (!fila.isEmpty()) {
            System.out.println("\n>>> Próximos arquivos na fila:");
            listarFila().stream()
                .limit(5)
                .forEach(a -> System.out.println("    • " + a));

            if (fila.size() > 5) {
                System.out.println("    ... e mais " + (fila.size() - 5) + " arquivo(s)");
            }
        }

        long tempoTotal = calcularTempoEstimadoTotal();
        System.out.println("\nTempo estimado total da fila: " + tempoTotal + "s " +
                         "(" + formatarTempo(tempoTotal) + ")");

        Logger.separador();
    }

    /**
     * Formata um tempo em segundos para formato HH:MM:SS.
     */
    private String formatarTempo(long segundos) {
        long horas = segundos / 3600;
        long minutos = (segundos % 3600) / 60;
        long segs = segundos % 60;

        if (horas > 0) {
            return String.format("%dh %dm %ds", horas, minutos, segs);
        } else if (minutos > 0) {
            return String.format("%dm %ds", minutos, segs);
        } else {
            return String.format("%ds", segs);
        }
    }

    /**
     * Limpa todos os arquivos da fila. Útil para descarregar a fila manualmente.
     */
    public void limparFila() {
        int removed = fila.size();
        fila.clear();
        Logger.aviso("Fila limpa (" + removed + " arquivo(s) removido(s))");
    }

    /**
     * Encerra o serviço de forma segura.
     */
    public void encerrar() {
        if (estaEmExecucao()) {
            pararImpressora();
        }

        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        Logger.info("Serviço de impressora encerrado");
        // limpa fila para liberar recursos
        fila.clear();
    }
}
