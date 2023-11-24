package in.codifi.position.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import in.codifi.position.entity.primary.PositionAvgPriceEntity;

public interface PositionsRepository extends JpaRepository<PositionAvgPriceEntity, Long> {

	List<PositionAvgPriceEntity> findAllByClientId(@Param("client_id") String clientId);
}
