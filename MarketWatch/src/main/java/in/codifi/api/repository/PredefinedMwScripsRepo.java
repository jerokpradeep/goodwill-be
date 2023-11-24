package in.codifi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.api.entity.primary.PredefinedMwScripsEntity;

public interface PredefinedMwScripsRepo extends JpaRepository<PredefinedMwScripsEntity, Long> {

	@Modifying
	@Query(value = "DELETE FROM TBL_PRE_DEFINED_MARKET_WATCH_SCRIPS WHERE mwId = :mwId and mwName = :mwName and token = :token and exch = :exch ")
	long deleteScripFomDataBase(@Param("mwName") String mwName, @Param("exch") String exch,
			@Param("token") String token, @Param("mwId") int mwId);

}
