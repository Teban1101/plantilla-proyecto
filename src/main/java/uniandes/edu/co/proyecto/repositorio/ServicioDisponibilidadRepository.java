package uniandes.edu.co.proyecto.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uniandes.edu.co.proyecto.modelo.ServicioDisponibilidad;
import uniandes.edu.co.proyecto.modelo.ServicioDisponibilidadId;

@Repository
public interface ServicioDisponibilidadRepository extends JpaRepository<ServicioDisponibilidad, ServicioDisponibilidadId> {
}
