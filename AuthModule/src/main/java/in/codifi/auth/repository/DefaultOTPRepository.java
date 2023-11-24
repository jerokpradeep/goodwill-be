package in.codifi.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import in.codifi.auth.entity.primary.DefaultOTPEntity;

public interface DefaultOTPRepository extends JpaRepository<DefaultOTPEntity, Long> {

	List<DefaultOTPEntity> findAllByActiveStatus(@Param("active_status") int activeStatus);

}
