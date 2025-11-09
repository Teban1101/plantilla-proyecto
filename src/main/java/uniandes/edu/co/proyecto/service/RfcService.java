package uniandes.edu.co.proyecto.service;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import uniandes.edu.co.proyecto.dto.Rfc1Dto;
import uniandes.edu.co.proyecto.dto.Rfc2Dto;
import uniandes.edu.co.proyecto.dto.Rfc3Dto;
import uniandes.edu.co.proyecto.dto.Rfc4Dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RfcService {

    private final NamedParameterJdbcTemplate jdbc;

    public RfcService(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Rfc1Dto> rfc1(Long clienteId) {
        String sql = "SELECT s.ID_SERVICIO, s.TIPO_SERVICIO, s.CLIENTE_ID, u.NOMBRE AS NOMBRE_CLIENTE, "
                + "s.CONDUCTOR_ID, uc.HABILITADO AS CONDUCTOR_HABILITADO, s.DISTANCIA_KM, t.COSTO_POR_KM, "
                + "(s.DISTANCIA_KM * t.COSTO_POR_KM) AS COSTO_TOTAL, ROUND(0.6 * (s.DISTANCIA_KM * t.COSTO_POR_KM), 2) AS COSTO_PARA_CONDUCTOR, "
                + "s.HORA_INICIO, s.HORA_FIN, s.DURACION_MIN, p.ID_PUNTO_GEOGRAFICO AS ORIGEN_ID, p.NOMBRE AS NOMBRE_ORIGEN, "
                + "p.DIRECCION AS DIRECCION_ORIGEN, p.CIUDAD AS CIUDAD_ORIGEN, LISTAGG(pg.DIRECCION, ' -> ') WITHIN GROUP (ORDER BY pa.NUMERO) AS PARADAS_DIRECCIONES "
                + "FROM SERVICIO s "
                + "JOIN TARIFASERVICIO t ON s.TIPO_SERVICIO = t.TIPO_SERVICIO "
                + "LEFT JOIN USUARIO u ON s.CLIENTE_ID = u.DOCUMENTO_USUARIO "
                + "LEFT JOIN USUARIOCONDUCTOR uc ON s.CONDUCTOR_ID = uc.DOCUMENTO_USUARIO "
                + "LEFT JOIN PUNTOGEOGRAFICO p ON s.ORIGEN_ID = p.ID_PUNTO_GEOGRAFICO "
                + "LEFT JOIN PARADA pa ON s.ID_SERVICIO = pa.ID_SERVICIO "
                + "LEFT JOIN PUNTOGEOGRAFICO pg ON pa.ID_PUNTO_GEOGRAFICO = pg.ID_PUNTO_GEOGRAFICO "
                + "WHERE s.CLIENTE_ID = :cliente_id "
                + "GROUP BY s.ID_SERVICIO, s.TIPO_SERVICIO, s.CLIENTE_ID, u.NOMBRE, s.CONDUCTOR_ID, uc.HABILITADO, s.DISTANCIA_KM, t.COSTO_POR_KM, s.HORA_INICIO, s.HORA_FIN, s.DURACION_MIN, p.ID_PUNTO_GEOGRAFICO, p.NOMBRE, p.DIRECCION, p.CIUDAD "
                + "ORDER BY s.HORA_INICIO DESC";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("cliente_id", clienteId);

        return jdbc.query(sql, params, (rs, rowNum) -> mapRfc1(rs));
    }

    private Rfc1Dto mapRfc1(ResultSet rs) throws SQLException {
        Rfc1Dto d = new Rfc1Dto();
        d.idServicio = rs.getLong("ID_SERVICIO");
        d.tipoServicio = rs.getString("TIPO_SERVICIO");
        d.clienteId = rs.getObject("CLIENTE_ID") == null ? null : rs.getLong("CLIENTE_ID");
        d.nombreCliente = rs.getString("NOMBRE_CLIENTE");
        d.conductorId = rs.getObject("CONDUCTOR_ID") == null ? null : rs.getLong("CONDUCTOR_ID");
        d.conductorHabilitado = rs.getString("CONDUCTOR_HABILITADO");
        d.distanciaKm = rs.getDouble("DISTANCIA_KM");
        d.costoPorKm = rs.getDouble("COSTO_POR_KM");
        d.costoTotal = rs.getDouble("COSTO_TOTAL");
        d.costoParaConductor = rs.getDouble("COSTO_PARA_CONDUCTOR");
        Timestamp t1 = rs.getTimestamp("HORA_INICIO");
        d.horaInicio = t1 == null ? null : t1.toLocalDateTime();
        Timestamp t2 = rs.getTimestamp("HORA_FIN");
        d.horaFin = t2 == null ? null : t2.toLocalDateTime();
        d.duracionMin = rs.getObject("DURACION_MIN") == null ? null : rs.getInt("DURACION_MIN");
        d.origenId = rs.getObject("ORIGEN_ID") == null ? null : rs.getLong("ORIGEN_ID");
        d.nombreOrigen = rs.getString("NOMBRE_ORIGEN");
        d.direccionOrigen = rs.getString("DIRECCION_ORIGEN");
        d.ciudadOrigen = rs.getString("CIUDAD_ORIGEN");
        d.paradasDirecciones = rs.getString("PARADAS_DIRECCIONES");
        return d;
    }

    public List<Rfc2Dto> rfc2() {
        String sql = "SELECT u.DOCUMENTO_USUARIO AS CONDUCTOR_ID, u.NOMBRE AS NOMBRE_CONDUCTOR, u.CORREO, u.CELULAR, COUNT(*) AS NUM_SERVICIOS "
                + "FROM SERVICIO s "
                + "JOIN USUARIOCONDUCTOR uc ON s.CONDUCTOR_ID = uc.DOCUMENTO_USUARIO "
                + "JOIN USUARIO u ON uc.DOCUMENTO_USUARIO = u.DOCUMENTO_USUARIO "
                + "GROUP BY u.DOCUMENTO_USUARIO, u.NOMBRE, u.CORREO, u.CELULAR "
                + "ORDER BY NUM_SERVICIOS DESC FETCH FIRST 20 ROWS ONLY";

        return jdbc.query(sql, (rs, rowNum) -> {
            Rfc2Dto d = new Rfc2Dto();
            d.conductorId = rs.getLong("CONDUCTOR_ID");
            d.nombreConductor = rs.getString("NOMBRE_CONDUCTOR");
            d.correo = rs.getString("CORREO");
            d.celular = rs.getString("CELULAR");
            d.numServicios = rs.getLong("NUM_SERVICIOS");
            return d;
        });
    }

    public List<Rfc3Dto> rfc3() {
        String sql = "SELECT v.DOCUMENTO_CONDUCTOR AS CONDUCTOR_ID, u.NOMBRE AS NOMBRE_CONDUCTOR, v.PLACA, s.TIPO_SERVICIO, "
                + "SUM(s.DISTANCIA_KM * t.COSTO_POR_KM) AS INGRESOS_BRUTOS, SUM(0.6 * s.DISTANCIA_KM * t.COSTO_POR_KM) AS INGRESOS_PARA_CONDUCTOR "
                + "FROM SERVICIO s "
                + "JOIN TARIFASERVICIO t ON s.TIPO_SERVICIO = t.TIPO_SERVICIO "
                + "JOIN SERVICIODISPONIBILIDAD sd ON s.ID_SERVICIO = sd.ID_SERVICIO "
                + "JOIN DISPONIBILIDAD d ON sd.ID_DISPONIBILIDAD = d.ID_DISPONIBILIDAD "
                + "JOIN VEHICULO v ON d.PLACA_VEHICULO = v.PLACA "
                + "LEFT JOIN USUARIO u ON v.DOCUMENTO_CONDUCTOR = u.DOCUMENTO_USUARIO "
                + "GROUP BY v.DOCUMENTO_CONDUCTOR, u.NOMBRE, v.PLACA, s.TIPO_SERVICIO "
                + "ORDER BY v.DOCUMENTO_CONDUCTOR, v.PLACA, s.TIPO_SERVICIO";

        return jdbc.query(sql, (rs, rowNum) -> {
            Rfc3Dto d = new Rfc3Dto();
            d.conductorId = rs.getObject("CONDUCTOR_ID") == null ? null : rs.getLong("CONDUCTOR_ID");
            d.nombreConductor = rs.getString("NOMBRE_CONDUCTOR");
            d.placa = rs.getString("PLACA");
            d.tipoServicio = rs.getString("TIPO_SERVICIO");
            d.ingresosBrutos = rs.getDouble("INGRESOS_BRUTOS");
            d.ingresosParaConductor = rs.getDouble("INGRESOS_PARA_CONDUCTOR");
            return d;
        });
    }

    public List<Rfc4Dto> rfc4(String ciudad, String fechaInicio, String fechaFin) {
        String sql = "WITH SERVICIOS_CIUDAD AS ( "
                + "SELECT s.ID_SERVICIO, s.TIPO_SERVICIO FROM SERVICIO s JOIN PUNTOGEOGRAFICO pg ON s.ORIGEN_ID = pg.ID_PUNTO_GEOGRAFICO "
                + "WHERE pg.CIUDAD = :ciudad AND s.HORA_INICIO BETWEEN TO_TIMESTAMP(:fecha_inicio,'YYYY-MM-DD HH24:MI:SS') AND TO_TIMESTAMP(:fecha_fin,'YYYY-MM-DD HH24:MI:SS') ) "
                + "SELECT TIPO_SERVICIO, COUNT(*) AS NUM_SERVICIOS, ROUND(100 * COUNT(*) / SUM(COUNT(*)) OVER (), 2) AS PORCENTAJE_DEL_TOTAL "
                + "FROM SERVICIOS_CIUDAD GROUP BY TIPO_SERVICIO ORDER BY NUM_SERVICIOS DESC";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("ciudad", ciudad);
        params.addValue("fecha_inicio", fechaInicio);
        params.addValue("fecha_fin", fechaFin);

        return jdbc.query(sql, params, (rs, rowNum) -> {
            Rfc4Dto d = new Rfc4Dto();
            d.tipoServicio = rs.getString("TIPO_SERVICIO");
            d.numServicios = rs.getLong("NUM_SERVICIOS");
            d.porcentajeDelTotal = rs.getDouble("PORCENTAJE_DEL_TOTAL");
            return d;
        });
    }
}
