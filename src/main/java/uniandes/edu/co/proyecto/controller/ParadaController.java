package uniandes.edu.co.proyecto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uniandes.edu.co.proyecto.modelo.Parada;
import uniandes.edu.co.proyecto.modelo.ParadaId;
import uniandes.edu.co.proyecto.repositorio.ParadaRepository;
import java.util.List;

@RestController
@RequestMapping("/api/paradas")
public class ParadaController {

    private final ParadaRepository repo;

    public ParadaController(ParadaRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Parada> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{idServicio}/{idPunto}")
    public Parada getById(@PathVariable Long idServicio, @PathVariable Long idPunto) {
        ParadaId id = new ParadaId();
        id.setIdServicio(idServicio);
        id.setIdPuntoGeografico(idPunto);
        return repo.findById(id).orElse(null);
    }
}
