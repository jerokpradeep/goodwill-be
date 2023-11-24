package in.codifi.common.reposirory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.common.entity.EQSectorMappingEntity;

public interface EQMappingRepository extends JpaRepository<EQSectorMappingEntity, Long> {

	/**
	 * method to get distinct sector id
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Query("SELECT DISTINCT sectorId FROM TBL_EQSECTOR_DATA_MAP ")
	List<Integer> findByDistinctSectorId();

	/**
	 * method to find all by sector id and activestatus
	 * 
	 * @author SOWMIYA
	 * @param sectorId
	 * @param ativeStatus
	 * @return
	 */
	List<EQSectorMappingEntity> findAllBySectorIdAndActiveStatus(Integer sectorId, int ativeStatus);

	/**
	 * method to find by scrips
	 * 
	 * @author SOWMIYA
	 * @param scrips
	 * @return
	 */
	EQSectorMappingEntity findByScrips(String scrips);

	/**
	 * method to update eq sector scrips details
	 * 
	 * @author SOWMIYA
	 * @param ids
	 * @param userId
	 * @param activeStatus
	 * @return
	 */
	@Modifying
	@Query(value = "UPDATE TBL_EQSECTOR_DATA_MAP set ACTIVE_STATUS =:activeStatus, UPDATED_BY =:userId WHERE ID IN (:ids)")
	int updateActiveStatus(@Param("ids") List<String> ids, @Param("userId") String userId,
			@Param("activeStatus") int activeStatus);

	/**
	 * method to find all by active status
	 * 
	 * @author SOWMIYA
	 * @param activeStatus
	 * @return
	 */
	List<EQSectorMappingEntity> findAllByActiveStatus(int activeStatus);

}
