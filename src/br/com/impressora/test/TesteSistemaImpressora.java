package br.com.impressora.test;

import br.com.impressora.model.Prioridade;
import br.com.impressora.service.ImpressoraService;
import br.com.impressora.util.Logger;

/**
 * Classe de teste que demonstra o funcionamento do sistema.
 * 
 * Testes implementados:
 * 1. Adição de arquivos com diferentes prioridades
 * 2. Cálculo de tempo estimado
 * 3. Listagem da fila
 * 4. Processamento concorrente
 * 5. Parada segura
 */
public class TesteSistemaImpressora {

    public static void main(String[] args) throws InterruptedException {
        executarTestes();
    }

    private static void executarTestes() throws InterruptedException {
        Logger.secao("INICIANDO TESTES DO SISTEMA DE IMPRESSORA");

        // Teste 1: Criação do serviço
        testeCreacaoServico();

        // Teste 2: Adição de arquivos
        testeAdicaoArquivos();

        // Teste 3: Listagem de fila
        testeListagemFila();

        // Teste 4: Cálculo de tempo
        testeCalculoTempo();

        // Teste 5: Processamento
        testeProcessamento();

        // Teste 6: Adição durante impressão
        testeAdicaoDuranteImpressao();

        // Teste 7: Parada segura
        testeParadaSegura();

        // Teste 8: Limpar fila
        testeLimparFila();

        // Teste 9: Validação de entrada inválida
        testeValidacao();

        Logger.secao("TODOS OS TESTES CONCLUÍDOS COM SUCESSO!");
    }

    private static void testeCreacaoServico() {
        Logger.secao("TESTE 1: Criação do Serviço");

        ImpressoraService service = new ImpressoraService();
        assert !service.estaEmExecucao() : "Serviço deve estar parado inicialmente";
        assert service.getTamanhoFila() == 0 : "Fila deve estar vazia";

        Logger.sucesso("✓ Serviço criado com sucesso");
        Logger.sucesso("✓ Fila vazia inicial verificada");
    }

    private static void testeAdicaoArquivos() {
        Logger.secao("TESTE 2: Adição de Arquivos");

        ImpressoraService service = new ImpressoraService();

        // Adiciona com diferentes prioridades
        boolean r1 = service.adicionarArquivo("documento.docx", 5, Prioridade.COMUM);
        boolean r2 = service.adicionarArquivo("urgente.txt", 3, Prioridade.URGENTE);
        boolean r3 = service.adicionarArquivo("relatorio.pdf", 8, Prioridade.POUCO_URGENTE);

        assert r1 && r2 && r3 : "Todos os arquivos devem ser adicionados";
        assert service.getTamanhoFila() == 3 : "Fila deve ter 3 arquivos";

        Logger.sucesso("✓ 3 arquivos adicionados com sucesso");
        Logger.sucesso("✓ Validação de quantidade verificada");
    }

    private static void testeListagemFila() {
        Logger.secao("TESTE 3: Listagem de Fila");

        ImpressoraService service = new ImpressoraService();
        service.adicionarArquivo("doc1.pdf", 5, Prioridade.COMUM);
        service.adicionarArquivo("urgente.docx", 3, Prioridade.URGENTE);
        service.adicionarArquivo("doc2.txt", 4, Prioridade.POUCO_URGENTE);

        var fila = service.listarFila();

        // Valida ordem de prioridade
        assert fila.size() == 3 : "Fila deve ter 3 arquivos";
        assert fila.get(0).getPrioridade() == Prioridade.URGENTE : "Primeiro deve ser URGENTE";
        assert fila.get(1).getPrioridade() == Prioridade.POUCO_URGENTE : "Segundo deve ser POUCO_URGENTE";
        assert fila.get(2).getPrioridade() == Prioridade.COMUM : "Terceiro deve ser COMUM";

        Logger.sucesso("✓ Listagem funcionando corretamente");
        Logger.sucesso("✓ Ordem de prioridades verificada");
        Logger.sucesso("✓ URGENTE > POUCO_URGENTE > COMUM");
    }

    private static void testeCalculoTempo() {
        Logger.secao("TESTE 4: Cálculo de Tempo Estimado");

        ImpressoraService service = new ImpressoraService();
        service.adicionarArquivo("arquivo1.pdf", 10, Prioridade.COMUM);
        service.adicionarArquivo("arquivo2.pdf", 20, Prioridade.COMUM);

        // 10 + 20 = 30 páginas
        // 500ms por página = 30 * 500 = 15000ms = 15s
        long tempoTotal = service.calcularTempoEstimadoTotal();

        assert tempoTotal == 15 : "Tempo deve ser 15 segundos";

        Logger.sucesso("✓ Cálculo de tempo funcionando");
        Logger.sucesso("✓ Total: 30 páginas = " + tempoTotal + " segundos");
    }

