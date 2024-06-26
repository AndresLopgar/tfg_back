package andres.art_connect.repos;
 
import andres.art_connect.domain.Publicacion;
import andres.art_connect.domain.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {

    Publicacion findFirstByIdUsuario(Usuario usuario);

	List<Publicacion> findAllByIdCompania(Long idCompania);

	List<Publicacion> findAllByIdUsuario(Usuario usuario);
	
	@Query("SELECT p FROM Publicacion p LEFT JOIN FETCH p.comentarios")
    List<Publicacion> findAllWithComentarios();

}
