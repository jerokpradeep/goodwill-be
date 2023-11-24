package in.codifi.admin.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.admin.entity.DeviceMappingEntity;

public interface DeviceMappingRepository extends JpaRepository<DeviceMappingEntity, Long> {

	@Transactional
//	@Query(value = "SELECT userName, userId, deviceId, deviceType FROM TBL_DEVICE_MAPPING  where "
//			+ "userId IN (:userId)")
	@Query(value = "SELECT a FROM TBL_DEVICE_MAPPING a where a.userId IN (:userId)")
	List<DeviceMappingEntity> getByMultipleUserId(@Param("userId") List<String> userId);

	List<DeviceMappingEntity> findByUserId(@Param("userId") String[] userId);

}
