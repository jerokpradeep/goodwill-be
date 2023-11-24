package in.codifi.alerts.repository;

import java.sql.Timestamp;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.alerts.entity.primary.AlertsEntity;

public interface AlertsRepository extends JpaRepository<AlertsEntity, Long> {

	/**
	 * method to get Alert Details
	 *
	 * @author Gowthaman M
	 * @return
	 */
	@Transactional
	@Query(value = " SELECT a FROM TBL_ALERT_DETAILS a WHERE a.userId = :userId and a.activeStatus = 1 ")
	List<AlertsEntity> getAlertDetails(@Param("userId") String userId);

	/**
	 * method to update Alert active status
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Transactional
	@Modifying
	@Query(value = " update TBL_ALERT_DETAILS set ACTIVE_STATUS = 0 where ID = :id ")
	int updateActiveStatus(@Param("id") long id);

	/**
	 * method to get Alert Details
	 *
	 * @author Gowthaman M
	 * @return
	 */
	@Transactional
	@Query(value = " SELECT a FROM TBL_ALERT_DETAILS a WHERE a.id = :id and a.activeStatus = 1 ")
	List<AlertsEntity> getTriggerIdDetails(@Param("id") long triggerId);

	/**
	 * method to update Trigger Status
	 *
	 * @author Gowthaman M
	 * @return
	 */
	@Transactional
	@Modifying
	@Query(value = " update TBL_ALERT_DETAILS set TRIGGER_STATUS = 1, TRIGGERED_TIME = :triggeredTime  where ID = :id ")
	void updateTriggerStatus(@Param("id") long triggerId, @Param("triggeredTime") Timestamp triggerTimestamp);

}
