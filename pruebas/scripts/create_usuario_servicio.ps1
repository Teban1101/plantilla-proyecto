$u = @{ 
    documentoUsuario = 7777
    numeroTarjeta = '0000'
    nombreTarjeta = 'Auto'
    fechaVencimiento = (Get-Date).AddYears(2)
    codigoSeguridad = 123
}
$json = $u | ConvertTo-Json
Invoke-RestMethod -Uri 'http://localhost:8080/AlpesCab/api/usuarioservicios' -Method Post -ContentType 'application/json' -Body $json | ConvertTo-Json -Depth 5
