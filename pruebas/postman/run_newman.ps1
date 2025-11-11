Param(
    [string]$collection = '.\pruebas\postman\AlpesCab-RF-collection.json',
    [string]$environment = '.\pruebas\postman\AlpesCab-Environment.json',
    [string]$outputJson = '.\pruebas\outputs\postman_results.json'
)

if (-not (Test-Path $collection)) { Write-Error "Colección no encontrada: $collection"; exit 2 }
if (-not (Test-Path $environment)) { Write-Error "Environment no encontrado: $environment"; exit 2 }
if (-not (Test-Path '.\pruebas\outputs')) { New-Item -ItemType Directory -Path '.\pruebas\outputs' | Out-Null }

Write-Host "Ejecutando Newman con colección: $collection"

# Ejecutar with npx (permitir auto-install con -y). Esto requiere Node.js/npm instalado.
$collectionPath = (Resolve-Path $collection).ProviderPath
$environmentPath = (Resolve-Path $environment).ProviderPath
$outputDir = (Resolve-Path (Split-Path -Parent $outputJson)).ProviderPath
$outputFile = Join-Path $outputDir (Split-Path -Leaf $outputJson)

$cmd = @('npx','-y','newman','run', $collectionPath, '-e', $environmentPath, '--reporters', 'cli,json', '--reporter-json-export', $outputFile)

Write-Host "Comando: $($cmd -join ' ')"

try {
    $proc = Start-Process -FilePath $cmd[0] -ArgumentList $cmd[1..($cmd.Length-1)] -NoNewWindow -Wait -PassThru -ErrorAction Stop
    if ($proc.ExitCode -eq 0) {
        Write-Host "Newman finalizó correctamente. Resultado JSON: $outputJson"
        Get-Content $outputJson -Raw | Out-Host
        exit 0
    } else {
        Write-Error "Newman terminó con código de salida $($proc.ExitCode)"
        exit $proc.ExitCode
    }
} catch {
    Write-Error "Error ejecutando newman: $_"
    Write-Host "Asegúrate de tener Node.js y npm instalados. Alternativa: instalar newman globalmente con 'npm install -g newman' o usar 'npx newman run ...'"
    exit 3
}
