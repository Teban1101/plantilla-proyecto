package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "VEHICULO")
public class Vehiculo {

    @Id
    @Column(name = "PLACA")
    @NotBlank
    private String placa;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "MODELO")
    private String modelo;

    @Column(name = "MARCA")
    private String marca;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "CIUDAD_EXPEDICION_PLACA")
    private String ciudadExpedicionPlaca;

    @Column(name = "CAPACIDAD_PASAJEROS")
    private Integer capacidadPasajeros;

    @Column(name = "DOCUMENTO_CONDUCTOR")
    private Long documentoConductor;

    public Vehiculo() {
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCiudadExpedicionPlaca() {
        return ciudadExpedicionPlaca;
    }

    public void setCiudadExpedicionPlaca(String ciudadExpedicionPlaca) {
        this.ciudadExpedicionPlaca = ciudadExpedicionPlaca;
    }

    public Integer getCapacidadPasajeros() {
        return capacidadPasajeros;
    }

    public void setCapacidadPasajeros(Integer capacidadPasajeros) {
        this.capacidadPasajeros = capacidadPasajeros;
    }

    public Long getDocumentoConductor() {
        return documentoConductor;
    }

    public void setDocumentoConductor(Long documentoConductor) {
        this.documentoConductor = documentoConductor;
    }
}
