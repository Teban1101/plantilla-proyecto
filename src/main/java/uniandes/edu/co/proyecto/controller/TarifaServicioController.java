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
import uniandes.edu.co.proyecto.modelo.TarifaServicio;
import uniandes.edu.co.proyecto.repositorio.TarifaServicioRepository;
import java.util.List;

@RestController
@RequestMapping("/api/tarifas")
public class TarifaServicioController {

    private final TarifaServicioRepository repo;

    public TarifaServicioController(TarifaServicioRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<List<TarifaServicio>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{tipo}")
    public ResponseEntity<TarifaServicio> getById(@PathVariable String tipo) {
        return repo.findById(tipo).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TarifaServicio> create(@Valid @RequestBody TarifaServicio t) {
        TarifaServicio saved = repo.save(t);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{tipo}")
    public ResponseEntity<TarifaServicio> update(@PathVariable String tipo, @Valid @RequestBody TarifaServicio t) {
        return repo.findById(tipo).map(existing -> {
            t.setTipoServicio(tipo);
            TarifaServicio saved = repo.save(t);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{tipo}")
    public ResponseEntity<Void> delete(@PathVariable String tipo) {
        return repo.findById(tipo).map(existing -> {
            repo.deleteById(tipo);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
