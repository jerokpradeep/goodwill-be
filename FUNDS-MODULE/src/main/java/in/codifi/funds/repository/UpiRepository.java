package in.codifi.funds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import in.codifi.funds.entity.primary.UpiDetailsEntity;

public interface UpiRepository extends JpaRepository<UpiDetailsEntity, Long> {

	UpiDetailsEntity findByUserId(@Param("userId") String userId);

}
