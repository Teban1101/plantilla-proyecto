# Postman collection - AlpesCab RF (11)

Instrucciones rápidas:

1. Abre Postman.
2. Importa `AlpesCab-RF-collection.json` (File -> Import).
3. Importa el environment `AlpesCab-Environment.json`.
4. Selecciona el environment "AlpesCab - Local".
5. Antes de ejecutar, arranca la aplicación Spring Boot con el contexto `/AlpesCab` en `http://localhost:8080`.
6. Ejecuta las requests (puedes correr toda la collection o ejecutar una por una). Cada request incluye un ejemplo de body cuando aplica.

Notas:
- Algunos endpoints PUT/complete usan IDs de ejemplo (p.ej. `4001`, `5977`). Si tu base de datos tiene otros IDs, actualiza la ruta en Postman antes de ejecutar.
- Si quieres que ejecute la colección aquí tras que arranques la app, dime y la ejecuto secuencialmente con Invoke-RestMethod para reproducir las mismas peticiones.

Newman (ejecución automática)
--------------------------------
Si prefieres ejecutar la colección desde la línea de comandos con Newman, hay un script PowerShell incluido:

 - `pruebas/postman/run_newman.ps1`

Requisitos:
- Node.js y npm instalados (https://nodejs.org/). Alternativa: usar `npx` que viene con npm.

Comandos exactos:

PowerShell (desde la raíz del proyecto):
```powershell
.\pruebas\postman\run_newman.ps1
```

cmd.exe (invoca PowerShell):
```cmd
powershell -NoProfile -ExecutionPolicy Bypass -File .\pruebas\postman\run_newman.ps1
```

El script usa `npx newman run <collection> -e <environment> --reporters cli,json --reporter-json-export <output>` y guardará el resultado JSON en `pruebas/outputs/postman_results.json`.

Si quieres que ejecute la colección desde aquí cuando levantes la app, arranca la app y dime "ejecuta newman"; yo correré el script y pegaré el resumen de resultados.
