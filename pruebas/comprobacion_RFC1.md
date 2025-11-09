# Comprobación RFC1

Comprobación para RFC1 (histórico de servicios por cliente).

Comando exacto a ejecutar (PowerShell desde la raíz del proyecto):

```powershell
.\pruebas\test_rfc1.ps1
```

Descripción: El script llama a `/api/rfcs/rfc1?clienteId=2005`. Si el cliente 2005 no existe en tu base, edita la variable `$clienteId` dentro del script.

Salida esperada: JSON con lista de servicios del cliente; el script muestra el número de elementos y el primer elemento.

Ejemplo (salida capturada en este entorno):

```
HTTP 200 - Elementos: 5
Primer elemento (resumen):
{
	"idServicio": 5524,
	"tipoServicio": "Confort",
	"clienteId": 2005,
	"nombreCliente": "Pasajero 2005",
	"conductorId": 1023,
	"conductorHabilitado": "1",
	"distanciaKm": 20.22,
	"costoPorKm": 1500.0,
	"costoTotal": 30330.0,
	"costoParaConductor": 18198.0,
	"horaInicio": "2025-11-11T10:15:11.36979",
	"horaFin": "2025-11-11T10:39:11.36979",
	"duracionMin": 24,
	"origenId": 3141,
	"nombreOrigen": "Punto 3141",
	"direccionOrigen": "Calle 3141 # 3241",
	"ciudadOrigen": "Medellín",
	"paradasDirecciones": "Calle 3086 # 3186 -> Calle 3050 # 3150"
}
```

Para correr: powershell -NoProfile -ExecutionPolicy Bypass -File .\pruebas\test_rfc1.ps1
