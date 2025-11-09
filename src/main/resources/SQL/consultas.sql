
-- ==================================================================
-- RFC1 - CONSULTAR EL HISTÓRICO DE TODOS LOS SERVICIOS PEDIDOS POR UN USUARIO
-- Descripción: Dado un usuario de servicios (cliente), mostrar la lista de servicios
-- con la información relevante: tarifa por km, costo total, costo para el conductor
-- (60% del valor), origen, y paradas concatenadas.
-- Parámetro de entrada: :cliente_id
-- Tablas usadas: SERVICIO, TARIFASERVICIO, USUARIO, USUARIOCONDUCTOR, PUNTOGEOGRAFICO, PARADA
-- Motivo de los joins: se necesita la tarifa para calcular el costo, datos del cliente
-- y conductor, la info del punto de origen y las paradas asociadas (agregado con LISTAGG).
-- ==================================================================

SELECT
    s.ID_SERVICIO,
    s.TIPO_SERVICIO,
    s.CLIENTE_ID,
    u.NOMBRE AS NOMBRE_CLIENTE,
    s.CONDUCTOR_ID,
    uc.HABILITADO AS CONDUCTOR_HABILITADO,
    s.DISTANCIA_KM,
    t.COSTO_POR_KM,
    (s.DISTANCIA_KM * t.COSTO_POR_KM) AS COSTO_TOTAL,
    ROUND(0.6 * (s.DISTANCIA_KM * t.COSTO_POR_KM), 2) AS COSTO_PARA_CONDUCTOR,
    s.HORA_INICIO,
    s.HORA_FIN,
    s.DURACION_MIN,
    p.ID_PUNTO_GEOGRAFICO AS ORIGEN_ID,
    p.NOMBRE AS NOMBRE_ORIGEN,
    p.DIRECCION AS DIRECCION_ORIGEN,
    p.CIUDAD AS CIUDAD_ORIGEN,
    LISTAGG(pg.DIRECCION, ' -> ') WITHIN GROUP (ORDER BY pa.NUMERO) AS PARADAS_DIRECCIONES
FROM SERVICIO s
JOIN TARIFASERVICIO t ON s.TIPO_SERVICIO = t.TIPO_SERVICIO
LEFT JOIN USUARIO u ON s.CLIENTE_ID = u.DOCUMENTO_USUARIO
LEFT JOIN USUARIOCONDUCTOR uc ON s.CONDUCTOR_ID = uc.DOCUMENTO_USUARIO
LEFT JOIN PUNTOGEOGRAFICO p ON s.ORIGEN_ID = p.ID_PUNTO_GEOGRAFICO
LEFT JOIN PARADA pa ON s.ID_SERVICIO = pa.ID_SERVICIO
LEFT JOIN PUNTOGEOGRAFICO pg ON pa.ID_PUNTO_GEOGRAFICO = pg.ID_PUNTO_GEOGRAFICO
WHERE s.CLIENTE_ID = :cliente_id
GROUP BY
    s.ID_SERVICIO,
    s.TIPO_SERVICIO,
    s.CLIENTE_ID,
    u.NOMBRE,
    s.CONDUCTOR_ID,
    uc.HABILITADO,
    s.DISTANCIA_KM,
    t.COSTO_POR_KM,
    s.HORA_INICIO,
    s.HORA_FIN,
    s.DURACION_MIN,
    p.ID_PUNTO_GEOGRAFICO,
    p.NOMBRE,
    p.DIRECCION,
    p.CIUDAD
ORDER BY s.HORA_INICIO DESC;

-- ==================================================================
-- RFC2 - MOSTRAR LOS 20 USUARIOS CONDUCTORES QUE MÁS SERVICIOS HAN PRESTADO
-- Descripción: Identificar los conductores con mayor número de servicios.
-- Parámetros: ninguno; limitar a top 20.
-- Tablas usadas: SERVICIO, USUARIOCONDUCTOR, USUARIO
-- Motivo de los joins: se cuenta por conductor en SERVICIO y se une con USUARIO
-- para mostrar nombre/datos de contacto.
-- ==================================================================

SELECT
    u.DOCUMENTO_USUARIO AS CONDUCTOR_ID,
    u.NOMBRE AS NOMBRE_CONDUCTOR,
    u.CORREO,
    u.CELULAR,
    COUNT(*) AS NUM_SERVICIOS
FROM SERVICIO s
JOIN USUARIOCONDUCTOR uc ON s.CONDUCTOR_ID = uc.DOCUMENTO_USUARIO
JOIN USUARIO u ON uc.DOCUMENTO_USUARIO = u.DOCUMENTO_USUARIO
GROUP BY u.DOCUMENTO_USUARIO, u.NOMBRE, u.CORREO, u.CELULAR
ORDER BY NUM_SERVICIOS DESC
FETCH FIRST 20 ROWS ONLY;

