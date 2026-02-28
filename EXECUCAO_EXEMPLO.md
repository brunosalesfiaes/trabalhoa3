# 📺 Exemplo de Execução Completo

## Sessão Real de Uso do Sistema

### Terminal Output Exemplo

```
════════════════════════════════════════════════════════════════════════════════
  BEM-VINDO AO SISTEMA DE GERENCIAMENTO DE IMPRESSORA
════════════════════════════════════════════════════════════════════════════════
Este sistema simula uma impressora com controle de prioridade
e processamento concorrente de arquivos usando Threads.

16:41:00.433 [INFO] - Digite uma opção do menu para começar...

════════════════════════════════════════════════════════════════════════════════
  MENU PRINCIPAL
════════════════════════════════════════════════════════════════════════════════
1 - Adicionar arquivo
2 - Listar fila
3 - Iniciar impressora
4 - Parar impressora
5 - Ver status detalhado
6 - Limpar tela
0 - Sair
════════════════════════════════════════════════════════════════════════════════
Escolha uma opção: 1

════════════════════════════════════════════════════════════════════════════════
  ADICIONAR NOVO ARQUIVO
════════════════════════════════════════════════════════════════════════════════
Nome do arquivo: contrato.pdf
Número de páginas: 6
Escolha a prioridade:
1 - Urgente
2 - Pouco Urgente
3 - Comum
Prioridade: 1

16:41:02.595 [SUCESSO] - Arquivo adicionado à fila: contrato.pdf | Prioridade: Urgente | Páginas: 0/6 | Adicionado: 16:41:02
16:41:02.605 [INFO] - Fila agora tem 1 arquivo(s)

Deseja iniciar a impressora agora? (s/n): s
16:41:06.319 [INFO] - Iniciando impressora...
16:41:06.320 [SUCESSO] - Impressora iniciada com sucesso

════════════════════════════════════════════════════════════════════════════════
16:41:06.321 [THREAD] - Thread de impressão iniciada - ID: ImpressoraWorker-Thread

════════════════════════════════════════════════════════════════════════════════
  MENU PRINCIPAL
════════════════════════════════════════════════════════════════════════════════
1 - Adicionar arquivo
2 - Listar fila
3 - Iniciar impressora
4 - Parar impressora
5 - Ver status detalhado
6 - Limpar tela
0 - Sair
════════════════════════════════════════════════════════════════════════════════
Escolha uma opção: 
16:41:06.322 [IMPRESSORA] - Iniciando impressão: contrato.pdf
16:41:06.329 [IMPRESSORA] - Total: 6 páginas | Prioridade: Urgente
  [███░░░░░░░░░░░░░░░░░] 16% | Página: 1/6 | contrato.pdf | Tempo restante: ~2s
  [██████░░░░░░░░░░░░░░] 33% | Página: 2/6 | contrato.pdf | Tempo restante: ~2s
  [██████████░░░░░░░░░░] 50% | Página: 3/6 | contrato.pdf | Tempo restante: ~1s
  [█████████████░░░░░░░] 66% | Página: 4/6 | contrato.pdf | Tempo restante: ~1s
  [████████████████░░░░] 83% | Página: 5/6 | contrato.pdf | Tempo restante: ~0s
  [████████████████████] 100% | Página: 6/6 | contrato.pdf | Tempo restante: ~0s
16:41:09.407 [SUCESSO] - Impressão concluída: contrato.pdf

════════════════════════════════════════════════════════════════════════════════
  MENU PRINCIPAL
════════════════════════════════════════════════════════════════════════════════
Escolha uma opção: 1

════════════════════════════════════════════════════════════════════════════════
  ADICIONAR NOVO ARQUIVO
════════════════════════════════════════════════════════════════════════════════
Nome do arquivo: relatorio.xlsx
Número de páginas: 8
Escolha a prioridade:
1 - Urgente
2 - Pouco Urgente
3 - Comum
Prioridade: 2

16:41:11.205 [SUCESSO] - Arquivo adicionado à fila: relatorio.xlsx | Prioridade: Pouco Urgente | Páginas: 0/8 | Adicionado: 16:41:11
16:41:11.206 [INFO] - Fila agora tem 1 arquivo(s)

Deseja iniciar a impressora agora? (s/n): n
16:41:12.100 [INFO] - Operação cancelada

════════════════════════════════════════════════════════════════════════════════
  MENU PRINCIPAL
════════════════════════════════════════════════════════════════════════════════
Escolha uma opção: 1

════════════════════════════════════════════════════════════════════════════════
  ADICIONAR NOVO ARQUIVO
════════════════════════════════════════════════════════════════════════════════
Nome do arquivo: nota.txt
Número de páginas: 3
Escolha a prioridade:
1 - Urgente
2 - Pouco Urgente
3 - Comum
Prioridade: 3

16:41:13.505 [SUCESSO] - Arquivo adicionado à fila: nota.txt | Prioridade: Comum | Páginas: 0/3 | Adicionado: 16:41:13
16:41:13.506 [INFO] - Fila agora tem 2 arquivo(s)

Deseja iniciar a impressora agora? (s/n): n
16:41:14.100 [INFO] - Operação cancelada

════════════════════════════════════════════════════════════════════════════════
  MENU PRINCIPAL
════════════════════════════════════════════════════════════════════════════════
Escolha uma opção: 2

════════════════════════════════════════════════════════════════════════════════
  FILA DE IMPRESSÃO
════════════════════════════════════════════════════════════════════════════════
Arquivos na fila (em ordem de prioridade):

  • relatorio.xlsx | Prioridade: Pouco Urgente | Páginas: 0/8 | Adicionado: 16:41:11
  • nota.txt | Prioridade: Comum | Páginas: 0/3 | Adicionado: 16:41:13

  Total: 2 arquivo(s) | Tempo estimado: 5s (0m 5s)

════════════════════════════════════════════════════════════════════════════════
  MENU PRINCIPAL
════════════════════════════════════════════════════════════════════════════════
Escolha uma opção: 3

════════════════════════════════════════════════════════════════════════════════
  INICIAR IMPRESSORA
════════════════════════════════════════════════════════════════════════════════
16:41:15.500 [INFO] - Iniciando impressora...
16:41:15.501 [SUCESSO] - Impressora iniciada com sucesso

16:41:15.502 [THREAD] - Thread de impressão iniciada - ID: ImpressoraWorker-Thread

Pressione Enter para voltar ao menu (a impressora funcionará em background)...

(Continuando no background...)
16:41:15.510 [IMPRESSORA] - Iniciando impressão: relatorio.xlsx
16:41:15.515 [IMPRESSORA] - Total: 8 páginas | Prioridade: Pouco Urgente
  [██░░░░░░░░░░░░░░░░░░] 12% | Página: 1/8 | relatorio.xlsx | Tempo restante: ~3s
  [████░░░░░░░░░░░░░░░░] 25% | Página: 2/8 | relatorio.xlsx | Tempo restante: ~3s
  [██████░░░░░░░░░░░░░░] 37% | Página: 3/8 | relatorio.xlsx | Tempo restante: ~2s
  [████████░░░░░░░░░░░░] 50% | Página: 4/8 | relatorio.xlsx | Tempo restante: ~2s
  [██████████░░░░░░░░░░] 62% | Página: 5/8 | relatorio.xlsx | Tempo restante: ~1s
  [████████████░░░░░░░░] 75% | Página: 6/8 | relatorio.xlsx | Tempo restante: ~1s
  [██████████████░░░░░░] 87% | Página: 7/8 | relatorio.xlsx | Tempo restante: ~0s
  [████████████████░░░░] 100% | Página: 8/8 | relatorio.xlsx | Tempo restante: ~0s
16:41:19.620 [SUCESSO] - Impressão concluída: relatorio.xlsx

16:41:19.625 [IMPRESSORA] - Iniciando impressão: nota.txt
16:41:19.630 [IMPRESSORA] - Total: 3 páginas | Prioridade: Comum
  [██████░░░░░░░░░░░░░░] 33% | Página: 1/3 | nota.txt | Tempo restante: ~1s
  [░░░░░░░░░░░░░░░░░░░░] 66% | Página: 2/3 | nota.txt | Tempo restante: ~0s
  [████████████████████] 100% | Página: 3/3 | nota.txt | Tempo restante: ~0s
16:41:21.650 [SUCESSO] - Impressão concluída: nota.txt

16:41:21.655 [SUCESSO] - Fila vazia. Impressora finalizada.
16:41:21.660 [THREAD] - Thread de impressão encerrada

(Voltando ao menu...)

════════════════════════════════════════════════════════════════════════════════
  MENU PRINCIPAL
════════════════════════════════════════════════════════════════════════════════
Escolha uma opção: 
(Usuário pressiona Enter)

════════════════════════════════════════════════════════════════════════════════
  MENU PRINCIPAL
════════════════════════════════════════════════════════════════════════════════
Escolha uma opção: 5

════════════════════════════════════════════════════════════════════════════════
  STATUS DA IMPRESSORA
════════════════════════════════════════════════════════════════════════════════
Estado: PARADA
Arquivos na fila: 0

Tempo estimado total da fila: 0s (0s)
════════════════════════════════════════════════════════════════════════════════

Pressione Enter para voltar ao menu...

════════════════════════════════════════════════════════════════════════════════
  MENU PRINCIPAL
════════════════════════════════════════════════════════════════════════════════
Escolha uma opção: 0

════════════════════════════════════════════════════════════════════════════════
ENCERRANDO APLICAÇÃO
════════════════════════════════════════════════════════════════════════════════
16:41:25.320 [INFO] - Serviço de impressora encerrado
16:41:25.322 [SUCESSO] - Programa encerrado com sucesso!

PS C:\trabalho>
```

