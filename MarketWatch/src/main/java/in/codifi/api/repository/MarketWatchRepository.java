package in.codifi.api.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import in.codifi.api.entity.primary.MarketWatchScripDetailsDTO;

public interface MarketWatchRepository extends CrudRepository<MarketWatchScripDetailsDTO, Long> {
	@Modifying
	@Query(value = "DELETE FROM TBL_MARKET_WATCH  WHERE mwId = :mwId and userId = :pUserId and token = :token and ex = :exch ")
	long deleteScripFomDataBase(@Param("pUserId") String pUserId, @Param("exch") String exch,
			@Param("token") String token, @Param("mwId") int mwId);
}