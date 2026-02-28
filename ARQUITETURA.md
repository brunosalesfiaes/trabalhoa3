# 🏗️ Arquitetura do Sistema de Impressora

## 📐 Diagrama de Classes

```
┌─────────────────────────────────────────────────────────────────────────┐
│                          PACKAGE STRUCTURE                              │
└─────────────────────────────────────────────────────────────────────────┘

br.com.impressora
│
├── model/
│   ├── Prioridade (enum)
│   │   └── URGENTE(1)
│   │   └── POUCO_URGENTE(2)
│   │   └── COMUM(3)
│   │
│   └── Arquivo (implements Comparable<Arquivo>)
│       ├── - nome: String
│       ├── - quantidadePaginas: int
│       ├── - prioridade: Prioridade
│       ├── - paginasRestantes: int
│       ├── - dataAdicao: LocalDateTime
│       │
│       ├── + getNome(): String
│       ├── + getQuantidadePaginas(): int
│       ├── + getPrioridade(): Prioridade
│       ├── + getPaginasRestantes(): int
│       ├── + getPaginasImpressas(): int
│       ├── + reduzirPaginasRestantes(int): void
│       ├── + estaPronto(): boolean
│       ├── + compareTo(Arquivo): int
│       └── + toString(): String
│
├── service/
│   └── ImpressoraService
│       ├── - fila: PriorityBlockingQueue<Arquivo>
│       ├── - worker: ImpressoraWorker
│       ├── - executorService: ExecutorService
│       ├── - iniciado: boolean
│       │
│       ├── + adicionarArquivo(String, int, Prioridade): boolean
│       ├── + iniciarImpressora(): void
│       ├── + pararImpressora(): void
│       ├── + estaEmExecucao(): boolean
│       ├── + listarFila(): List<Arquivo>
│       ├── + getTamanhoFila(): int
│       ├── + calcularTempoEstimadoTotal(): long
│       ├── + calcularTempoEstimadoArquivo(Arquivo): long
│       ├── + getArquivoAtual(): Arquivo
│       ├── + exibirStatus(): void
│       └── + encerrar(): void
│
├── thread/
│   └── ImpressoraWorker (implements Runnable)
│       ├── - fila: BlockingQueue<Arquivo>
│       ├── - rodando: volatile boolean
│       ├── - arquivoAtual: Arquivo
│       ├── - TEMPO_POR_PAGINA_MS: long = 500
│       │
│       ├── + iniciar(): void
│       ├── + parar(): void
│       ├── + estaRodando(): boolean
│       ├── + getArquivoAtual(): Arquivo
│       ├── + run(): void
│       ├── - imprimirArquivo(): void
│       ├── - logProgresso(): void
│       └── - construirBarraProgresso(int): String
│
├── util/
│   └── Logger (utilidade)
│       ├── - RESET, BOLD, GREEN, YELLOW, BLUE, RED, CYAN: String
│       │
│       + info(String): void
│       + sucesso(String): void
│       + aviso(String): void
│       + erro(String): void
│       + impressora(String): void
│       + thread(String): void
│       + separador(): void
│       + secao(String): void
│
└── main/
    └── Main
        ├── - service: ImpressoraService
        ├── - scanner: Scanner
        ├── - executando: boolean
        │
        ├── + main(String[]): void
        ├── - exibirBemVindo(): void
        ├── - exibirMenu(): void
        ├── - processarOpcao(): void
        ├── - adicionarArquivo(): void
        ├── - listarFila(): void
        ├── - iniciarImpressora(): void
        ├── - pararImpressora(): void
        ├── - verStatus(): void
        ├── - limparTela(): void
        ├── - encerrar(): void
        └── - formatarTempo(long): String
```

---

## 🔄 Fluxo de Dados

### 1. **Ciclo de Adição de Arquivo**

```
┌─────────────┐
│   Main      │
│   (Menu)    │
└──────┬──────┘
       │ input: nome, páginas, prioridade
       ↓
┌──────────────────────────┐
│ ImpressoraService        │
│  adicionarArquivo()      │
└──────┬───────────────────┘
       │ cria novo
       ↓
┌──────────────────────────┐
│ Arquivo                  │
│ - Valida entrada         │
│ - De timestamp           │
└──────┬───────────────────┘
       │ inserir com compareTo
       ↓
┌──────────────────────────┐
│ PriorityBlockingQueue    │
│ (reordena por prioridade)│
└──────────────────────────┘
       │
       ↓ se fila não vazia
┌──────────────────────────┐
│ Main                     │
│ "Deseja iniciar?"        │
└──────────────────────────┘
```

