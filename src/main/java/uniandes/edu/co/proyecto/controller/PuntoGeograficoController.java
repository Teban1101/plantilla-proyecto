package uniandes.edu.co.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import uniandes.edu.co.proyecto.modelo.PuntoGeografico;
import uniandes.edu.co.proyecto.repositorio.PuntoGeograficoRepository;

import java.util.List;

@RestController
@RequestMapping("/api/puntos")
public class PuntoGeograficoController {

    @Autowired
    private PuntoGeograficoRepository puntoRepository;

    @GetMapping
    public ResponseEntity<List<PuntoGeografico>> getAll() {
        return ResponseEntity.ok(puntoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PuntoGeografico> getById(@PathVariable Long id) {
        return puntoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PuntoGeografico> create(@Valid @RequestBody PuntoGeografico p) {
        // Assign id manually if DB (Oracle) doesn't generate it
        if (p.getIdPuntoGeografico() == null) {
            Long max = puntoRepository.findMaxId();
            p.setIdPuntoGeografico((max == null ? 1L : max + 1L));
        }
        PuntoGeografico saved = puntoRepository.save(p);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PuntoGeografico> update(@PathVariable Long id, @Valid @RequestBody PuntoGeografico p) {
        return puntoRepository.findById(id).map(existing -> {
            p.setIdPuntoGeografico(id);
            PuntoGeografico saved = puntoRepository.save(p);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return puntoRepository.findById(id).map(existing -> {
            puntoRepository.deleteById(id);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
