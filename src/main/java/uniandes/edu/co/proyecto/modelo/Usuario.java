package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @Column(name = "DOCUMENTO_USUARIO")
    @NotNull
    private Long documentoUsuario;

    @Column(name = "NOMBRE")
    @NotBlank
    private String nombre;

    @Column(name = "CORREO")
    private String correo;

    @Column(name = "CELULAR")
    private String celular;

    public Usuario() {
    }

    public Long getDocumentoUsuario() {
        return documentoUsuario;
    }

    public void setDocumentoUsuario(Long documentoUsuario) {
        this.documentoUsuario = documentoUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
