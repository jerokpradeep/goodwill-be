package in.codifi.common.reposirory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.common.entity.SectorHeatMappingEntity;

public interface SectorMappingRepository extends JpaRepository<SectorHeatMappingEntity, Long> {

	/**
	 * method to get distinct sector id
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Query("SELECT DISTINCT sectorId FROM TBL_SECTOR_HEATMAP_DATA_MAP ")
	List<Integer> findDistinctBySectorId();

	/**
	 * method to find all by sector id
	 * 
	 * @param sectorId
	 * @param ativeStatus
	 * @return
	 */
	List<SectorHeatMappingEntity> findAllBySectorIdAndActiveStatus(Integer sectorId, int ativeStatus);

	/**
	 * method to find all by scrips
	 * 
	 * @param scrips
	 * @return
	 */
	SectorHeatMappingEntity findByScrips(String scrips);

	/**
	 * method to update sector heatmap details
	 * 
	 * @param ids
	 * @param userId
	 * @param activeStatus
	 * @return
	 */
	@Modifying
	@Query(value = "UPDATE TBL_SECTOR_HEATMAP_DATA_MAP set ACTIVE_STATUS =:activeStatus, UPDATED_BY =:userId WHERE ID IN (:ids)")
	int updateActiveStatus(@Param("ids") List<String> ids, @Param("userId") String userId,
			@Param("activeStatus") int activeStatus);

	/**
	 * method to update sector heat map
	 * 
	 * @author SOWMIYA
	 * @param activeStatus
	 * @return
	 */
	List<SectorHeatMappingEntity> findAllByActiveStatus(int activeStatus);

}
