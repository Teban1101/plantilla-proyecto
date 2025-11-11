package uniandes.edu.co.proyecto.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uniandes.edu.co.proyecto.modelo.UsuarioConductor;
import uniandes.edu.co.proyecto.modelo.Usuario;
import uniandes.edu.co.proyecto.repositorio.UsuarioConductorRepository;
import uniandes.edu.co.proyecto.repositorio.UsuarioRepository;
import java.util.List;

@RestController
@RequestMapping("/api/usuarioconductores")
public class UsuarioConductorController {

    private final UsuarioConductorRepository repo;
    private final UsuarioRepository usuarioRepo;

    public UsuarioConductorController(UsuarioConductorRepository repo, UsuarioRepository usuarioRepo) {
        this.repo = repo;
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping
    public List<UsuarioConductor> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{documento}")
    public UsuarioConductor getById(@PathVariable Long documento) {
        return repo.findById(documento).orElse(null);
    }

    @PostMapping
    public ResponseEntity<UsuarioConductor> create(@RequestBody UsuarioConductor u) {
        // Ensure base USUARIO exists (FK constraint in DB). If not, create it from provided data.
        Long doc = u.getDocumentoUsuario();
        if (doc != null && !usuarioRepo.existsById(doc)) {
            Usuario base = new Usuario();
            base.setDocumentoUsuario(doc);
            // Provide minimal valid data to satisfy validation constraints
            base.setNombre("AutoUsuario" + doc);
            base.setCorreo("auto" + doc + "@example.com");
            base.setCelular("0000000000");
            usuarioRepo.save(base);
        }
        UsuarioConductor saved = repo.save(u);
        return ResponseEntity.status(201).body(saved);
    }
}
