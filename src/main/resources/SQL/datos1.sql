-- Seed data para ALPESCAB
-- Genera: 100 conductores, 200 usuarios de servicio, 100 vehículos, 200 puntos geográficos,
-- 100 disponibilidades, 3 tarifas, ~1000 servicios (5 por pasajero), paradas, revisiones y relaciones.
-- Ejecutar en Oracle SQL*Plus o SQL Developer contra el schema destino.

SET DEFINE OFF;

SET DEFINE OFF;

-- ==================================================================
-- LIMPIEZA: borrar filas en orden que respete FK (children primero)
-- ==================================================================
BEGIN
    EXECUTE IMMEDIATE 'DELETE FROM PARADA';
    EXECUTE IMMEDIATE 'DELETE FROM SERVICIODISPONIBILIDAD';
    EXECUTE IMMEDIATE 'DELETE FROM REVISION';
    EXECUTE IMMEDIATE 'DELETE FROM SERVICIO';
    EXECUTE IMMEDIATE 'DELETE FROM DISPONIBILIDAD';
    EXECUTE IMMEDIATE 'DELETE FROM VEHICULO';
    EXECUTE IMMEDIATE 'DELETE FROM USUARIOSERVICIO';
    EXECUTE IMMEDIATE 'DELETE FROM USUARIOCONDUCTOR';
    EXECUTE IMMEDIATE 'DELETE FROM USUARIO';
    EXECUTE IMMEDIATE 'DELETE FROM PUNTOGEOGRAFICO';
    EXECUTE IMMEDIATE 'DELETE FROM CIUDAD';
    EXECUTE IMMEDIATE 'DELETE FROM TARIFASERVICIO';
    COMMIT;
EXCEPTION WHEN OTHERS THEN
    NULL;
END;
/ 

-- ====== TARIFAS (reinsertar después del borrado) ======
INSERT INTO TARIFASERVICIO (TIPO_SERVICIO, COSTO_POR_KM) VALUES ('Estandar', 1000);
INSERT INTO TARIFASERVICIO (TIPO_SERVICIO, COSTO_POR_KM) VALUES ('Confort', 1500);
INSERT INTO TARIFASERVICIO (TIPO_SERVICIO, COSTO_POR_KM) VALUES ('Large', 2000);

-- ====== CIUDADES (necesarias antes de PUNTOS) ======
INSERT INTO CIUDAD (NOMBRE_CIUDAD, PAIS) VALUES ('Bogotá','Colombia');
INSERT INTO CIUDAD (NOMBRE_CIUDAD, PAIS) VALUES ('Medellín','Colombia');
INSERT INTO CIUDAD (NOMBRE_CIUDAD, PAIS) VALUES ('Cali','Colombia');
INSERT INTO CIUDAD (NOMBRE_CIUDAD, PAIS) VALUES ('Barranquilla','Colombia');
INSERT INTO CIUDAD (NOMBRE_CIUDAD, PAIS) VALUES ('Bucaramanga','Colombia');

-- ====== USUARIOS CONDUCTORES + USUARIO (100) ======
DECLARE
    i NUMBER := 1000;
BEGIN
    FOR i IN 1001..1100 LOOP
        INSERT INTO USUARIO (DOCUMENTO_USUARIO, NOMBRE, CORREO, CELULAR)
        VALUES (i, 'Conductor ' || i, 'conductor' || i || '@mail.com', '300' || i);

        INSERT INTO USUARIOCONDUCTOR (DOCUMENTO_USUARIO, HABILITADO)
        VALUES (i, '1');
    END LOOP;
    COMMIT;
END;
/

-- ====== USUARIOS DE SERVICIO (200) ======
DECLARE
    j NUMBER := 2000;
    tarjeta_base NUMBER := 4000000000000000; -- base para generar tarjetas
BEGIN
    FOR j IN 2001..2200 LOOP
        INSERT INTO USUARIO (DOCUMENTO_USUARIO, NOMBRE, CORREO, CELULAR)
        VALUES (j, 'Pasajero ' || j, 'pasajero' || j || '@mail.com', '310' || j);

        -- Intentamos insertar respetando dos variantes de esquema: DOCUMENTO_USUARIO o DOCUMENTO
        BEGIN
            INSERT INTO USUARIOSERVICIO (DOCUMENTO_USUARIO, NUMEROTARJETA, NOMBRETARJETA, FECHAVENCIMIENTO, CODIGOSEGURIDAD)
            VALUES (j, tarjeta_base + j, 'Pasajero ' || j, DATE '2026-12-31', 123);
        EXCEPTION WHEN OTHERS THEN
            BEGIN
                INSERT INTO USUARIOSERVICIO (DOCUMENTO, NUMEROTARJETA, NOMBRETARJETA, FECHAVENCIMIENTO, CODIGOSEGURIDAD)
                VALUES (j, tarjeta_base + j, 'Pasajero ' || j, DATE '2026-12-31', 123);
            EXCEPTION WHEN OTHERS THEN
                NULL;
            END;
        END;
    END LOOP;
    COMMIT;
END;
/

-- ====== PUNTOS GEOGRÁFICOS (200) ======
DECLARE
    p NUMBER := 3000;
    ciudades SYS.odcivarchar2list := SYS.odcivarchar2list('Bogotá','Medellín','Cali','Barranquilla','Bucaramanga');
BEGIN
    FOR p IN 3001..3200 LOOP
        INSERT INTO PUNTOGEOGRAFICO (ID_PUNTO_GEOGRAFICO, NOMBRE, DIRECCION, COORDENADAS, CIUDAD)
        VALUES (p, 'Punto ' || p, 'Calle ' || p || ' # ' || (100 + p), '-74.' || (100000 + p) || ',4.' || (70000 + p), ciudades(MOD(p,5)+1));
    END LOOP;
    COMMIT;
