#!/usr/bin/env pwsh
# ============================================================================
# Script PowerShell para compilação e execução do projeto Impressora Java
# ============================================================================

Write-Host ""
Write-Host "════════════════════════════════════════════════════════════════════════════════"
Write-Host "  SISTEMA DE GERENCIAMENTO DE IMPRESSORA JAVA"
Write-Host "════════════════════════════════════════════════════════════════════════════════"
Write-Host ""

$ProjectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$SrcDir = Join-Path $ProjectDir "src"
$BinDir = Join-Path $ProjectDir "bin"

# Verifica se javac está disponível
try {
    javac -version *>$null
} catch {
    Write-Host "[ERRO] Java Development Kit (JDK) não encontrado!" -ForegroundColor Red
    Write-Host "Certifique-se de que o JDK 17+ está instalado e configurado no PATH."
    Read-Host "Pressione qualquer tecla para sair"
    exit 1
}

Write-Host "[INFO] Diretório do projeto: $ProjectDir" -ForegroundColor Cyan
Write-Host "[INFO] Compilando projeto..." -ForegroundColor Cyan
Write-Host ""

# Cria diretório bin se não existir
if (-not (Test-Path $BinDir)) {
    New-Item -ItemType Directory -Path $BinDir > $null
}

# Compila todas as classes
javac -d $BinDir `
    "$SrcDir\br\com\impressora\model\*.java" `
    "$SrcDir\br\com\impressora\util\*.java" `
    "$SrcDir\br\com\impressora\thread\*.java" `
    "$SrcDir\br\com\impressora\service\*.java" `
    "$SrcDir\br\com\impressora\main\*.java" `
    "$SrcDir\br\com\impressora\test\*.java"

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "[ERRO] Erro ao compilar o projeto!" -ForegroundColor Red
    Read-Host "Pressione qualquer tecla para sair"
    exit 1
}

Write-Host ""
Write-Host "[SUCESSO] Compilação concluída com sucesso!" -ForegroundColor Green
Write-Host ""
Write-Host "════════════════════════════════════════════════════════════════════════════════"
Write-Host "  OPÇÕES DE EXECUÇÃO"
Write-Host "════════════════════════════════════════════════════════════════════════════════"
Write-Host ""
Write-Host "1 - Executar aplicação interativa" -ForegroundColor Cyan
Write-Host "2 - Executar testes" -ForegroundColor Cyan
Write-Host "3 - Apenas compilar" -ForegroundColor Cyan
Write-Host "0 - Sair" -ForegroundColor Cyan
Write-Host ""

$opcao = Read-Host "Escolha uma opção"

switch ($opcao) {
    "1" {
        Write-Host ""
        Write-Host "Iniciando aplicação..." -ForegroundColor Green
        Write-Host ""
        java -cp $BinDir br.com.impressora.main.Main
    }
    "2" {
        Write-Host ""
        Write-Host "Executando testes..." -ForegroundColor Green
        Write-Host ""
        java -cp $BinDir br.com.impressora.test.TesteSistemaImpressora
    }
    "3" {
        Write-Host ""
        Write-Host "Compilação realizada com sucesso!" -ForegroundColor Green
        Write-Host "Diretório bin: $BinDir" -ForegroundColor Cyan
    }
    "0" {
        Write-Host ""
        Write-Host "Saindo..." -ForegroundColor Yellow
        exit 0
    }
    default {
        Write-Host ""
        Write-Host "[ERRO] Opção inválida!" -ForegroundColor Red
    }
}

Write-Host ""
Read-Host "Pressione qualquer tecla para sair"
