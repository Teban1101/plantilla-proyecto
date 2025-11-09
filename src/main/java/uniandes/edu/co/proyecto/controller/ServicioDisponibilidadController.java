package uniandes.edu.co.proyecto.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uniandes.edu.co.proyecto.modelo.ServicioDisponibilidad;
import uniandes.edu.co.proyecto.modelo.ServicioDisponibilidadId;
import uniandes.edu.co.proyecto.repositorio.ServicioDisponibilidadRepository;
import java.util.List;

@RestController
@RequestMapping("/api/servicio-disponibilidades")
public class ServicioDisponibilidadController {

    private final ServicioDisponibilidadRepository repo;

    public ServicioDisponibilidadController(ServicioDisponibilidadRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<ServicioDisponibilidad> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{idServicio}/{idDisponibilidad}")
    public ServicioDisponibilidad getById(@PathVariable Long idServicio, @PathVariable Long idDisponibilidad) {
        ServicioDisponibilidadId id = new ServicioDisponibilidadId();
        id.setIdServicio(idServicio);
        id.setIdDisponibilidad(idDisponibilidad);
        return repo.findById(id).orElse(null);
    }
}