END;
/

-- ====== VEHÍCULOS (uno por conductor) ======
DECLARE
    i NUMBER := 1000;
BEGIN
    FOR i IN 1001..1100 LOOP
        BEGIN
            INSERT INTO VEHICULO (PLACA, TIPO, MODELO, MARCA, COLOR, CIUDAD_EXPEDICION_PLACA, CAPACIDAD_PASAJEROS, DOCUMENTO_CONDUCTOR)
            VALUES ('CAR' || i, 'Carro', 'Modelo ' || (2015 + MOD(i,10)), 'Marca ' || MOD(i,5), 'Color ' || MOD(i,3), 'Bogotá', 4, i);
        EXCEPTION WHEN OTHERS THEN
            NULL;
        END;
    END LOOP;
    COMMIT;
END;
/

-- ====== DISPONIBILIDADES (uno por vehículo) ======
DECLARE
    d NUMBER := 4000;
BEGIN
    FOR d IN 4001..4100 LOOP
        BEGIN
            INSERT INTO DISPONIBILIDAD (ID_DISPONIBILIDAD, DIA, HORA_INICIO, HORA_FIN, PLACA_VEHICULO)
            VALUES (d, 2, TO_TIMESTAMP('2025-09-28 08:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2025-09-28 12:00:00','YYYY-MM-DD HH24:MI:SS'), 'CAR' || (1000 + MOD(d-4000,100) + 1));
        EXCEPTION WHEN OTHERS THEN
            NULL;
        END;
    END LOOP;
    COMMIT;
END;
/

-- ====== SERVICIOS (~1000) ======
DECLARE
    v_id NUMBER := 5000;
BEGIN
    FOR pas IN 2001..2200 LOOP
        FOR k IN 1..5 LOOP
            v_id := v_id + 1;
            BEGIN
                INSERT INTO SERVICIO (ID_SERVICIO, TIPO_SERVICIO, CLIENTE_ID, CONDUCTOR_ID, ORIGEN_ID, DISTANCIA_KM, HORA_INICIO, HORA_FIN, DURACION_MIN)
                VALUES (
                    v_id,
                    CASE MOD(v_id,3) WHEN 0 THEN 'Estandar' WHEN 1 THEN 'Confort' ELSE 'Large' END,
                    pas,
                    1000 + MOD(v_id,100) + 1,
                    3000 + MOD(v_id,200) + 1,
                    5 + MOD(v_id,50),
                    TO_TIMESTAMP('2025-09-' || LPAD(20 + MOD(v_id,8),2,'0') || ' ' || LPAD(8 + MOD(v_id,10),2,'0') || ':00:00','YYYY-MM-DD HH24:MI:SS'),
                    TO_TIMESTAMP('2025-09-' || LPAD(20 + MOD(v_id,8),2,'0') || ' ' || LPAD(9 + MOD(v_id,10),2,'0') || ':00:00','YYYY-MM-DD HH24:MI:SS'),
                    10 + MOD(v_id,120)
                );

                -- Asociar servicio a una disponibilidad
                INSERT INTO SERVICIODISPONIBILIDAD (ID_SERVICIO, ID_DISPONIBILIDAD)
                VALUES (v_id, 4000 + MOD(v_id,100) + 1);

                -- Insertar 1..3 paradas
                IF MOD(v_id,3) = 0 THEN
                    INSERT INTO PARADA (ID_SERVICIO, ID_PUNTO_GEOGRAFICO, NUMERO) VALUES (v_id, 3000 + MOD(v_id,200) + 1, 1);
                ELSIF MOD(v_id,3) = 1 THEN
                    INSERT INTO PARADA (ID_SERVICIO, ID_PUNTO_GEOGRAFICO, NUMERO) VALUES (v_id, 3000 + MOD(v_id+1,200) + 1, 1);
                    INSERT INTO PARADA (ID_SERVICIO, ID_PUNTO_GEOGRAFICO, NUMERO) VALUES (v_id, 3000 + MOD(v_id+2,200) + 1, 2);
                ELSE
                    INSERT INTO PARADA (ID_SERVICIO, ID_PUNTO_GEOGRAFICO, NUMERO) VALUES (v_id, 3000 + MOD(v_id+3,200) + 1, 1);
                    INSERT INTO PARADA (ID_SERVICIO, ID_PUNTO_GEOGRAFICO, NUMERO) VALUES (v_id, 3000 + MOD(v_id+4,200) + 1, 2);
                    INSERT INTO PARADA (ID_SERVICIO, ID_PUNTO_GEOGRAFICO, NUMERO) VALUES (v_id, 3000 + MOD(v_id+5,200) + 1, 3);
                END IF;

                -- 40% de probabilidades de dejar revisión (determinístico: si mod(v_id,5)=0)
                IF MOD(v_id,5) = 0 THEN
                    INSERT INTO REVISION (ID_REVISION, EMISOR_ID, RECEPTOR_ID, CALIFICACION, COMENTARIO)
                    VALUES (800000 + v_id, pas, 1000 + MOD(v_id,100) + 1, MOD(v_id,6), 'Revisión automática.');
                END IF;
            EXCEPTION WHEN OTHERS THEN
                -- si alguna inserción falla (FK u otro), ignorar y continuar
                NULL;
            END;
        END LOOP;
    END LOOP;
    COMMIT;
END;
/

PROMPT Seed completo.
