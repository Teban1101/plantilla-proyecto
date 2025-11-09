-- Script: Arregla FK cliente en SERVICIO (apunta a USUARIO) opcionalmente elimina USUARIOSERVICIO si está vacía
-- Luego popula SERVICIO, SERVICIODISPONIBILIDAD, PARADA y REVISION con mensajes de progreso.

SET DEFINE OFF;
SET SERVEROUTPUT ON SIZE 1000000;

DECLARE
    v_exists NUMBER;
    v_count NUMBER;
    v_id NUMBER;
    v_conductor NUMBER;
    v_origen NUMBER;
    v_disponibilidad NUMBER;
    v_punto NUMBER;
    v_distancia NUMBER;
    v_duracion NUMBER;
    v_inicio TIMESTAMP;
    v_fin TIMESTAMP;
    v_tipo VARCHAR2(50);
    v_num_paradas NUMBER;
    cnt_servicios NUMBER := 0;
    cnt_paradas NUMBER := 0;
    cnt_revisiones NUMBER := 0;
BEGIN
    DBMS_OUTPUT.PUT_LINE('fix_and_populate_services: inicio');

    -- 1) Asegurar que TARIFASERVICIO tiene tipos necesarios
    SELECT COUNT(*) INTO v_count FROM TARIFASERVICIO;
    IF v_count = 0 THEN
        DBMS_OUTPUT.PUT_LINE('No hay tarifas, insertando tarifas por defecto');
        BEGIN
            INSERT INTO TARIFASERVICIO (TIPO_SERVICIO, COSTO_POR_KM) VALUES ('Estandar', 100);
            INSERT INTO TARIFASERVICIO (TIPO_SERVICIO, COSTO_POR_KM) VALUES ('Confort', 150);
            INSERT INTO TARIFASERVICIO (TIPO_SERVICIO, COSTO_POR_KM) VALUES ('Large', 200);
            COMMIT;
        EXCEPTION WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Warning: no pudo insertar tarifas por defecto: '||SQLERRM);
            ROLLBACK;
        END;
    END IF;

    -- 2) Reparar FK de SERVICIO: si apunta a USUARIOSERVICIO, cambiarlo para apuntar a USUARIO
    SELECT COUNT(*) INTO v_exists FROM USER_CONSTRAINTS WHERE TABLE_NAME = 'SERVICIO' AND CONSTRAINT_NAME = 'FK_SERVICIO_CLIENTE';
    IF v_exists > 0 THEN
        BEGIN
            EXECUTE IMMEDIATE 'ALTER TABLE SERVICIO DROP CONSTRAINT FK_SERVICIO_CLIENTE';
            DBMS_OUTPUT.PUT_LINE('Constraint FK_SERVICIO_CLIENTE eliminada');
        EXCEPTION WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('No se pudo eliminar FK_SERVICIO_CLIENTE (tal vez no existe): '||SQLERRM);
        END;
    END IF;

    -- Añadir constraint nueva apuntando a USUARIO (si no existe)
    SELECT COUNT(*) INTO v_exists FROM USER_CONSTRAINTS WHERE TABLE_NAME = 'SERVICIO' AND CONSTRAINT_NAME = 'FK_SERVICIO_CLIENTE';
    IF v_exists = 0 THEN
        BEGIN
            EXECUTE IMMEDIATE 'ALTER TABLE SERVICIO ADD CONSTRAINT FK_SERVICIO_CLIENTE FOREIGN KEY (CLIENTE_ID) REFERENCES USUARIO(DOCUMENTO_USUARIO)';
            DBMS_OUTPUT.PUT_LINE('Constraint FK_SERVICIO_CLIENTE creada apuntando a USUARIO');
        EXCEPTION WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('No se pudo crear FK_SERVICIO_CLIENTE: '||SQLERRM);
        END;
    END IF;

    -- 3) Si USUARIOSERVICIO existe y está vacía, eliminarla (el usuario lo solicitó)
    SELECT COUNT(*) INTO v_exists FROM USER_TABLES WHERE TABLE_NAME = 'USUARIOSERVICIO';
    IF v_exists > 0 THEN
        SELECT COUNT(*) INTO v_count FROM USUARIOSERVICIO;
        IF v_count = 0 THEN
            BEGIN
                EXECUTE IMMEDIATE 'DROP TABLE USUARIOSERVICIO CASCADE CONSTRAINTS';
                DBMS_OUTPUT.PUT_LINE('USUARIOSERVICIO eliminada porque estaba vacía');
            EXCEPTION WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('No se pudo eliminar USUARIOSERVICIO: '||SQLERRM);
            END;
        ELSE
            DBMS_OUTPUT.PUT_LINE('USUARIOSERVICIO tiene '||v_count||' filas: no se eliminará');
        END IF;
    ELSE
        DBMS_OUTPUT.PUT_LINE('USUARIOSERVICIO no existe');
    END IF;

    -- 4) Preparar v_id
    SELECT NVL(MAX(ID_SERVICIO), 5000) INTO v_id FROM SERVICIO;

    -- 5) Crear servicios: por cada usuario en USUARIO crear 5 servicios
    FOR u IN (SELECT DOCUMENTO_USUARIO FROM USUARIO) LOOP
        FOR i IN 1..5 LOOP
            v_id := v_id + 1;

            -- seleccionar conductor aleatorio
            BEGIN
                SELECT DOCUMENTO_USUARIO INTO v_conductor FROM (
                    SELECT DOCUMENTO_USUARIO FROM USUARIOCONDUCTOR WHERE HABILITADO IN ('1','S') ORDER BY DBMS_RANDOM.VALUE
                ) WHERE ROWNUM = 1;
            EXCEPTION WHEN NO_DATA_FOUND THEN
                v_conductor := NULL;
            END;

            IF v_conductor IS NULL THEN
                -- si no hay conductores, no podemos insertar servicio
                CONTINUE;
            END IF;

            -- origen aleatorio
            BEGIN
                SELECT ID_PUNTO_GEOGRAFICO INTO v_origen FROM (
                    SELECT ID_PUNTO_GEOGRAFICO FROM PUNTOGEOGRAFICO ORDER BY DBMS_RANDOM.VALUE
                ) WHERE ROWNUM = 1;
            EXCEPTION WHEN NO_DATA_FOUND THEN
                v_origen := NULL;
            END;

            IF v_origen IS NULL THEN
                CONTINUE;
            END IF;

            -- disponibilidad aleatoria
            BEGIN
                SELECT ID_DISPONIBILIDAD INTO v_disponibilidad FROM (
                    SELECT ID_DISPONIBILIDAD FROM DISPONIBILIDAD ORDER BY DBMS_RANDOM.VALUE
                ) WHERE ROWNUM = 1;
            EXCEPTION WHEN NO_DATA_FOUND THEN
                v_disponibilidad := NULL;
            END;

            v_tipo := CASE WHEN MOD(v_id,3)=0 THEN 'Estandar' WHEN MOD(v_id,3)=1 THEN 'Confort' ELSE 'Large' END;
            v_distancia := ROUND(3 + DBMS_RANDOM.VALUE * 20, 2);
            v_inicio := SYSTIMESTAMP + NUMTODSINTERVAL(TRUNC(DBMS_RANDOM.VALUE(0,72)),'HOUR');
            v_duracion := 10 + TRUNC(DBMS_RANDOM.VALUE(0,120));
            v_fin := v_inicio + NUMTODSINTERVAL(v_duracion,'MINUTE');

            BEGIN
                INSERT INTO SERVICIO (ID_SERVICIO, TIPO_SERVICIO, CLIENTE_ID, CONDUCTOR_ID, ORIGEN_ID, DISTANCIA_KM, HORA_INICIO, HORA_FIN, DURACION_MIN)
                VALUES (v_id, v_tipo, u.DOCUMENTO_USUARIO, v_conductor, v_origen, v_distancia, v_inicio, v_fin, v_duracion);
                cnt_servicios := cnt_servicios + 1;
            EXCEPTION WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE('Insert SERVICIO falló para cliente '||u.DOCUMENTO_USUARIO||': '||SQLERRM);
                CONTINUE;
            END;

            -- asociar disponibilidad
            IF v_disponibilidad IS NOT NULL THEN
                BEGIN
                    INSERT INTO SERVICIODISPONIBILIDAD (ID_SERVICIO, ID_DISPONIBILIDAD) VALUES (v_id, v_disponibilidad);
                EXCEPTION WHEN OTHERS THEN
                    NULL;
                END;
            END IF;

            -- paradas
            v_num_paradas := 1 + TRUNC(DBMS_RANDOM.VALUE * 3);
            FOR p_idx IN 1..v_num_paradas LOOP
                BEGIN
                    SELECT ID_PUNTO_GEOGRAFICO INTO v_punto FROM (
                        SELECT ID_PUNTO_GEOGRAFICO FROM PUNTOGEOGRAFICO ORDER BY DBMS_RANDOM.VALUE
                    ) WHERE ROWNUM = 1;
                    INSERT INTO PARADA (ID_SERVICIO, ID_PUNTO_GEOGRAFICO, NUMERO) VALUES (v_id, v_punto, p_idx);
                    cnt_paradas := cnt_paradas + 1;
                EXCEPTION WHEN OTHERS THEN
                    NULL;
                END;
            END LOOP;

            -- revisión ocasional
            IF MOD(v_id,5)=0 THEN
                BEGIN
                    INSERT INTO REVISION (ID_REVISION, EMISOR_ID, RECEPTOR_ID, CALIFICACION, COMENTARIO)
                    VALUES (900000 + v_id, u.DOCUMENTO_USUARIO, v_conductor, MOD(v_id,6), 'Revisión generada por fix_and_populate_services.sql');
                    cnt_revisiones := cnt_revisiones + 1;
                EXCEPTION WHEN OTHERS THEN
                    NULL;
                END;
            END IF;

            -- commit periódico
            IF MOD(cnt_servicios,200)=0 THEN
                COMMIT;
                DBMS_OUTPUT.PUT_LINE('Committed '||cnt_servicios||' servicios hasta ahora');
            END IF;

        END LOOP;
    END LOOP;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('fix_and_populate_services: terminado. Servicios='||cnt_servicios||', Paradas='||cnt_paradas||', Revisiones='||cnt_revisiones);
EXCEPTION WHEN OTHERS THEN
    DBMS_OUTPUT.PUT_LINE('fix_and_populate_services: error -> '||SQLERRM);
    ROLLBACK;
END;
/

PROMPT fix_and_populate_services: script finalizado.


SELECT 'SERVICIO' as tabla, COUNT(*) FROM SERVICIO;
SELECT 'PARADA'   as tabla, COUNT(*) FROM PARADA;
SELECT 'REVISION' as tabla, COUNT(*) FROM REVISION;