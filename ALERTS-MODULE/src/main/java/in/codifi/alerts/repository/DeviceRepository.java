package in.codifi.alerts.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.alerts.entity.primary.DeviceEntity;

public interface DeviceRepository extends JpaRepository<DeviceEntity, Long> {

	/**
	 * Method to get device id
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Transactional
	@Query(value = "SELECT a FROM TBL_DEVICE_MAPPING a WHERE a.userId = :user_id")
	List<DeviceEntity> getDeviceIdForUser(@Param("user_id") String pUserId);

}
