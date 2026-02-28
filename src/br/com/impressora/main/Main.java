package br.com.impressora.main;

import br.com.impressora.model.Prioridade;
import br.com.impressora.service.ImpressoraService;
import br.com.impressora.util.Logger;

import java.util.Scanner;

/**
 * Classe principal com menu interativo.
 * 
 * Permite ao usuário:
 * - Adicionar arquivos à fila
 * - Iniciar/parar a impressora
 * - Visualizar status e fila
 * - Encerrar o programa
 */
public class Main {
    private static final ImpressoraService service = new ImpressoraService();
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean executando = true;

    public static void main(String[] args) {
        exibirBemVindo();

        while (executando) {
            exibirMenu();
            processarOpcao();
        }

        encerrar();
    }

    /**
     * Exibe mensagem de boas-vindas.
     */
    private static void exibirBemVindo() {
        Logger.secao("BEM-VINDO AO SISTEMA DE GERENCIAMENTO DE IMPRESSORA");
        System.out.println("Este sistema simula uma impressora com controle de prioridade");
        System.out.println("e processamento concorrente de arquivos usando Threads.");
        System.out.println();
        Logger.info("Digite uma opção do menu para começar...");
        System.out.println();
    }

    /**
     * Exibe o menu principal.
     */
    private static void exibirMenu() {
        Logger.secao("MENU PRINCIPAL");
        System.out.println("1 - Adicionar arquivo");
        System.out.println("2 - Listar fila");
        System.out.println("3 - Iniciar impressora");
        System.out.println("4 - Parar impressora");
        System.out.println("5 - Ver status detalhado");
        System.out.println("6 - Limpar tela");
        System.out.println("7 - Limpar fila de impressão");
        System.out.println("0 - Sair");
        Logger.separador();
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Processa a opção escolhida pelo usuário.
     */
    private static void processarOpcao() {
        String opcao = scanner.nextLine().trim();

        switch (opcao) {
            case "1" -> adicionarArquivo();
            case "2" -> listarFila();
            case "3" -> iniciarImpressora();
            case "4" -> pararImpressora();
            case "5" -> verStatus();
            case "6" -> limparTela();
            case "7" -> limparFila();
            case "0" -> executando = false;
            default -> Logger.erro("Opção inválida. Tente novamente.");
        }

        System.out.println();
    }

    /**
     * Menu para adicionar novo arquivo.
     */
    private static void adicionarArquivo() {
        Logger.secao("ADICIONAR NOVO ARQUIVO");

        System.out.print("Nome do arquivo: ");
        String nome = scanner.nextLine().trim();

        if (nome.isEmpty()) {
            Logger.erro("Nome não pode estar vazio");
            return;
        }

        System.out.print("Número de páginas: ");
        int paginas;
        try {
            paginas = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            Logger.erro("Número de páginas inválido");
            return;
        }

        System.out.println("\nEscolha a prioridade:");
        System.out.println("1 - Urgente");
        System.out.println("2 - Pouco Urgente");
        System.out.println("3 - Comum");
        System.out.print("Prioridade: ");

        Prioridade prioridade = switch (scanner.nextLine().trim()) {
            case "1" -> Prioridade.URGENTE;
            case "2" -> Prioridade.POUCO_URGENTE;
            case "3" -> Prioridade.COMUM;
            default -> {
                Logger.aviso("Prioridade inválida. Usando 'Comum'");
                yield Prioridade.COMUM;
            }
        };

        boolean adicionado = service.adicionarArquivo(nome, paginas, prioridade);

        if (adicionado && !service.estaEmExecucao()) {
            System.out.print("\nDeseja iniciar a impressora agora? (s/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                service.iniciarImpressora();
            }
        }
    }

    /**
     * Lista todos os arquivos na fila.
     */
    private static void listarFila() {
        Logger.secao("FILA DE IMPRESSÃO");

        if (service.getTamanhoFila() == 0) {
            Logger.aviso("Fila vazia");
            return;
        }

        System.out.println("Arquivos na fila (em ordem de prioridade):\n");
        service.listarFila().forEach(arquivo ->
            System.out.println("  • " + arquivo)
        );

        long tempoTotal = service.calcularTempoEstimadoTotal();
        System.out.printf(
            "\n  Total: %d arquivo(s) | Tempo estimado: %ds (%s)%n",
            service.getTamanhoFila(),
            tempoTotal,
            formatarTempo(tempoTotal)
        );
    }

    /**
     * Inicia a impressora.
     */
    private static void iniciarImpressora() {
        Logger.secao("INICIAR IMPRESSORA");

        if (service.getTamanhoFila() == 0) {
            Logger.aviso("Nenhum arquivo na fila. Adicione um arquivo primeiro.");
            return;
        }

        service.iniciarImpressora();

        System.out.print("\nPressione Enter para voltar ao menu (a impressora funcionará em background)...");
        scanner.nextLine();
    }

    /**
     * Para a impressora.
     */
    private static void pararImpressora() {
        Logger.secao("PARAR IMPRESSORA");

        if (!service.estaEmExecucao()) {
            Logger.aviso("Impressora não está em execução");
            return;
        }

        System.out.print("Deseja parar a impressora? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
            service.pararImpressora();
        } else {
            Logger.info("Operação cancelada");
        }
    }

    /**
     * Exibe status detalhado da impressora.
     */
    private static void verStatus() {
        service.exibirStatus();

        System.out.print("Pressione Enter para voltar ao menu...");
        scanner.nextLine();
    }

    /**
     * Limpa todos os arquivos da fila sem encerrar o serviço.
     */
    private static void limparFila() {
        Logger.secao("LIMPAR FILA");
        service.limparFila();
    }

    /**
     * Limpa a tela do console.
     */
    private static void limparTela() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            Logger.erro("Erro ao limpar tela: " + e.getMessage());
        }
    }

    /**
     * Encerra a aplicação.
     */
    private static void encerrar() {
        Logger.secao("ENCERRANDO APLICAÇÃO");

        if (service.estaEmExecucao()) {
            Logger.info("Parando impressora antes de encerrar...");
            service.pararImpressora();
        }

        service.encerrar();
        scanner.close();

        Logger.sucesso("Programa encerrado com sucesso!");
    }

    /**
     * Formata um tempo em segundos para formato legível.
     */
    private static String formatarTempo(long segundos) {
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
}
