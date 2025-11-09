package uniandes.edu.co.proyecto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uniandes.edu.co.proyecto.modelo.UsuarioServicio;
import uniandes.edu.co.proyecto.repositorio.UsuarioServicioRepository;
import java.util.List;

@RestController
@RequestMapping("/api/usuarioservicios")
public class UsuarioServicioController {

    private final UsuarioServicioRepository repo;

    public UsuarioServicioController(UsuarioServicioRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<UsuarioServicio> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{documento}")
    public UsuarioServicio getById(@PathVariable Long documento) {
        return repo.findById(documento).orElse(null);
    }
}
