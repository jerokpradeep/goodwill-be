package in.codifi.admin.service.spec;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import in.codifi.admin.entity.UserNotification;
import in.codifi.admin.model.request.SendNoficationReqModel;

public interface UserNotificationSpec {

	void saveNotification(SendNoficationReqModel reqModel) throws JsonProcessingException;
	
	List<UserNotification> getNotificationList(String userId);
}
