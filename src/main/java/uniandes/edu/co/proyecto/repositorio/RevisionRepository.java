package uniandes.edu.co.proyecto.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uniandes.edu.co.proyecto.modelo.Revision;

@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long> {

	@Query("SELECT COALESCE(MAX(r.idRevision), 0) FROM Revision r")
	Long findMaxId();
}
