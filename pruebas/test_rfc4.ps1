$baseUrl = "http://localhost:8080/AlpesCab"

# Parámetros de ejemplo (coinciden con los seeds en src/main/resources/SQL/datos1.sql)
$ciudad = 'Bogotá'
$fechaInicio = '2025-09-20 00:00:00'
$fechaFin = '2025-09-30 23:59:59'

# Construir URI con escape de parámetros
$params = @{ ciudad = $ciudad; fechaInicio = $fechaInicio; fechaFin = $fechaFin }
$query = ($params.GetEnumerator() | ForEach-Object { [System.Uri]::EscapeDataString($_.Key) + '=' + [System.Uri]::EscapeDataString($_.Value) }) -join '&'
$uri = "$baseUrl/api/rfcs/rfc4?$query"

Write-Host "Consulta RFC4 -> $uri"

try {
    $resp = Invoke-RestMethod -Uri $uri -Method Get -ErrorAction Stop
    $json = $resp | ConvertTo-Json -Depth 6
    Write-Host "--- RFC4 respuesta (parcial) ---"
    # Mostrar conteo y primer elemento si es arreglo
    if ($resp -is [System.Array]) {
        Write-Host "Elementos encontrados: $($resp.Length)"
        if ($resp.Length -gt 0) {
            $first = $resp[0] | ConvertTo-Json -Depth 6
            Write-Host "Primer elemento:"
            Write-Host $first
        }
    } else {
        Write-Host $json
    }
} catch {
    Write-Host "Error al llamar RFC4: $_"
}
