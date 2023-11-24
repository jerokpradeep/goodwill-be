package in.codifi.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.admin.entity.HoldingsEntity;

public interface HoldingRepository extends JpaRepository<HoldingsEntity, Long> {

	/**
	 * method to find all by userid and createdOn
	 * 
	 * @author SOWMIYA
	 * @param userId
	 * @param date
	 * @return
	 */
//	@Query(value = "SELECT userId,holdingsType,isin,qty,collateralQty,haircut,brokerCollQty,dpQty,closePrice,benQty,unpledgeQy,product,actualPrice,poaStatus,authFlag,authQty,reqId,txnId from TBL_HOLDINGS_DATA WHERE userId = :user_id AND createdOn LIKE :created_on")
//	List<HoldingsEntity> getHoldingsData(@Param("user_id") String user_id, @Param("created_on") Date date);

	@Query(value = "SELECT a from TBL_HOLDINGS_DATA a WHERE a.userId = :user_id")
	List<HoldingsEntity> getHoldingsData(@Param("user_id") String user_id);
}
