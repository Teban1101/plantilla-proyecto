package uniandes.edu.co.proyecto.dto;

import java.time.LocalDateTime;

public class Rfc1Dto {
    public Long idServicio;
    public String tipoServicio;
    public Long clienteId;
    public String nombreCliente;
    public Long conductorId;
    public String conductorHabilitado;
    public Double distanciaKm;
    public Double costoPorKm;
    public Double costoTotal;
    public Double costoParaConductor;
    public LocalDateTime horaInicio;
    public LocalDateTime horaFin;
    public Integer duracionMin;
    public Long origenId;
    public String nombreOrigen;
    public String direccionOrigen;
    public String ciudadOrigen;
    public String paradasDirecciones;

    public Rfc1Dto() {}
}
