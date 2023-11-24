package in.codifi.api.repository;

import java.util.List;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.api.entity.primary.TickerTapeEntity;

public interface TickerTapeRepository extends JpaRepository<TickerTapeEntity, Long> {

	@ActivateRequestContext
	List<TickerTapeEntity> findByUserId(@Param("user_id") String user_id);

	@Transactional
	@Query(value = " select distinct userId from TBL_TICKER_TAPE ")
	List<String> getDistinctUserId();

}
