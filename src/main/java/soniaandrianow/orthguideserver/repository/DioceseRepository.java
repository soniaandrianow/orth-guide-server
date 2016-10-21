package soniaandrianow.orthguideserver.repository;

import soniaandrianow.orthguideserver.domain.Diocese;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Diocese entity.
 */
@SuppressWarnings("unused")
public interface DioceseRepository extends JpaRepository<Diocese,Long> {

}
