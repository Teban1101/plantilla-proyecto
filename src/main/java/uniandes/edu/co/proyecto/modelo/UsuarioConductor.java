package uniandes.edu.co.proyecto.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USUARIOCONDUCTOR")
public class UsuarioConductor {

    @Id
    @Column(name = "DOCUMENTO_USUARIO")
    private Long documentoUsuario;

    @Column(name = "HABILITADO")
    private String habilitado;

    public UsuarioConductor() {
    }

    public Long getDocumentoUsuario() {
        return documentoUsuario;
    }

    public void setDocumentoUsuario(Long documentoUsuario) {
        this.documentoUsuario = documentoUsuario;
    }

    public String getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(String habilitado) {
        this.habilitado = habilitado;
    }
}
