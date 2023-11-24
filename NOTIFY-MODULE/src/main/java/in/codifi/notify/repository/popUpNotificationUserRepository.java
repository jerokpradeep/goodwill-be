package in.codifi.notify.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.notify.entity.PopUpNotificationUserEntity;

public interface popUpNotificationUserRepository extends JpaRepository<PopUpNotificationUserEntity, Long> {

	@Transactional
	@Query("SELECT popupId FROM TBL_POPUP_NOTIFICATION_USERS WHERE userId = :userId")
	List<Long> getPopupNotifications(@Param("userId") String userId);

}
