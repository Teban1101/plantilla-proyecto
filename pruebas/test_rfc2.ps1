$baseUrl = "http://localhost:8080/AlpesCab"

# RFC2: top 20 conductores con más servicios
$uri = "$baseUrl/api/rfcs/rfc2"
Write-Host "Consulta RFC2 -> $uri"

try {
    $resp = Invoke-RestMethod -Uri $uri -Method Get -ErrorAction Stop
    if ($resp -is [System.Array]) {
        Write-Host "HTTP 200 - Elementos: $($resp.Length)"
        if ($resp.Length -gt 0) {
            # Mostrar hasta 20 resultados completos
            $countToShow = [Math]::Min(20, $resp.Length)
            Write-Host "Mostrando los primeros $countToShow resultados (JSON):"
            $toShow = if ($resp.Length -le 20) { $resp } else { $resp[0..($countToShow-1)] }
            $toShow | ConvertTo-Json -Depth 6 | Write-Host
        }
    } else {
        $resp | ConvertTo-Json -Depth 6 | Write-Host
    }
} catch {
    Write-Host "Error al llamar RFC2: $_"
}
