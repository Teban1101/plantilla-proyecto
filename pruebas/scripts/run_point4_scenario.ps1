$base = 'http://localhost:8080/AlpesCab'
Write-Host "Starting RFC1(serializable) background job..."
$job = Start-Job -ScriptBlock {
    param($base)
    try {
        $res = Invoke-RestMethod -Uri "$base/api/rfc1/transactional/serializable?clienteId=7777" -Method Get -TimeoutSec 600
        $res | ConvertTo-Json -Depth 10 | Out-File -FilePath "./rfc1_serializable_result.json" -Encoding utf8
    } catch {
        $_ | Out-String | Out-File "./rfc1_serializable_error.txt" -Encoding utf8
    }
} -ArgumentList $base

Start-Sleep -Seconds 5
Write-Host "Creating punto..."
$p = @{ nombre = 'PuntoRF8'; direccion = 'Calle RF8'; coordenadas = '-74.08,4.63'; ciudad = 'PruebaCiudad' }
try {
    $pResp = Invoke-RestMethod -Uri "$base/api/puntos" -Method Post -ContentType 'application/json' -Body ($p | ConvertTo-Json)
    Write-Host "Punto created:" ($pResp | ConvertTo-Json -Depth 5)
    $id = $pResp.idPuntoGeografico
} catch {
    Write-Host "Error creating punto:" $_
    if ($_.Exception.Response -ne $null) {
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        Write-Host $reader.ReadToEnd()
    }
    exit 1
}

# ensure UsuarioServicio exists to satisfy FK from SERVICIO
Write-Host "Ensuring UsuarioServicio for clienteId 7777 exists..."
$us = @{ documentoUsuario = 7777; numeroTarjeta = '0000'; nombreTarjeta = 'Test'; codigoSeguridad = 123 }
try {
    $uResp = Invoke-RestMethod -Uri "$base/api/usuarioservicios" -Method Post -ContentType 'application/json' -Body ($us | ConvertTo-Json)
    Write-Host "UsuarioServicio created:" ($uResp | ConvertTo-Json -Depth 5)
} catch {
    Write-Host "Error creating UsuarioServicio (it may already exist):" $_
}

Start-Sleep -Seconds 1
Write-Host "Creating servicio pointing to punto id $id..."
$s = @{ tipoServicio = 'Estandar'; clienteId = 7777; origenId = $id; distanciaKm = 3.5 }
try {
    $sResp = Invoke-RestMethod -Uri "$base/api/servicios" -Method Post -ContentType 'application/json' -Body ($s | ConvertTo-Json)
    Write-Host "Servicio response:" ($sResp | ConvertTo-Json -Depth 10)
} catch {
    Write-Host "Error creating servicio:"
    if ($_.Exception.Response -ne $null) {
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        Write-Host $reader.ReadToEnd()
    } else {
        Write-Host $_
    }
}

Write-Host "Waiting for RFC1 job to finish..."
Wait-Job $job
Write-Host "RFC1 job finished. Dumping results (if present):"
if (Test-Path ./rfc1_serializable_result.json) {
    Get-Content ./rfc1_serializable_result.json | Write-Host
} elseif (Test-Path ./rfc1_serializable_error.txt) {
    Get-Content ./rfc1_serializable_error.txt | Write-Host
} else {
    Write-Host "No RFC1 result files found."
}

# cleanup
Remove-Item ./rfc1_serializable_result.json -ErrorAction SilentlyContinue
Remove-Item ./rfc1_serializable_error.txt -ErrorAction SilentlyContinue
