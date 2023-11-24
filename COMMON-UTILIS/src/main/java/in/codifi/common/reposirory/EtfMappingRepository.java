package in.codifi.common.reposirory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.common.entity.EtfMappingEntity;

public interface EtfMappingRepository extends JpaRepository<EtfMappingEntity, Long> {

	@Modifying
	@Query(value = "UPDATE TBL_ETF_DATA_MAP set ACTIVE_STATUS =:activeStatus, UPDATED_BY =:userId WHERE ID IN (:ids)")
	void updateActiveStatus(@Param("ids") List<String> ids, @Param("userId") String userId,
			@Param("activeStatus") int activeStatus);

	List<EtfMappingEntity> findAllByActiveStatus(int activeStatus);

	@Query("SELECT DISTINCT p.etfId FROM TBL_ETF_DATA_MAP p")
	List<Integer> findDistinctByEtfId();

	List<EtfMappingEntity> findAllByEtfIdAndActiveStatus(int etfId, int activeStatus);

	EtfMappingEntity findByScrips(String scrips);
}
