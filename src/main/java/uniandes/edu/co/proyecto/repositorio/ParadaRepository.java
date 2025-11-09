package uniandes.edu.co.proyecto.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uniandes.edu.co.proyecto.modelo.Parada;
import uniandes.edu.co.proyecto.modelo.ParadaId;

@Repository
public interface ParadaRepository extends JpaRepository<Parada, ParadaId> {
}
