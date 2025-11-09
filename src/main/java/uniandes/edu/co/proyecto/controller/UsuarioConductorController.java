package uniandes.edu.co.proyecto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uniandes.edu.co.proyecto.modelo.UsuarioConductor;
import uniandes.edu.co.proyecto.repositorio.UsuarioConductorRepository;
import java.util.List;

@RestController
@RequestMapping("/api/usuarioconductores")
public class UsuarioConductorController {

    private final UsuarioConductorRepository repo;

    public UsuarioConductorController(UsuarioConductorRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<UsuarioConductor> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{documento}")
    public UsuarioConductor getById(@PathVariable Long documento) {
        return repo.findById(documento).orElse(null);
    }
}
