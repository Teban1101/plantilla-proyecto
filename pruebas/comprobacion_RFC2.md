# Comprobación RFC2

Comprobación para RFC2 (top 20 conductores con más servicios).

Comando exacto a ejecutar (PowerShell desde la raíz del proyecto):

```powershell
.\pruebas\test_rfc2.ps1
```

Descripción: El script llama a `/api/rfcs/rfc2` y muestra el conteo y los primeros 20 elementos completos.

Salida esperada: Un arreglo JSON con hasta 20 objetos (conductorId, nombreConductor, correo, celular, numServicios). El script imprime los primeros 20 resultados (o menos si la respuesta tiene menos de 20 elementos).

Ejemplo (salida capturada en este entorno — se muestran los primeros elementos del listado de 20):

```
HTTP 200 - Elementos: 20
Mostrando los primeros 20 resultados (JSON):
[
	{
		"conductorId": 1038,
		"nombreConductor": "Conductor 1038",
		"correo": "conductor1038@mail.com",
		"celular": "3001038",
		"numServicios": 24
	},
	{
		"conductorId": 1039,
		"nombreConductor": "Conductor 1039",
		"correo": "conductor1039@mail.com",
		"celular": "3001039",
		"numServicios": 23
	},
	... (hasta 20 objetos)
]
```
Comando: powershell -NoProfile -ExecutionPolicy Bypass -File .\pruebas\test_rfc2.ps1