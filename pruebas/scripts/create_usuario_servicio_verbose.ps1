try {
    $u = @{ 
        documentoUsuario = 7777
        numeroTarjeta = '0000'
        nombreTarjeta = 'Auto'
        fechaVencimiento = (Get-Date).AddYears(2)
        codigoSeguridad = 123
    }
    $json = $u | ConvertTo-Json
    Invoke-RestMethod -Uri 'http://localhost:8080/AlpesCab/api/usuarioservicios' -Method Post -ContentType 'application/json' -Body $json -ErrorAction Stop | ConvertTo-Json -Depth 5
} catch {
    Write-Host "Request failed. Details:"
    if ($_.Exception.Response -ne $null) {
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        Write-Host $reader.ReadToEnd()
    } else {
        $_ | Out-Host
    }
}
