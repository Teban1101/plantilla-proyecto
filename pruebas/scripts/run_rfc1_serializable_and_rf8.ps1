$job = Start-Job -ScriptBlock { Invoke-RestMethod -Uri 'http://localhost:8080/AlpesCab/api/rfc1/transactional/serializable?clienteId=7777' -Method GET -TimeoutSec 120 }
Start-Sleep -Seconds 5
$punto = Invoke-RestMethod -Uri 'http://localhost:8080/AlpesCab/api/puntos' -Method POST -ContentType 'application/json' -Body (@{ nombre='PuntoRF4'; direccion='Calle RF4'; coordenadas='-74.08,4.63'; ciudad='PruebaCiudad' } | ConvertTo-Json)
$origenId = $punto.idPuntoGeografico
if (-not $origenId) { $origenId = $punto.id }
$body = @{ tipoServicio='Estandar'; clienteId=7777; origenId=[int]$origenId; distanciaKm=3.5 } | ConvertTo-Json
Invoke-RestMethod -Uri 'http://localhost:8080/AlpesCab/api/servicios' -Method POST -ContentType 'application/json' -Body $body
Receive-Job -Job $job -Wait -AutoRemoveJob | ConvertTo-Json -Depth 5
