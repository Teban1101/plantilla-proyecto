package uniandes.edu.co.proyecto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uniandes.edu.co.proyecto.modelo.Ciudad;
import uniandes.edu.co.proyecto.repositorio.CiudadRepository;
import java.util.List;

@RestController
@RequestMapping("/api/ciudades")
public class CiudadController {

    private final CiudadRepository repo;

    public CiudadController(CiudadRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Ciudad> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{nombre}")
    public Ciudad getById(@PathVariable String nombre) {
        return repo.findById(nombre).orElse(null);
    }
}
