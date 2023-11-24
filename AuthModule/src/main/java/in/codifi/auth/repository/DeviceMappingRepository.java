package in.codifi.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.auth.entity.primary.DeviceMappingEntity;

public interface DeviceMappingRepository extends JpaRepository<DeviceMappingEntity, Long> {

	/**
	 * 
	 * Method to find all by userId and deviceType
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @param deviceType
	 * @return
	 */
	DeviceMappingEntity findAllByDeviceIdAndDeviceTypeAndActiveStatus(String userId, String deviceType,
			int activeStatus);

}
