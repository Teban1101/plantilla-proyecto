$baseUrl = "http://localhost:8080/AlpesCab"

# RFC1: histórico de servicios de un cliente
# Usar un cliente presente en los seeds: 2005 (o cualquier 2001..2200)
$clienteId = 2005

$uri = "$baseUrl/api/rfcs/rfc1?clienteId=$clienteId"
Write-Host "Consulta RFC1 -> $uri"

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
    Write-Host "Error al llamar RFC1: $_"
}
