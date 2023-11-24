package in.codifi.notify.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.notify.entity.PopUpNotificationEntity;

public interface PopUpNotificationRepository extends JpaRepository<PopUpNotificationEntity, Long> {

	@Transactional
	@Query(value = "SELECT a FROM TBL_POPUP_NOTIFICATION a WHERE a.userType = 'all'  or a.id in (:id)  AND a.activeStatus = 1  ORDER BY a.id ASC")
	List<PopUpNotificationEntity> getpopUpMessage(@Param("id") List<Long> popUpid);

	@Transactional
	@Query(value = "SELECT a FROM TBL_POPUP_NOTIFICATION a WHERE a.userType = 'all' ")
	List<PopUpNotificationEntity> getpopUpMessageForAll();
}
