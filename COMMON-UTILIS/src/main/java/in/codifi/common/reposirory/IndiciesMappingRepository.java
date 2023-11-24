package in.codifi.common.reposirory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.common.entity.IndicesMappingEntity;

public interface IndiciesMappingRepository extends JpaRepository<IndicesMappingEntity, Long> {

	@Modifying
	@Query(value = "UPDATE TBL_INDICES_DATA_MAP set ACTIVE_STATUS =:activeStatus, UPDATED_BY =:userId WHERE ID IN (:ids)")
	void updateActiveStatus(@Param("ids") List<String> ids, @Param("userId") String userId,
			@Param("activeStatus") int activeStatus);

	List<IndicesMappingEntity> findAllByActiveStatus(int activeStatus);

	IndicesMappingEntity findByScripsAndExchange(String scrips, String exchange);

	List<IndicesMappingEntity> findAllByExchange(String exchange);

	@Query("SELECT DISTINCT p.exchange FROM TBL_INDICES_DATA_MAP p")
	List<String> findDistinctByExchange();

}
