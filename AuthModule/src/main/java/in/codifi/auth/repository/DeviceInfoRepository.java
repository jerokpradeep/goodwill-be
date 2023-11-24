package in.codifi.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.auth.entity.primary.DeviceInfoEntity;

public interface DeviceInfoRepository extends JpaRepository<DeviceInfoEntity, Long> {

	/**
	 * method to find by user id
	 * 
	 * @author sowmiya
	 * @param userId
	 * @return
	 */
	DeviceInfoEntity findByUserIdAndActiveStatus(@Param("userId") String userId,
			@Param("activeStatus") int activeStatus);

	/**
	 * method to update active status 0
	 * 
	 * @author sowmiya
	 * @param userId
	 * @return
	 */
	@Query(value = "update tbl_device_info set active_status = :activeStatus where user_id =:userId")
	int updateDeviceInfo(@Param("userId") String userId, @Param("activeStatus") int activeStatus);

	/**
	 * method to find by user id and unique id and type
	 * 
	 * @author sowmiya
	 * @param userId
	 * @param uniqueId
	 * @param type
	 * @return
	 */
	DeviceInfoEntity findByUserIdAndUniqueIdAndType(@Param("userId") String userId, @Param("uniqueId") String uniqueId,
			@Param("type") String type);

	DeviceInfoEntity findByUniqueIdAndTypeAndActiveStatus(@Param("uniqueId") String uniqueId,
			@Param("type") String type, @Param("activeStatus") int activeStatus);

}
