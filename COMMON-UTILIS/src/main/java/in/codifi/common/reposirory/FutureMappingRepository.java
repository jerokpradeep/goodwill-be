package in.codifi.common.reposirory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.common.entity.FutureMappingEntity;

public interface FutureMappingRepository extends JpaRepository<FutureMappingEntity, Long> {

	/**
	 * method to find all by active status
	 * 
	 * @author SOWMIYA
	 * @param activeStatus
	 * @return
	 */
	List<FutureMappingEntity> findAllByFutureId(int futureId);

	@Query("SELECT DISTINCT p.futureId FROM TBL_FUTURES_DATA_MAP p")
	List<Integer> findDistinctByFutureId();

	/**
	 * method to find by exch,symbol,insType
	 * 
	 * @author SOWMIYA
	 * 
	 * @param symbol
	 * @param exch
	 * @param insType
	 * @return
	 */
	FutureMappingEntity findByExchAndSymbolAndInsType(String symbol, String exch, String insType);

	/**
	 * method to update future details
	 * 
	 * @author SOWMIYA
	 * @param ids
	 * @param userId
	 * @param activeStatus
	 * @return
	 */
	@Modifying
	@Query(value = "UPDATE TBL_FUTURES_DATA_MAP set ACTIVE_STATUS =:activeStatus, UPDATED_BY =:userId WHERE ID IN (:ids)")
	int updateActiveStatus(@Param("ids") List<String> ids, @Param("userId") String userId,
			@Param("activeStatus") int activeStatus);

	/**
	 * method to find all by active status
	 * 
	 * @author SOWMIYA
	 * @param activeStatus
	 * @return
	 */
	List<FutureMappingEntity> findAllByActiveStatus(int activeStatus);

}