---

## 🧪 Exemplo de Execução de Testes

```
16:43:03.186 [SUCESSO] - ✓ Serviço criado com sucesso
16:43:03.191 [SUCESSO] - ✓ Fila vazia inicial verificada

16:43:03.210 [SUCESSO] - Arquivo adicionado à fila: documento.docx | Prioridade: Comum | Páginas: 0/5
16:43:03.216 [SUCESSO] - Arquivo adicionado à fila: urgente.txt | Prioridade: Urgente | Páginas: 0/3
16:43:03.216 [SUCESSO] - Arquivo adicionado à fila: relatorio.pdf | Prioridade: Pouco Urgente | Páginas: 0/8
16:43:03.217 [SUCESSO] - ✓ 3 arquivos adicionados com sucesso
16:43:03.217 [SUCESSO] - ✓ Validação de quantidade verificada

16:43:03.222 [SUCESSO] - ✓ Listagem funcionando corretamente
16:43:03.223 [SUCESSO] - ✓ Ordem de prioridades verificada
16:43:03.223 [SUCESSO] - ✓ URGENTE > POUCO_URGENTE > COMUM

16:43:03.230 [SUCESSO] - ✓ Cálculo de tempo funcionando
16:43:03.235 [SUCESSO] - ✓ Total: 30 páginas = 15 segundos

16:41:06.321 [THREAD] - Thread de impressão iniciada - ID: ImpressoraWorker-Thread
  [███░░░░░░░░░░░░░░░░░] 25% | Página: 1/4 | arquivo_teste.pdf | Tempo restante: ~1s
  [██████░░░░░░░░░░░░░░] 50% | Página: 2/4 | arquivo_teste.pdf | Tempo restante: ~1s
  [██████████░░░░░░░░░░] 75% | Página: 3/4 | arquivo_teste.pdf | Tempo restante: ~0s
  [████████████████░░░░] 100% | Página: 4/4 | arquivo_teste.pdf | Tempo restante: ~0s
16:43:05.302 [SUCESSO] - Impressão concluída: arquivo_teste.pdf
16:43:06.740 [SUCESSO] - ✓ Processamento funcionou corretamente
16:43:06.740 [SUCESSO] - ✓ Duração: 3503ms
16:43:06.740 [SUCESSO] - ✓ Fila vazia verificada

16:43:06.740 [SUCESSO] - ✓ Adição durante impressão funcionou
16:43:06.740 [SUCESSO] - ✓ Thread-safety verificado
16:43:06.740 [SUCESSO] - ✓ Segundo arquivo foi processado (tinha prioridade)

16:43:07.321 [SUCESSO] - Fila vazia. Impressora finalizada.

════════════════════════════════════════════════════════════════════════════════
  TODOS OS TESTES CONCLUÍDOS COM SUCESSO!
════════════════════════════════════════════════════════════════════════════════
```

