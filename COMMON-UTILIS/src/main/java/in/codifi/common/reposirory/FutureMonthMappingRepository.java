package in.codifi.common.reposirory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.common.entity.FutureMonthMappingEntity;

public interface FutureMonthMappingRepository extends JpaRepository<FutureMonthMappingEntity, Long> {

	/**
	 * method to find by exch and symbol , instype
	 * 
	 * @author SOWMIYA
	 * 
	 * @param symbol
	 * @param exch
	 * @param insType
	 * @return
	 */
	FutureMonthMappingEntity findByExchAndSymbolAndInsType(String symbol, String exch, String insType);

	/**
	 * method to update future month mapping table
	 * 
	 * @author SOWMIYA
	 * 
	 * @param ids
	 * @param userId
	 * @param activeStatus
	 * @return
	 */
	@Modifying
	@Query(value = "UPDATE TBL_FUTURE_MONTH_DATA_MAP set ACTIVE_STATUS =:activeStatus, UPDATED_BY =:userId WHERE ID IN (:ids)")
	int updateActiveStatus(@Param("ids") List<String> ids, @Param("userId") String userId,
			@Param("activeStatus") int activeStatus);

	/**
	 * method to find by active status
	 * 
	 * @author SOWMIYA
	 * @param activeStatus
	 * @return
	 */
	List<FutureMonthMappingEntity> findAllByActiveStatus(int activeStatus);

}
