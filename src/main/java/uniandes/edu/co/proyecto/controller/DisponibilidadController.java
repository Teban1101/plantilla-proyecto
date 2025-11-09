package uniandes.edu.co.proyecto.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import uniandes.edu.co.proyecto.modelo.Disponibilidad;
import uniandes.edu.co.proyecto.repositorio.DisponibilidadRepository;
import java.util.List;

@RestController
@RequestMapping("/api/disponibilidades")
public class DisponibilidadController {

    private final DisponibilidadRepository repo;

    public DisponibilidadController(DisponibilidadRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<List<Disponibilidad>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disponibilidad> getById(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Disponibilidad> create(@Valid @RequestBody Disponibilidad d) {
        Disponibilidad saved = repo.save(d);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Disponibilidad> update(@PathVariable Long id, @Valid @RequestBody Disponibilidad d) {
        return repo.findById(id).map(existing -> {
            d.setIdDisponibilidad(id);
            Disponibilidad saved = repo.save(d);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return repo.findById(id).map(existing -> {
            repo.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
