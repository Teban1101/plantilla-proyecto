package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "TARIFASERVICIO")
public class TarifaServicio {

    @Id
    @Column(name = "TIPO_SERVICIO")
    @NotBlank
    private String tipoServicio;

    @Column(name = "COSTO_POR_KM")
    @NotNull
    private Double costoPorKm;

    public TarifaServicio() {
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Double getCostoPorKm() {
        return costoPorKm;
    }

    public void setCostoPorKm(Double costoPorKm) {
        this.costoPorKm = costoPorKm;
    }
}