### 2. **Ciclo de Execução (Impressão)**

```
┌─────────────────────┐
│ Main.main()         │
│ (Thread Principal)  │
└──────────┬──────────┘
           │
           ↓
    ┌──────────────┐
    │ Menu Ativo   │
    │ (input)      │
    │              │
    │ Opção 3:     │
    │ Iniciar      │
    └──────┬───────┘
           │
           ↓
┌─────────────────────────────────┐
│ ImpressoraService               │
│  .iniciarImpressora()           │
│  - worker.iniciar()             │
│  - submit worker ao executor    │
└──────────┬──────────────────────┘
           │
           ↓ (nova thread)
┌─────────────────────────────────┐
│ ImpressoraWorker.run()          │
│ (Thread Separada)               │
│                                 │
│ while (rodando || !fila.vazia):  │
│   arquivo = fila.poll()         │
│   imprimirArquivo()             │
│     while (páginas > 0):        │
│       sleep(500ms)              │
│       reduzirPáginas()          │
│       logProgresso()            │
└─────────────────────────────────┘
           │
           ↓
┌─────────────────────────────────┐
│ Logger                          │
│ - registra progresso            │
│ - formata output                │
│ - adiciona cores               │
└─────────────────────────────────┘
```

### 3. **Ciclo de Parada Segura**

```
┌──────────────┐
│ Main (Menu)  │
│ Opção 4      │
└──────┬───────┘
       │
       ↓
┌───────────────────────────────┐
│ ImpressoraService             │
│  .pararImpressora()           │
│  - worker.parar()             │
│  - executorService.shutdown() │
│  - awaitTermination(timeout)  │
└───────┬─────────────────────────┘
        │
        ↓
┌────────────────────────────────┐
│ ImpressoraWorker.run()         │
│ rodando = false                │
│ Finalizando arquivo atual...   │
│ Sai do loop                    │
└────────────────────────────────┘
```

---

## 🔀 Diagrama de Relações

```
Main (Interface)
  │
  │ usa
  ↓
ImpressoraService (Orquestrador)
  │
  ├─ possui → PriorityBlockingQueue<Arquivo>
  ├─ cria → ImpressoraWorker
  └─ gerencia → ExecutorService
        │
        └─ executa → ImpressoraWorker (Runnable)
              │
              ├─ consome → Arquivo
              │             │
              │             └─ possui → Prioridade
              │
              └─ registra → Logger

```

---

## 🔐 Thread-Safety Analysis

### PriorityBlockingQueue

```java
BlockingQueue<Arquivo> fila = new PriorityBlockingQueue<>();
```

| Operação | Thread-Safe? | Razão |
|----------|-------------|-------|
| add | ✅ SIM | Sincronizado internamente |
| poll | ✅ SIM | Sincronizado internamente |
| isEmpty | ✅ SIM | Sincronizado internamente |
| Múltiplas threads adicionando | ✅ SIM | Lock interno garante exclusão mútua |
| Bloqueio quando vazia | ✅ SIM | poll(timeout) aguarda graciosamente |

### ImpressoraWorker

```java
volatile boolean rodando;  // Garante leitura/escrita atômica
```

| Operação | Thread-Safe? | Razão |
|----------|-------------|-------|
| Verificar rodando | ✅ SIM | volatile flag |
| Parar while estiver imprimindo | ✅ SIM | Verifica a cada página |
| InterruptedException | ✅ SIM | Tratada e re-propagada |
| Modificar paginasRestantes | ✅ SIM | Arquivo é consumido (não compartilhado) |

---

## 📊 Sequência de Operações

### Adição Concorrente Enquanto Imprime

