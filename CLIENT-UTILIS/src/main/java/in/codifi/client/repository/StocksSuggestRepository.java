package in.codifi.client.repository;

import java.util.List;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.client.entity.primary.StockSuggestEntity;

public interface StocksSuggestRepository extends JpaRepository<StockSuggestEntity, Long> {

	@ActivateRequestContext
	List<StockSuggestEntity> findByUserId(@Param("user_id") String user_id);

	@Transactional
	@Query(value = " select distinct userId from TBL_STOCK_SUGGEST ")
	List<String> getDistinctUserId();

}
