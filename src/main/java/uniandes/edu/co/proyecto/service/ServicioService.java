package uniandes.edu.co.proyecto.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniandes.edu.co.proyecto.modelo.Servicio;
import uniandes.edu.co.proyecto.modelo.TarifaServicio;
import uniandes.edu.co.proyecto.modelo.Usuario;
import uniandes.edu.co.proyecto.modelo.UsuarioConductor;
import uniandes.edu.co.proyecto.repositorio.ServicioRepository;
import uniandes.edu.co.proyecto.repositorio.TarifaServicioRepository;
import uniandes.edu.co.proyecto.repositorio.UsuarioConductorRepository;
import uniandes.edu.co.proyecto.repositorio.UsuarioRepository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final TarifaServicioRepository tarifaRepository;
    private final UsuarioConductorRepository usuarioConductorRepository;
    private final UsuarioRepository usuarioRepository;
    private final JdbcTemplate jdbcTemplate;

    public ServicioService(ServicioRepository servicioRepository,
                           TarifaServicioRepository tarifaRepository,
                           UsuarioConductorRepository usuarioConductorRepository,
                           UsuarioRepository usuarioRepository,
                           JdbcTemplate jdbcTemplate) {
        this.servicioRepository = servicioRepository;
        this.tarifaRepository = tarifaRepository;
        this.usuarioConductorRepository = usuarioConductorRepository;
        this.usuarioRepository = usuarioRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Servicio createServicio(Servicio s) {
        return createServicio(s, false);
    }

    // allow simulating an error for tests
    @Transactional
    public Servicio createServicio(Servicio s, boolean simulateError) {
        if (s.getIdServicio() == null) {
            Long max = servicioRepository.findMaxId();
            s.setIdServicio((max == null ? 1L : max + 1L));
        }

        // ensure tarifa exists
        if (s.getTipoServicio() != null && !tarifaRepository.existsById(s.getTipoServicio())) {
            TarifaServicio t = new TarifaServicio();
            t.setTipoServicio(s.getTipoServicio());
            t.setCostoPorKm(1000.0);
            tarifaRepository.save(t);
        }

        // pick an available conductor
        List<UsuarioConductor> conductores = usuarioConductorRepository.findAll();
        UsuarioConductor assigned = conductores.stream().filter(c -> "1".equals(c.getHabilitado())).findFirst().orElse(null);
        if (assigned == null) {
            // create minimal usuario for cliente if needed
            Long cliente = s.getClienteId();
            if (cliente != null && !usuarioRepository.existsById(cliente)) {
                Usuario base = new Usuario();
                base.setDocumentoUsuario(cliente);
                base.setNombre("AutoUser" + cliente);
                base.setCorreo("auto" + cliente + "@example.com");
                base.setCelular("0000000000");
                usuarioRepository.save(base);
            }
            UsuarioConductor uc = new UsuarioConductor();
            uc.setDocumentoUsuario(s.getClienteId() == null ? 0L : s.getClienteId());
            uc.setHabilitado("1");
            usuarioConductorRepository.save(uc);
            assigned = uc;
        }

        // mark assigned conductor as unavailable
        assigned.setHabilitado("0");
        usuarioConductorRepository.save(assigned);
        s.setConductorId(assigned.getDocumentoUsuario());

        // Ensure cliente exists in USUARIOSERVICIO table to satisfy FK; try both column variants.
        Long cliente = s.getClienteId();
        if (cliente != null) {
            try {
                jdbcTemplate.update("INSERT INTO USUARIOSERVICIO (DOCUMENTO_USUARIO, DOCUMENTO) VALUES (?, ?)", cliente, cliente);
            } catch (Exception e) {
                try {
                    jdbcTemplate.update("INSERT INTO USUARIOSERVICIO (DOCUMENTO, DOCUMENTO_USUARIO) VALUES (?, ?)", cliente, cliente);
                } catch (Exception ex) {
                    // ignore - if insert fails it may already exist or schema differs; we'll let the save fail later
                }
            }
        }

        // defaults for mandatory fields
        if (s.getHoraInicio() == null) s.setHoraInicio(LocalDateTime.now());
        if (s.getHoraFin() == null) s.setHoraFin(LocalDateTime.now());
        if (s.getDuracionMin() == null) s.setDuracionMin(0);
        if (s.getDistanciaKm() == null) s.setDistanciaKm(0.0);

        Servicio saved = servicioRepository.save(s);

        if (simulateError) {
            throw new RuntimeException("Simulated failure to test rollback");
        }

        return saved;
    }

    @Transactional
    public Servicio completeService(Long id, Servicio payload) {
        return servicioRepository.findById(id).map(existing -> {
            if (payload.getHoraFin() != null) existing.setHoraFin(payload.getHoraFin());
            if (payload.getDuracionMin() != null) existing.setDuracionMin(payload.getDuracionMin());
            if (payload.getDistanciaKm() != null) existing.setDistanciaKm(payload.getDistanciaKm());
            Servicio saved = servicioRepository.save(existing);

            // set conductor available again
            if (existing.getConductorId() != null && usuarioConductorRepository.existsById(existing.getConductorId())) {
                UsuarioConductor uc = usuarioConductorRepository.findById(existing.getConductorId()).get();
                uc.setHabilitado("1");
                usuarioConductorRepository.save(uc);
            }
            return saved;
        }).orElse(null);
    }
}
