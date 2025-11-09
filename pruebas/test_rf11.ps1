$baseUrl = "http://localhost:8080/AlpesCab"

$endpoints = @(
    "/api/usuarios",
    "/api/vehiculos",
    "/api/servicios"
)

foreach ($ep in $endpoints) {
    $uri = "$baseUrl$ep"
    Write-Host "\nLlamando: $uri"
    try {
        # Usamos Invoke-RestMethod para obtener objeto JSON deserializado
        $resp = Invoke-RestMethod -Uri $uri -Method Get -ErrorAction Stop
        if ($resp -is [System.Array]) {
            Write-Host "HTTP 200 - Elementos: $($resp.Length)"
            if ($resp.Length -gt 0) {
                Write-Host "Primer elemento (resumen):"
                $first = $resp[0] | ConvertTo-Json -Depth 5
                Write-Host $first
            }
        } else {
            Write-Host "HTTP 200 - Respuesta:
$($resp | ConvertTo-Json -Depth 5)"
        }
    } catch {
        Write-Host "Error llamando $uri : $_"
    }
}
