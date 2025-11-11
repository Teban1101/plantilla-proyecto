package uniandes.edu.co.proyecto.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    public ResponseEntity<Ciudad> create(@RequestBody Ciudad c) {
        Ciudad saved = repo.save(c);
        return ResponseEntity.status(201).body(saved);
    }
}
