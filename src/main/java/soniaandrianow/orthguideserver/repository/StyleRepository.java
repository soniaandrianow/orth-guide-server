package soniaandrianow.orthguideserver.repository;

import soniaandrianow.orthguideserver.domain.Style;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Style entity.
 */
@SuppressWarnings("unused")
public interface StyleRepository extends JpaRepository<Style,Long> {

}
