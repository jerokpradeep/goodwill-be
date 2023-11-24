package in.codifi.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import in.codifi.api.entity.primary.MarketWatchNameDTO;

public interface MarketWatchNameRepository extends CrudRepository<MarketWatchNameDTO, Long> {
//	@Transactional
//	@Query(value = " SELECT A.mwName as mwName , A.userId as userId, A.mwId as mwId,"
//			+ " B.formattedName as formattedInsName, B.ex as exchange, B.exSeg as segment, "
//			+ " B.token as token, B.symbol as symbol,B.tradingSymbol as tradingSymbol, B.expDt as expiry, B.pdc as pdc , "
//			+ " case when B.sortingOrder is null then 0 else B.sortingOrder end as sortOrder FROM"
//			+ " TBL_MARKET_WATCH_NAME as A  LEFT JOIN A.mwDetailsDTO B on  A.mwId = B.mwId and A.userId = B.userId "
//			+ " where A.userId = :pUserId  order by A.userId, A.mwId , B.sortingOrder")
//	List<IMwTblResponse> getUserScripDetails(@Param("pUserId") String pUserId);

	List<MarketWatchNameDTO> findAllByUserId(String pUserId);

	MarketWatchNameDTO findAllByUserIdAndMwId(String pUserId, int mwid);

//	@Transactional
//	@Query(value = " SELECT A.mwName as mwName , A.userId as userId, A.mwId as mwId,"
//			+ " B.formattedName as formattedInsName, B.ex as exchange, B.exSeg as segment, "
//			+ " B.token as token, B.symbol as symbol,B.tradingSymbol as tradingSymbol, B.expDt as expiry, B.pdc as pdc,"
//			+ " case when B.sortingOrder is null then 0 else B.sortingOrder end as sortOrder FROM"
//			+ " TBL_MARKET_WATCH_NAME as A  LEFT JOIN A.mwDetailsDTO B on  A.mwId = B.mwId and A.userId = B.userId "
//			+ " order by A.userId, A.mwId , B.sortingOrder")
//	List<IMwTblResponse> getUserScripDetailsForAllUser();

	@Modifying
	@Query(value = "UPDATE TBL_MARKET_WATCH_NAME SET mwName = :mwName WHERE mwId = :mwId AND userId = :userId")
	int updateMWName(@Param("mwName") String mwName, @Param("mwId") int mwId, @Param("userId") String userId);
}
