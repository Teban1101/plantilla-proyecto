package uniandes.edu.co.proyecto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uniandes.edu.co.proyecto.modelo.Revision;
import uniandes.edu.co.proyecto.repositorio.RevisionRepository;
import java.util.List;

@RestController
@RequestMapping("/api/revisiones")
public class RevisionController {

    private final RevisionRepository repo;

    public RevisionController(RevisionRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Revision> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Revision getById(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }
}
