package in.codifi.holdings.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.holdings.entity.primary.HoldingsEntity;

public interface HoldingRepository extends JpaRepository<HoldingsEntity, Long> {

	List<HoldingsEntity> findAllByUserId(@Param("userId") String userId);

	/**
	 * method to update request details
	 * 
	 * @param isin
	 * @param userId
	 * @param reqId
	 * @param txnId
	 * @param qty
	 * @param authFlag
	 */
	@Modifying
	@Query(value = " update TBL_HOLDINGS_DATA set reqId = :req_id , txnId = :txn_id , qty = 0 , authFlag = 0 where userId = :user_id and isin = :isin ")
	int updateReqList(@Param("user_id") String user_id, @Param("isin") String isin, @Param("req_id") String req_id,
			@Param("txn_id") String txn_id);

	/**
	 * method to poa status
	 * 
	 * @author SOWMIYA
	 * @param userId
	 * @return
	 */
//	@Query(value = "select e.poaStatus from TBL_HOLDINGS_DATA e where e.userId =:userId  limit 1")
//	String getPOAStatus(@Param("userId") String userId);

	/**
	 * method to update authflag and authqty
	 * 
	 * @author SOWMIYA
	 * @param isin
	 * @param userId
	 */
	@Modifying
	@Query(value = "update TBL_HOLDINGS_DATA set authFlag = 0 , authQty = 0 , updated_on = CURRENT_TIMESTAMP  where isin = :isin and userId = :user_id ")
	int updateAllRevocationStatus(@Param("isin") String isin, @Param("user_id") String user_id);

	/**
	 * method to get user id using
	 * 
	 * @param reId
	 * @return
	 */
	@Query(value = "select e.userId from TBL_HOLDINGS_DATA e where e.reqId = :reqId")
	List<String> getUserUsingReqId(String reqId);
	
	@Transactional
	@Query(value = " select userId from TBL_HOLDINGS_DATA where reqId = :reqId")
	List<String> getUserByReqId(String reqId);

}
