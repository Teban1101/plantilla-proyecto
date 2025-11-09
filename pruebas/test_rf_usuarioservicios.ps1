$baseUrl = "http://localhost:8080/AlpesCab"
$uri = "$baseUrl/api/usuarioservicios"
Write-Host "Consulta RF - usuarioservicios -> $uri"
try {
    $resp = Invoke-RestMethod -Uri $uri -Method Get -ErrorAction Stop
    if ($resp -is [System.Array]) {
        Write-Host "HTTP 200 - Elementos: $($resp.Length)"
        if ($resp.Length -gt 0) { $resp[0..([Math]::Min(4,$resp.Length-1))] | ConvertTo-Json -Depth 6 | Write-Host }
    } else { $resp | ConvertTo-Json -Depth 6 | Write-Host }
} catch { Write-Host "Error al llamar $uri : $_" }
