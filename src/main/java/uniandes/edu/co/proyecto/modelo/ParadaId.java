package uniandes.edu.co.proyecto.modelo;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ParadaId implements Serializable {

    @Column(name = "ID_SERVICIO")
    private Long idServicio;

    @Column(name = "ID_PUNTO_GEOGRAFICO")
    private Long idPuntoGeografico;

    public ParadaId() {
    }

    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public Long getIdPuntoGeografico() {
        return idPuntoGeografico;
    }

    public void setIdPuntoGeografico(Long idPuntoGeografico) {
        this.idPuntoGeografico = idPuntoGeografico;
    }
}
