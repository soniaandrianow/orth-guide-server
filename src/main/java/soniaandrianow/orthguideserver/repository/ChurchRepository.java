package soniaandrianow.orthguideserver.repository;

import soniaandrianow.orthguideserver.domain.Church;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Church entity.
 */
@SuppressWarnings("unused")
public interface ChurchRepository extends JpaRepository<Church,Long> {

}