```
Time    Main Thread              Worker Thread           Queue
────    ───────────────────     ──────────────────────  ──────────────
t0                                                      [urgent.pdf]
        Opção 3 (iniciar)
        submit worker
t1                              run() iniciado
                                poll() → urgent.pdf
                                começar impressão
t2      Opção 1 (adicionar)
        novo.pdf (pouco_urg)
        offer()
                                ──────────────→         [novo.pdf]
                                imprimindo página 1
t3                              .pagina_restante--
                                logProgress()
                                
t4      (continua menu)         .pagina_restante--
        Opção 2 (listar fila)   imprimindo página 2
        ───────────────────→
                     ← mostra [novo.pdf] na fila
t5                              arquivo pronto
                                poll() → novo.pdf
                                começar impressão
                                
t6      Opção 4 (parar)         (respondendo a parada)
        .parar()                rodando = false
                                sai do loop
                                ───────→ final
```

---

## 🎯 Pontos Críticos

### 1. **Comparação (Prioridade + FIFO)**

```java
@Override
public int compareTo(Arquivo outro) {
    // Primeiro por prioridade (menor = maior)
    int comp = Integer.compare(
        this.prioridade.getNivel(),
        outro.prioridade.getNivel()
    );
    
    // Se empate, por timestamp (FIFO)
    if (comp == 0) {
        return this.dataAdicao.compareTo(outro.dataAdicao);
    }
    
    return comp;
}
```

**Garantias:**
- URGENTE(1) sempre primeiro
- Dentro de URGENTE: FIFO
- Depois POUCO_URGENTE(2)
- Depois COMUM(3)

### 2. **Parada Segura**

```java
public void pararImpressora() {
    worker.parar();  // Set rodando = false
    
    // Aguarda thread terminar (até 10s)
    if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
        executorService.shutdownNow();  // Força se timeout
    }
}
```

**Garantias:**
- Arquivo atual termina antes de parar
- InterruptedException trata interrupções
- Timeout evita travamento

### 3. **Tratamento de Exceções**

```java
try {
    while (rodando || !fila.isEmpty()) {
        arquivo = fila.poll(1, TimeUnit.SECONDS);  // Timeout
        if (arquivo != null) {
            imprimirArquivo();
        }
    }
} catch (InterruptedException e) {
    Logger.aviso("Interrompido");
    Thread.currentThread().interrupt();  // Re-propaga
    break;
}
```

---

## 📈 Complexidade

### Temporal

| Operação | Complexidade | Razão |
|----------|-------------|-------|
| Adicionar arquivo | O(log n) | PriorityQueue mantém heap |
| Retirar arquivo | O(log n) | Remove root do heap |
| Listar fila | O(n) | Copia todos elementos |
| Calcular tempo | O(n) | Itera fila |

### Especial

| Operação | Comportamento |
|----------|--------------|
| Impressão | Linear: 500ms por página |
| Sincronização | Lock-free reads (volatile), locked writes |
| Memória | O(n) para n arquivos |

---

## 🔧 Configurable Parameters

### ImpressoraWorker.java

```java
// Taxa de impressão (em milissegundos por página)
private static final long TEMPO_POR_PAGINA_MS = 500;
// Alterar para:
// 1000 = 1 página/segundo
// 250 = 4 páginas/segundo
// 100 = 10 páginas/segundo
```

### ImpressoraService.java

```java
// Timeout para parada segura
if (!executorService.awaitTermination(10, TimeUnit.SECONDS))
// Alterar 10 para outro valor em segundos
```

### TesteSistemaImpressora.java

```java
// Tempos de sleep nos testes
Thread.sleep(3500);  // Ajustar conforme TEMPO_POR_PAGINA_MS
```

---

## 🛡️ Validações

### Arquivo (Construtor)

```java
this.nome = Objects.requireNonNull(nome, "Nome não pode ser nulo");
this.prioridade = Objects.requireNonNull(prioridade, "Prioridade não pode ser nula");
```

### ImpressoraService (Adicionar)

```java
if (quantidadePaginas <= 0) {
    Logger.erro("Número de páginas deve ser maior que 0");
    return false;
}
```

### Main (Input)

```java
try {
    paginas = Integer.parseInt(scanner.nextLine().trim());
} catch (NumberFormatException e) {
    Logger.erro("Número de páginas inválido");
    return;
}
```

---

**Arquitetura robusta, escalável e fácil de manter! 🏆**
