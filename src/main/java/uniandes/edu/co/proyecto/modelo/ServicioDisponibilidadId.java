package uniandes.edu.co.proyecto.modelo;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ServicioDisponibilidadId implements Serializable {

    @Column(name = "ID_SERVICIO")
    private Long idServicio;

    @Column(name = "ID_DISPONIBILIDAD")
    private Long idDisponibilidad;

    public ServicioDisponibilidadId() {
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public Long getIdDisponibilidad() {
        return idDisponibilidad;
    }

    public void setIdDisponibilidad(Long idDisponibilidad) {
        this.idDisponibilidad = idDisponibilidad;
    }
}
