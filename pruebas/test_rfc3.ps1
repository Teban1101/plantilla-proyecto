$baseUrl = "http://localhost:8080/AlpesCab"

# RFC3: ingresos por vehículo / conductor
$uri = "$baseUrl/api/rfcs/rfc3"
Write-Host "Consulta RFC3 -> $uri"

try {
    $resp = Invoke-RestMethod -Uri $uri -Method Get -ErrorAction Stop
    if ($resp -is [System.Array]) {
        Write-Host "HTTP 200 - Elementos: $($resp.Length)"
        if ($resp.Length -gt 0) {
            Write-Host "Primer elemento (resumen):"
            $resp[0] | ConvertTo-Json -Depth 6 | Write-Host
        }
    } else {
        $resp | ConvertTo-Json -Depth 6 | Write-Host
    }
} catch {
    Write-Host "Error al llamar RFC3: $_"
}