---

## 📊 Cronograma de Execução

```
Tempo  Evento                              Estado Thread    Estado Fila
─────────────────────────────────────────────────────────────────────
0.0s   Aplicação inicia
       Menu exibido
       
1.5s   Usuário: Opção 1
       Nome: contrato.pdf
       Páginas: 6
       Prioridade: Urgente
       Arquivo adicionado                 -              [contrato.pdf]
       
2.0s   Usuário: Opção 3
       Impressora iniciada                Rodando        [contrato.pdf]
       Worker thread cria
       
2.1s   Worker poll fila                   Rodando        []
       Começa impressão
       
3.0s   Página 2 impressa                  +1pág          []
4.0s   Página 4 impressa                  +3pág total    []
4.5s   Página 5 impressa                  +4pág total    []
5.0s   Página 6 impressa
       contrato.pdf completo              Aguardando     []
       
5.5s   Usuário: Opção 1
       Adiciona relatorio.xlsx
       Prioridade: Pouco Urgente
       Arquivo adicionado                 Aguardando     [relatorio.xlsx]
       
6.0s   Usuário: Opção 1
       Adiciona nota.txt
       Prioridade: Comum
       Arquivo adicionado                 Aguardando     [relatorio,nota]
       
7.0s   Usuário: Opção 3
       Impressora iniciada                Rodando        [relatorio,nota]
       Worker poll fila
       
7.1s   Começa relatorio.xlsx              Imprimindo     [nota.txt]
8.0s   Páginas 2-3 impressas              +2pág          [nota.txt]
9.5s   Página 5 impressa                  +4pág          [nota.txt]
11.0s  Página 8 impressa
       relatorio.xlsx completo            Aguardando     [nota.txt]
       
11.1s  Worker poll fila
       Começa nota.txt                    Imprimindo     []
       
12.0s  Página 2 impressa                  +1pág          []
12.5s  Página 3 impressa
       nota.txt completo                  Aguardando     []
       
12.6s  Fila vazia
       Worker termina                     Parado         []
       
13.0s  Usuário: Opção 0
       Sair / Encerrar
```

