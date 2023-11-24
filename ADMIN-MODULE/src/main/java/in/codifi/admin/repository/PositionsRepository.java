package in.codifi.admin.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.admin.entity.PositionAvgPriceEntity;

public interface PositionsRepository extends JpaRepository<PositionAvgPriceEntity, Long> {

	@Transactional
	@Query(value = " select distinct clientId from TBL_POSITION_AVG_PRICE")
	List<String> getUserId();

	List<PositionAvgPriceEntity> findAllByClientId(@Param("client_id") String clientId);

	@Query(value = "select distinct exchange from TBL_POSITION_AVG_PRICE")
	List<String> getDistinctExch();

	@Query(value = "SELECT count(*) FROM TBL_POSITION_AVG_PRICE where EXCHANGE = :exch")
	long getPositionCountByExch(@Param("exch") String exch);
}
