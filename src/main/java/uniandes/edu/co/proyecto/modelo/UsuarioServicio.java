package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "USUARIOSERVICIO")
public class UsuarioServicio {

    @Id
    // DB contains a column named DOCUMENTO (seed scripts handle both variants),
    // map to DOCUMENTO so inserts populate the actual PK column and avoid ORA-01400.
    @Column(name = "DOCUMENTO")
    private Long documentoUsuario;

    @Column(name = "NUMEROTARJETA")
    private String numeroTarjeta;

    @Column(name = "NOMBRETARJETA")
    private String nombreTarjeta;

    @Column(name = "FECHAVENCIMIENTO")
    private Date fechaVencimiento;

    @Column(name = "CODIGOSEGURIDAD")
    private Integer codigoSeguridad;

    public UsuarioServicio() {
    }

    public Long getDocumentoUsuario() {
        return documentoUsuario;
    }

    public void setDocumentoUsuario(Long documentoUsuario) {
        this.documentoUsuario = documentoUsuario;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getNombreTarjeta() {
        return nombreTarjeta;
    }

    public void setNombreTarjeta(String nombreTarjeta) {
        this.nombreTarjeta = nombreTarjeta;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Integer getCodigoSeguridad() {
        return codigoSeguridad;
    }

    public void setCodigoSeguridad(Integer codigoSeguridad) {
        this.codigoSeguridad = codigoSeguridad;
    }
}
