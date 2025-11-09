# Comprobación RFC3

Comprobación para RFC3 (ingresos por vehículo y conductor).

Comando exacto a ejecutar (PowerShell desde la raíz del proyecto):

```powershell
.\pruebas\test_rfc3.ps1
```

Descripción: El script llama a `/api/rfcs/rfc3` y muestra el número de elementos y el primer elemento.

Salida esperada: Arreglo JSON con objetos que contienen conductorId, nombreConductor, placa, tipoServicio, ingresosBrutos e ingresosParaConductor.

Ejemplo (salida capturada en este entorno):

```
HTTP 200 - Elementos: 299
Primer elemento (resumen):
{
	"conductorId": 1001,
	"nombreConductor": "Conductor 1",
	"placa": "CAR1001",
	"tipoServicio": "Confort",
	"ingresosBrutos": 85575.0,
	"ingresosParaConductor": 51345.0
}
```
Comando: powershell -NoProfile -ExecutionPolicy Bypass -File .\pruebas\test_rfc3.ps1