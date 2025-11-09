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
import uniandes.edu.co.proyecto.modelo.Vehiculo;
import uniandes.edu.co.proyecto.repositorio.VehiculoRepository;
import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoRepository repo;

    public VehiculoController(VehiculoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<List<Vehiculo>> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{placa}")
    public ResponseEntity<Vehiculo> getById(@PathVariable String placa) {
        return repo.findById(placa).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Vehiculo> create(@Valid @RequestBody Vehiculo v) {
        Vehiculo saved = repo.save(v);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{placa}")
    public ResponseEntity<Vehiculo> update(@PathVariable String placa, @Valid @RequestBody Vehiculo v) {
        return repo.findById(placa).map(existing -> {
            v.setPlaca(placa);
            Vehiculo saved = repo.save(v);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{placa}")
    public ResponseEntity<Void> delete(@PathVariable String placa) {
        return repo.findById(placa).map(existing -> {
            repo.deleteById(placa);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
