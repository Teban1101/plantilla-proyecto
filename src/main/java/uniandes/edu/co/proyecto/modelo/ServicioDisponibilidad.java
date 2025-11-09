package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "SERVICIODISPONIBILIDAD")
public class ServicioDisponibilidad {

    @EmbeddedId
    private ServicioDisponibilidadId id;

    public ServicioDisponibilidad() {
    }

    public ServicioDisponibilidadId getId() {
        return id;
    }

    public void setId(ServicioDisponibilidadId id) {
        this.id = id;
    }
}