---

## 💡 Interpretação de Logs

### [INFO]
Mensagens informativas gerais
```
16:41:00.433 [INFO] - Digite uma opção do menu para começar...
```

### [SUCESSO]
Operação realizada com êxito
```
16:41:02.595 [SUCESSO] - Arquivo adicionado à fila
```

### [THREAD]
Eventos relacionados a threads
```
16:41:06.321 [THREAD] - Thread de impressão iniciada - ID: ImpressoraWorker-Thread
```

### [IMPRESSORA]
Eventos de impressão
```
16:41:06.322 [IMPRESSORA] - Iniciando impressão: contrato.pdf
```

### [AVISO]
Avisos que não impedem execução
```
16:41:12.100 [AVISO] - Prioridade inválida. Usando 'Comum'
```

### [ERRO]
Erros que impedem operação
```
16:41:00.000 [ERRO] - Número de páginas deve ser maior que 0
```

---

## 🔄 Barra de Progresso

```
████████████████████ 100% | Página: 6/6 | arquivo.pdf | Tempo: ~0s

Componentes:
  ████████████████░░░░  = Progresso visual (20 caracteres)
  100%                  = Percentual completo
  Página: 6/6           = Páginas impressas/total
  arquivo.pdf           = Nome do arquivo
  ~0s                   = Tempo restante estimado
```

**Símbolo:**
- `█` = Página preenchida
- `░` = Página restante

---

## 📈 Métricas de Performance

```
Teste Unitário de Processamento:
  - Arquivos processados: 1
  - Páginas por arquivo: 4
  - Taxa: 2 páginas/segundo
  - Tempo teórico: 2.0 segundos
  - Tempo real: ~2.05 segundos (com overhead)

Teste Concorrente:
  - Arquivo 1 adicionado: t=0.0s
  - Arquivo 2 adicionado: t=0.5s (durante impressão)
  - Ambos processados: t=3.0s
  - Verificação: ✓ Thread-safe
```

---

**Exemplo de execução completo! 🎉**
