# 🚀 Guia Rápido - Sistema de Impressora Java

## Início Rápido em 3 Passos

### 1️⃣ **Compilar o Projeto**

**Windows (PowerShell):**
```powershell
.\compile_and_run.ps1
```

**Windows (CMD):**
```cmd
compile_and_run.bat
```

**Linux/Mac:**
```bash
javac -d bin src/br/com/impressora/model/*.java \
              src/br/com/impressora/util/*.java \
              src/br/com/impressora/thread/*.java \
              src/br/com/impressora/service/*.java \
              src/br/com/impressora/main/*.java
```

### 2️⃣ **Executar Aplicação Interativa**

**Windows:**
```cmd
java -cp bin br.com.impressora.main.Main
```

**Linux/Mac:**
```bash
java -cp bin br.com.impressora.main.Main
```

### 3️⃣ **Executar Testes**

```bash
java -cp bin br.com.impressora.test.TesteSistemaImpressora
```

---

## 📝 Exemplo de Uso Completo

### Cenário: Imprimir 3 documentos com prioridades diferentes

```
[Menu Principal]
Escolha uma opção: 1

[Adicionar Novo Arquivo]
Nome do arquivo: contrato.pdf
Número de páginas: 10
Escolha a prioridade:
1 - Urgente
2 - Pouco Urgente
3 - Comum
Prioridade: 1

Deseja iniciar a impressora agora? (s/n): s
✓ Impressora iniciada com sucesso

[Menu Principal]
Escolha uma opção: 1

[Adicionar Novo Arquivo]
Nome do arquivo: relatorio.xlsx
Número de páginas: 8
Prioridade: 3

[Menu Principal]
Escolha uma opção: 1

[Adicionar Novo Arquivo]
Nome do arquivo: informe.docx
Número de páginas: 5
Prioridade: 2

[Menu Principal]
Escolha uma opção: 5   # Ver Status
```

**Saída esperada:**
```
════════════════════════════════════════════════════════════════════════════════
  STATUS DA IMPRESSORA
════════════════════════════════════════════════════════════════════════════════
Estado: EM EXECUÇÃO
Arquivos na fila: 2

>>> Arquivo em impressão:
    Nome: contrato.pdf
    Prioridade: Urgente
    Progresso: 7/10 páginas
    Tempo estimado: ~1s

>>> Próximos arquivos na fila:
    • informe.docx | Prioridade: Pouco Urgente | Páginas: 0/5
    • relatorio.xlsx | Prioridade: Comum | Páginas: 0/8

Tempo estimado total da fila: 6s (6s)
════════════════════════════════════════════════════════════════════════════════
```

---

## 🎮 Funções do Menu

| Opção | Descrição |
|-------|-----------|
| **1** | Adiciona novo arquivo à fila |
| **2** | Lista todos os arquivos na fila |
| **3** | Inicia o processamento |
| **4** | Para a impressora |
| **5** | Mostra status detalhado |
| **6** | Limpa a tela |
| **0** | Encerra a aplicação |

---

## 💡 Exemplos Práticos

### Exemplo 1: Impressão Simples

```
Opção: 1
Nome: documento.pdf
Páginas: 5
Prioridade: 3 (Comum)
Iniciar? s

[Aguarda 2,5 segundos para 5 páginas a 2 páginas/s]
✓ Impressão concluída
```

### Exemplo 2: Múltiplas Prioridades

```
Adiciona:
  • urgent.txt (3 pág) - Prioridade 1 (Urgente)
  • report.pdf (6 pág) - Prioridade 3 (Comum)
  • notice.doc (4 pág) - Prioridade 2 (Pouco Urgente)

Ordem de impressão:
  1. urgent.txt (1,5s)
  2. notice.doc (2s)
  3. report.pdf (3s)
  Total: 6,5s
```

### Exemplo 3: Adição Durante Impressão

```
Inicia impressora com 1 arquivo
Após 1 segundo, adiciona novo arquivo
A nova fila é reprocessada com prioridades respeitadas
```

---

## 🔧 Arquitetura Resumida

```
┌──────────────────────────────────────┐
│       Thread Principal (Interface)    │
│           Main.java                  │
└────────────┬─────────────────────────┘
             │
             ↓
┌──────────────────────────────────────┐
│   ImpressoraService (Lógica)         │
│  • Gerencia fila                     │
│  • Controla thread worker           │
│  • Calcula tempos                   │
└────────────┬─────────────────────────┘
             │
             ↓
  ┌─────────────────────────┐
  │ PriorityBlockingQueue   │
  │ [Arquivo1, Arquivo2...] │
  │ (thread-safe)           │
  └─────────────┬───────────┘
                │
                ↓
        ┌───────────────────┐
        │ ImpressoraWorker  │
        │ (Thread Separada) │
        │ • Consome fila    │
        │ • Imprime 2 pág/s │
        │ • Registra logs   │
        └───────────────────┘
```

---

## 📊 Performance Esperada

| Operação | Tempo |
|----------|-------|
| Compilação | < 2s |
| Startup | < 100ms |
| Adicionar arquivo | ~5ms |
| Iniciar impressed | ~10ms |
| Processamento | 500ms por página |

---

## 🧪 Testes Inclusos

O sistema inclui 7 testes que validam:

1. ✅ Criação do serviço
2. ✅ Adição de arquivos
3. ✅ Ordenação por prioridade (FIFO)
4. ✅ Cálculo de tempo estimado
5. ✅ Processamento concorrente
6. ✅ Adição durante impressão (thread-safety)
7. ✅ Parada segura

**Executar testes:**
```bash
java -cp bin br.com.impressora.test.TesteSistemaImpressora
```

---

## 🐛 Troubleshooting

### Erro: "javac: command not found"
- Instale o JDK 17+
- Configure a variável de ambiente JAVA_HOME

### Erro: "Class not found"
- Verifique se compilou com `-d bin`
- Execute `java -cp bin br.com.impressora.main.Main`

### A impressora não processa
- Certifique-se de adicionar um arquivo antes de iniciar
- Use menu opção 2 para verificar fila

### Caracteres estranhos no console
- Windows: use PowerShell em vez de CMD
- Defina encoding UTF-8 no console

---

## 📚 Referência de Código

**Adicionar arquivo programaticamente:**
```java
ImpressoraService service = new ImpressoraService();
service.adicionarArquivo("documento.pdf", 10, Prioridade.URGENTE);
service.iniciarImpressora();

// Aguarda conclusão
Thread.sleep(5000);

service.encerrar();
```

**Obter status atual:**
```java
long tempoRestante = service.calcularTempoEstimadoTotal();
int arquivosFila = service.getTamanhoFila();
Arquivo atual = service.getArquivoAtual();
```

---

## 📝 Notas Importantes

- ⚠️ O sistema simula impressão em tempo real (não imprime realmente)
- ⚠️ 2 páginas/segundo é fixo (editar ImpressoraWorker para alterar)
- ⚠️ A fila é respeita FIFO para mesma prioridade
- ⚠️ Thread-safe para múltiplas adições simultâneas
- ⚠️ Parada segura aguarda arquivo atual terminar

---

## 🎓 Conceitos Aprendidos

- Threads em Java
- PriorityQueue e sincronização
- ExecutorService
- Comparable e ordenação customizada
- Tratamento de InterruptedException
- Logging formatado
- Arquitetura em camadas

---

**Dúvidas? Consulte README.md para documentação completa!**
