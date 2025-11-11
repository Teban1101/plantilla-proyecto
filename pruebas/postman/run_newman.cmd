@echo off
REM Ejecuta la colección Postman con newman (requiere newman instalado globalmente o en PATH)
SET COLLECTION=pruebas\postman\AlpesCab-RF-collection.json
SET ENV=pruebas\postman\AlpesCab-Environment.json
SET OUTPUT=pruebas\outputs\postman_results.json

if not exist "%~dp0%OUTPUT%" (
  if not exist "%~dp0pruebas\outputs" mkdir "%~dp0pruebas\outputs"
)

echo Ejecutando newman...
newman run "%COLLECTION%" -e "%ENV%" --reporters cli,json --reporter-json-export "%OUTPUT%"
if %ERRORLEVEL% neq 0 (
  echo Newman falló con código %ERRORLEVEL%.
  exit /b %ERRORLEVEL%
)
echo Newman finalizó. Resultado en %OUTPUT%
type "%OUTPUT%"
