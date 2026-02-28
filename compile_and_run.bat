@echo off
REM ============================================================================
REM Script de compilação e execução do projeto Impressora Java
REM ============================================================================

echo.
echo ════════════════════════════════════════════════════════════════════════════════
echo  SISTEMA DE GERENCIAMENTO DE IMPRESSORA JAVA
echo ════════════════════════════════════════════════════════════════════════════════
echo.

set PROJECT_DIR=%~dp0
set SRC_DIR=%PROJECT_DIR%src
set BIN_DIR=%PROJECT_DIR%bin

REM Verifica se javac está disponível
javac -version >nul 2>&1
if errorlevel 1 (
    echo [ERRO] Java Development Kit (JDK) não encontrado!
    echo Certifique-se de que o JDK 17+ está instalado e configurado no PATH.
    echo.
    pause
    exit /b 1
)

echo [INFO] Diretório do projeto: %PROJECT_DIR%
echo [INFO] Compilando projeto...
echo.

REM Cria diretório bin se não existir
if not exist "%BIN_DIR%" mkdir "%BIN_DIR%"

REM Compila todas as classes
javac -d "%BIN_DIR%" ^
    "%SRC_DIR%\br\com\impressora\model\*.java" ^
    "%SRC_DIR%\br\com\impressora\util\*.java" ^
    "%SRC_DIR%\br\com\impressora\thread\*.java" ^
    "%SRC_DIR%\br\com\impressora\service\*.java" ^
    "%SRC_DIR%\br\com\impressora\main\*.java" ^
    "%SRC_DIR%\br\com\impressora\test\*.java"

if errorlevel 1 (
    echo.
    echo [ERRO] Erro ao compilar o projeto!
    pause
    exit /b 1
)

echo.
echo [SUCESSO] Compilação concluída com sucesso!
echo.
echo ════════════════════════════════════════════════════════════════════════════════
echo  OPÇÕES DE EXECUÇÃO
echo ════════════════════════════════════════════════════════════════════════════════
echo.
echo 1 - Executar aplicação interativa
echo 2 - Executar testes
echo 3 - Apenas compilar
echo 0 - Sair
echo.
set /p opcao=Escolha uma opção: 

if "%opcao%"=="1" (
    echo.
    echo Iniciando aplicação...
    echo.
    java -cp "%BIN_DIR%" br.com.impressora.main.Main
) else if "%opcao%"=="2" (
    echo.
    echo Executando testes...
    echo.
    java -cp "%BIN_DIR%" br.com.impressora.test.TesteSistemaImpressora
) else if "%opcao%"=="3" (
    echo.
    echo Compilação realizada com sucesso!
    echo Diretório bin: %BIN_DIR%
) else if "%opcao%"=="0" (
    echo.
    echo Saindo...
    exit /b 0
) else (
    echo.
    echo [ERRO] Opção inválida!
)

echo.
pause