-- ==================================================================
-- RFC3 - MOSTRAR EL TOTAL DE DINERO OBTENIDO POR USUARIOS CONDUCTORES
--         PARA CADA UNO DE SUS VEHÍCULOS, DISCRIMINADO POR SERVICIOS
-- Descripción: Para cada conductor y cada vehículo suyo, mostrar el dinero
-- total ganado por tipo de servicio (INGRESOS_BRUTOS) y la parte que recibe
-- el conductor (INGRESOS_PARA_CONDUCTOR = 60% de INGRESOS_BRUTOS).
-- Observación: En la base de datos la relación servicio->vehículo se realiza
-- a través de SERVICIODISPONIBILIDAD -> DISPONIBILIDAD -> VEHICULO.
-- Tablas usadas: SERVICIO, TARIFASERVICIO, SERVICIODISPONIBILIDAD, DISPONIBILIDAD, VEHICULO, USUARIO
-- Motivo de los joins: vincular cada servicio con la disponibilidad/vehículo
-- usado y luego agrupar por placa y tipo de servicio.
-- ==================================================================

SELECT
    v.DOCUMENTO_CONDUCTOR AS CONDUCTOR_ID,
    u.NOMBRE AS NOMBRE_CONDUCTOR,
    v.PLACA,
    s.TIPO_SERVICIO,
    SUM(s.DISTANCIA_KM * t.COSTO_POR_KM) AS INGRESOS_BRUTOS,
    SUM(0.6 * s.DISTANCIA_KM * t.COSTO_POR_KM) AS INGRESOS_PARA_CONDUCTOR
FROM SERVICIO s
JOIN TARIFASERVICIO t ON s.TIPO_SERVICIO = t.TIPO_SERVICIO
JOIN SERVICIODISPONIBILIDAD sd ON s.ID_SERVICIO = sd.ID_SERVICIO
JOIN DISPONIBILIDAD d ON sd.ID_DISPONIBILIDAD = d.ID_DISPONIBILIDAD
JOIN VEHICULO v ON d.PLACA_VEHICULO = v.PLACA
LEFT JOIN USUARIO u ON v.DOCUMENTO_CONDUCTOR = u.DOCUMENTO_USUARIO
GROUP BY v.DOCUMENTO_CONDUCTOR, u.NOMBRE, v.PLACA, s.TIPO_SERVICIO
ORDER BY v.DOCUMENTO_CONDUCTOR, v.PLACA, s.TIPO_SERVICIO;

-- ==================================================================
-- RFC4 - MOSTRAR LA UTILIZACIÓN DE SERVICIOS EN UNA CIUDAD DURANTE UN RANGO DE FECHAS
-- Descripción: Para una ciudad y rango de fechas, mostrar el número de servicios
-- por tipo (nivel) y el porcentaje que representa del total en ese rango.
-- Parámetros: :ciudad, :fecha_inicio, :fecha_fin. (Fechas en formato 'YYYY-MM-DD HH24:MI:SS')
-- Atención: Esta consulta considera la ciudad del punto de origen del servicio.
-- Si se desea incluir servicios que llegan a la ciudad (paradas/destinos), se
-- puede ampliar la consulta para chequear PARADA -> PUNTOGEOGRAFICO.
-- Tablas usadas: SERVICIO, PUNTOGEOGRAFICO, TARIFASERVICIO (opcional para detalle de tarifa)
-- ==================================================================

WITH SERVICIOS_CIUDAD AS (
    SELECT s.ID_SERVICIO, s.TIPO_SERVICIO
    FROM SERVICIO s
    JOIN PUNTOGEOGRAFICO pg ON s.ORIGEN_ID = pg.ID_PUNTO_GEOGRAFICO
    WHERE pg.CIUDAD = :ciudad
      AND s.HORA_INICIO BETWEEN TO_TIMESTAMP(:fecha_inicio,'YYYY-MM-DD HH24:MI:SS')
                            AND TO_TIMESTAMP(:fecha_fin,'YYYY-MM-DD HH24:MI:SS')
)
SELECT
    TIPO_SERVICIO,
    COUNT(*) AS NUM_SERVICIOS,
    ROUND(100 * COUNT(*) / SUM(COUNT(*)) OVER (), 2) AS PORCENTAJE_DEL_TOTAL
FROM SERVICIOS_CIUDAD
GROUP BY TIPO_SERVICIO
ORDER BY NUM_SERVICIOS DESC;

