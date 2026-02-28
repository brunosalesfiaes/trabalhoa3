package br.com.impressora.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitária para logging formatado no console.
 * Fornece métodos para registrar diferentes tipos de mensagens com timestamps.
 */
public class Logger {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
    private static final String RESET = "\u001B[0m";
    private static final String BOLD = "\u001B[1m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String RED = "\u001B[31m";
    private static final String CYAN = "\u001B[36m";

    private Logger() {
        // Classe utilitária não deve ser instanciada
    }

    /**
     * Log de informação (azul)
     */
    public static void info(String mensagem) {
        print(BLUE + "[INFO]" + RESET, mensagem);
    }

    /**
     * Log de sucesso (verde)
     */
    public static void sucesso(String mensagem) {
        print(GREEN + "[SUCESSO]" + RESET, mensagem);
    }

    /**
     * Log de aviso (amarelo)
     */
    public static void aviso(String mensagem) {
        print(YELLOW + "[AVISO]" + RESET, mensagem);
    }

    /**
     * Log de erro (vermelho)
     */
    public static void erro(String mensagem) {
        print(RED + "[ERRO]" + RESET, mensagem);
    }

    /**
     * Log de impressora (ciano)
     */
    public static void impressora(String mensagem) {
        print(CYAN + "[IMPRESSORA]" + RESET, mensagem);
    }

    /**
     * Log de thread (verde claro com negrito)
     */
    public static void thread(String mensagem) {
        print(GREEN + BOLD + "[THREAD]" + RESET, mensagem);
    }

    /**
     * Imprime com formatação e timestamp
     */
    private static void print(String tipo, String mensagem) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        System.out.printf("%s %s - %s%n", timestamp, tipo, mensagem);
    }

    /**
     * Imprime uma linha separadora
     */
    public static void separador() {
        System.out.println("═".repeat(80));
    }

    /**
     * Imprime com cor de seção
     */
    public static void secao(String titulo) {
        separador();
        System.out.println(BOLD + CYAN + "  " + titulo + RESET);
        separador();
    }
}
