package uniandes.edu.co.proyecto.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import uniandes.edu.co.proyecto.modelo.UsuarioServicio;
import uniandes.edu.co.proyecto.repositorio.UsuarioServicioRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;

@RestController
@RequestMapping("/api/usuarioservicios")
public class UsuarioServicioController {

    private final UsuarioServicioRepository repo;
    private final JdbcTemplate jdbcTemplate;

    public UsuarioServicioController(UsuarioServicioRepository repo, JdbcTemplate jdbcTemplate) {
        this.repo = repo;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public List<UsuarioServicio> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{documento}")
    public UsuarioServicio getById(@PathVariable Long documento) {
        return repo.findById(documento).orElse(null);
    }

    @PostMapping
    public ResponseEntity<UsuarioServicio> create(@RequestBody UsuarioServicio usuarioServicio) {
        UsuarioServicio saved = repo.save(usuarioServicio);
        return ResponseEntity.status(201).body(saved);
    }

    // helper endpoint that accepts a generic JSON map and constructs the entity
    @PostMapping("/raw")
    public ResponseEntity<UsuarioServicio> createRaw(@RequestBody Map<String, Object> payload) {
        try {
            Object doc = payload.get("documentoUsuario");
            if (doc == null) return ResponseEntity.badRequest().build();
            UsuarioServicio u = new UsuarioServicio();
            u.setDocumentoUsuario(Long.parseLong(doc.toString()));
            if (payload.get("numeroTarjeta") != null) u.setNumeroTarjeta(payload.get("numeroTarjeta").toString());
            if (payload.get("nombreTarjeta") != null) u.setNombreTarjeta(payload.get("nombreTarjeta").toString());
            if (payload.get("codigoSeguridad") != null) u.setCodigoSeguridad(Integer.parseInt(payload.get("codigoSeguridad").toString()));
            repo.save(u);
            return ResponseEntity.status(201).body(u);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // even more permissive: accept raw JSON string and parse manually
    @PostMapping("/raw2")
    public ResponseEntity<UsuarioServicio> createRaw2(@RequestBody String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payload = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
            Object doc = payload.get("documentoUsuario");
            if (doc == null) return ResponseEntity.badRequest().build();
            UsuarioServicio u = new UsuarioServicio();
            u.setDocumentoUsuario(Long.parseLong(doc.toString()));
            if (payload.get("numeroTarjeta") != null) u.setNumeroTarjeta(payload.get("numeroTarjeta").toString());
            if (payload.get("nombreTarjeta") != null) u.setNombreTarjeta(payload.get("nombreTarjeta").toString());
            if (payload.get("codigoSeguridad") != null) u.setCodigoSeguridad(Integer.parseInt(payload.get("codigoSeguridad").toString()));
            repo.save(u);
            return ResponseEntity.status(201).body(u);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // convenience endpoint to create a UsuarioServicio with only the id (for tests)
    @PostMapping("/fix/{documento}")
    public ResponseEntity<UsuarioServicio> createWithId(@PathVariable Long documento) {
        try {
            int rows = 0;
            try {
                // insert both PK columns which in this DB are NOT NULL (DOCUMENTO_USUARIO and DOCUMENTO)
                rows = jdbcTemplate.update("INSERT INTO USUARIOSERVICIO (DOCUMENTO_USUARIO, DOCUMENTO) VALUES (?, ?)", documento, documento);
            } catch (Exception e) {
                try {
                    // fallback: try inserting into the pair but reversed column names if needed
                    rows = jdbcTemplate.update("INSERT INTO USUARIOSERVICIO (DOCUMENTO, DOCUMENTO_USUARIO) VALUES (?, ?)", documento, documento);
                } catch (Exception ex) {
                    // both attempts failed
                    return ResponseEntity.status(500).body(null);
                }
            }

            if (rows <= 0) {
                // nothing inserted (maybe already exists); verify presence
                // attempt a simple select to check
                List<UsuarioServicio> found = repo.findAll();
                boolean exists = found.stream().anyMatch(u -> documento.equals(u.getDocumentoUsuario()));
                if (!exists) return ResponseEntity.status(500).body(null);
            }

            UsuarioServicio u = new UsuarioServicio();
            u.setDocumentoUsuario(documento);
            return ResponseEntity.status(201).body(u);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Diagnostic: list columns of USUARIOSERVICIO table (useful to detect which PK column exists)
    @GetMapping("/diagnostics/columns")
    public ResponseEntity<List<Map<String,Object>>> getUsuarioServicioColumns() {
        try {
            List<Map<String,Object>> cols = jdbcTemplate.queryForList("SELECT COLUMN_NAME, NULLABLE, DATA_TYPE FROM USER_TAB_COLUMNS WHERE TABLE_NAME = 'USUARIOSERVICIO'");
            return ResponseEntity.ok(cols);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/diagnostics/has/{documento}")
    public ResponseEntity<Map<String,Object>> hasUsuarioServicio(@PathVariable Long documento) {
        try {
            Integer c1 = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM USUARIOSERVICIO WHERE DOCUMENTO_USUARIO = ?", Integer.class, documento);
            Integer c2 = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM USUARIOSERVICIO WHERE DOCUMENTO = ?", Integer.class, documento);
            Map<String,Object> res = Map.of("documento_usuario_count", c1, "documento_count", c2);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/diagnostics/count")
    public ResponseEntity<Map<String,Object>> countUsuarioServicio() {
        try {
            Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM USUARIOSERVICIO", Integer.class);
            return ResponseEntity.ok(Map.of("total", total));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // Force an INSERT using JdbcTemplate and return diagnostic info
    @PostMapping("/diagnostics/insert/{documento}")
    public ResponseEntity<Map<String,Object>> diagnosticInsert(@PathVariable Long documento) {
        try {
            int rows = jdbcTemplate.update("INSERT INTO USUARIOSERVICIO (DOCUMENTO_USUARIO, DOCUMENTO) VALUES (?, ?)", documento, documento);
            return ResponseEntity.ok(Map.of("rowsInserted", rows));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
}