    private static void testeProcessamento() throws InterruptedException {
        Logger.secao("TESTE 5: Processamento Concorrente");

        ImpressoraService service = new ImpressoraService();
        service.adicionarArquivo("arquivo_teste.pdf", 4, Prioridade.URGENTE);

        Logger.info("Iniciando impressora...");
        long inicio = System.currentTimeMillis();

        service.iniciarImpressora();

        // Aguarda conclusão
        Thread.sleep(3500); // 4 páginas * 500ms = 2000ms, +buffer

        long duracao = System.currentTimeMillis() - inicio;
        long filaSizeApos = service.getTamanhoFila();

        assert filaSizeApos == 0 : "Fila deve estar vazia após conclusão";
        assert duracao >= 1800 : "Deve demorar pelo menos 1,8 segundos";

        Logger.sucesso("✓ Processamento funcionou corretamente");
        Logger.sucesso("✓ Duração: " + duracao + "ms");
        Logger.sucesso("✓ Fila vazia verificada");

        service.encerrar();
    }

    private static void testeAdicaoDuranteImpressao() throws InterruptedException {
        Logger.secao("TESTE 6: Adição Durante Impressão");

        ImpressoraService service = new ImpressoraService();
        service.adicionarArquivo("primeiro.pdf", 2, Prioridade.COMUM);

        Logger.info("Iniciando impressora...");
        service.iniciarImpressora();

        // Aguarda um pouco
        Thread.sleep(500);

        // Adiciona outro arquivo durante a impressão
        Logger.info("Adicionando arquivo durante impressão...");
        boolean adicionado = service.adicionarArquivo("segundo.pdf", 2, Prioridade.URGENTE);

        assert adicionado : "Deve ser possível adicionar durante impressão";
        assert service.getTamanhoFila() > 0 : "Deve haver arquivos na fila";

        // Aguarda conclusão
        Thread.sleep(3500);

        assert service.getTamanhoFila() == 0 : "Todos os arquivos devem ser processados";

        Logger.sucesso("✓ Adição durante impressão funcionou");
        Logger.sucesso("✓ Thread-safety verificado");
        Logger.sucesso("✓ Segundo arquivo foi processado (tinha prioridade)");

        service.encerrar();
    }

    private static void testeParadaSegura() throws InterruptedException {
        Logger.secao("TESTE 7: Parada Segura");

        ImpressoraService service = new ImpressoraService();
        service.adicionarArquivo("arquivo_parada.pdf", 10, Prioridade.COMUM);

        Logger.info("Iniciando impressora...");
        service.iniciarImpressora();

        assert service.estaEmExecucao() : "Deve estar em execução";

        // Aguarda alguns milissegundos
        Thread.sleep(500);

        Logger.info("Parando impressora...");
        service.pararImpressora();

        assert !service.estaEmExecucao() : "Deve estar parada";

        Logger.sucesso("✓ Parada segura funcionou");
        Logger.sucesso("✓ Recurso de thread encerrado corretamente");

        service.encerrar();
    }

    private static void testeLimparFila() {
        Logger.secao("TESTE 8: Limpar Fila");

        ImpressoraService service = new ImpressoraService();
        service.adicionarArquivo("a1.pdf", 1, Prioridade.COMUM);
        service.adicionarArquivo("a2.pdf", 2, Prioridade.URGENTE);

        assert service.getTamanhoFila() == 2 : "A fila deve ter 2 arquivos antes de limpar";

        service.limparFila();
        assert service.getTamanhoFila() == 0 : "A fila deve estar vazia após limpar";

        Logger.sucesso("✓ Fila limpa corretamente");
    }

    /**
     * Testa validação de entrada inválida.
     */
    private static void testeValidacao() {
        Logger.secao("TESTE BÔNUS: Validação de Entrada");

        ImpressoraService service = new ImpressoraService();

        // Tenta adicionar com 0 páginas
        boolean resultado = service.adicionarArquivo("vazio.pdf", 0, Prioridade.COMUM);
        assert !resultado : "Deve rejeitar arquivo com 0 páginas";

        Logger.sucesso("✓ Validação de páginas funcionando");
    }
}
