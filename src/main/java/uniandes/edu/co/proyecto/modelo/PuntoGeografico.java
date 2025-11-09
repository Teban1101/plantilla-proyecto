package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PUNTOGEOGRAFICO")
public class PuntoGeografico {

    @Id
    @Column(name = "ID_PUNTO_GEOGRAFICO")
    private Long idPuntoGeografico;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "COORDENADAS")
    private String coordenadas;

    @Column(name = "CIUDAD")
    private String ciudad;

    public PuntoGeografico() {
    }

    public Long getIdPuntoGeografico() {
        return idPuntoGeografico;
    }

    public void setIdPuntoGeografico(Long idPuntoGeografico) {
        this.idPuntoGeografico = idPuntoGeografico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
