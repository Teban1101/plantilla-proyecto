package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "PARADA")
public class Parada {

    @EmbeddedId
    private ParadaId id;

    @Column(name = "NUMERO")
    private Integer numero;

    public Parada() {
    }

    public ParadaId getId() {
        return id;
    }

    public void setId(ParadaId id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
}
