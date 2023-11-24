package in.codifi.client.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.client.entity.primary.PreferenceMappingEntity;

public interface PreferenceMappingRepository extends JpaRepository<PreferenceMappingEntity, Long> {

	List<PreferenceMappingEntity> findBySource(@Param("source") String source);

	@Transactional
	@Query(value = " select distinct source from TBL_USER_PREFERENCES_MAPPING")
	List<String> getDistinctSource();

}
