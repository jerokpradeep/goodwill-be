package in.codifi.notify.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

//import in.codifi.notify.config.HazelcastConfig;
import in.codifi.notify.entity.PopUpNotificationEntity;
import in.codifi.notify.entity.PopUpNotificationUserEntity;
import in.codifi.notify.model.request.PopUpGetRequest;
import in.codifi.notify.model.request.PopUpRequest;
import in.codifi.notify.model.response.GenericResponse;
import in.codifi.notify.model.response.PopupResp;
import in.codifi.notify.repository.PopUpNotificationRepository;
import in.codifi.notify.repository.popUpNotificationUserRepository;
import in.codifi.notify.service.spec.PopUpNotificationServiceSpec;
import in.codifi.notify.utils.AppConstants;
import in.codifi.notify.utils.PrepareResponse;
import in.codifi.notify.utils.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class PopUpNotificationService implements PopUpNotificationServiceSpec {

	@Inject
	PopUpNotificationRepository popUpnotificationrepository;
	@Inject
	popUpNotificationUserRepository popUpnotificationUserrepository;
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * method to insert pop-up notification
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> insertPopUpNotification(PopUpRequest reqModel) {
		try {
			if (!validateNotifyParams(reqModel))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);

			PopUpNotificationEntity popup = preparepopUpMessage(reqModel);
			if ((reqModel.getUserType().equalsIgnoreCase("all"))) {
				popUpnotificationrepository.saveAndFlush(popup);
				return prepareResponse.prepareSuccessResponseObject(popup);
			} else if (reqModel.getUserType().equalsIgnoreCase("individual")
					&& (StringUtil.isListNotNullOrEmpty(reqModel.getUserId()))) {

				PopUpNotificationEntity notification = popUpnotificationrepository.saveAndFlush(popup);
				if (notification != null) {
					List<PopUpNotificationUserEntity> myList = new ArrayList<PopUpNotificationUserEntity>();
					for (String user : reqModel.getUserId()) {
						PopUpNotificationUserEntity singleUser = new PopUpNotificationUserEntity();
						singleUser.setPopupId(notification.getId());
						singleUser.setUserId(user);
						myList.add(singleUser);
					}

					popUpnotificationUserrepository.saveAll(myList);
				}
				return prepareResponse.prepareSuccessResponseObject(popup);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			}

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

	/**
	 * method to validate pop-up notification parameters
	 * 
	 * @author LOKESH
	 * @return
	 */

	private boolean validateNotifyParams(PopUpRequest reqModel) {
		if (StringUtil.isListNotNullOrEmpty(reqModel.getMessage())
				&& StringUtil.isNotNullOrEmpty(reqModel.getMessageTitle())
				&& StringUtil.isNotNullOrEmpty(reqModel.getUserType()) && (reqModel.getStatus() >= 0)
				&& (reqModel.getOkExist() >= 0) && (reqModel.getCancelExist() >= 0) && (reqModel.getSourceExist() >= 0)
				&& StringUtil.isNotNullOrEmpty(reqModel.getOkTitle())
				&& StringUtil.isNotNullOrEmpty(reqModel.getCancelTitle())
				&& StringUtil.isNotNullOrEmpty(reqModel.getSourceMsg())
				&& StringUtil.isNotNullOrEmpty(reqModel.getSourceLink())
				&& StringUtil.isNotNullOrEmpty(reqModel.getDestination())
				&& StringUtil.isNotNullOrEmpty(reqModel.getDisplayType()) && (reqModel.getSsoLogin() >= 0)) {
			return true;
		}
		return false;
	}

	/**
	 * method to prepare pop-up notification
	 * 
	 * @author LOKESH
	 * @return
	 */

	private PopUpNotificationEntity preparepopUpMessage(PopUpRequest reqModel) {
		PopUpNotificationEntity popup = new PopUpNotificationEntity();
		try {
			popup.setUserType(reqModel.getUserType());
			// popup.setMessage(reqModel.getMessage());
			popup.setMessage(reqModel.getMessage().get(0));
			popup.setMessageTitle(reqModel.getMessageTitle());
			popup.setStatus(reqModel.getStatus());
			popup.setOkExist(reqModel.getOkExist());
			popup.setOkTitle(reqModel.getOkTitle());
			popup.setCancelExist(reqModel.getCancelExist());
			popup.setCancelTitle(reqModel.getCancelTitle());
			popup.setSourceExist(reqModel.getSourceExist());
			popup.setSourceLink(reqModel.getSourceLink());
			popup.setSourceMsg(reqModel.getSourceMsg());
			popup.setDestination(reqModel.getDestination());
			popup.setDisplayType(reqModel.getDisplayType());
//			popup.setSsoLogin(reqModel.getSsoLogin());
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}

		return popup;
	}

	/**
	 * method to get pop-up notification
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPopUpNotification(PopUpGetRequest getReqModel) {
		try {
			if (StringUtil.isNullOrEmpty(getReqModel.getUserId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);

			List<Long> popUpid = popUpnotificationUserrepository.getPopupNotifications(getReqModel.getUserId());

			if (StringUtil.isListNullOrEmpty(popUpid) || popUpid.size() <= 0) {
				List<PopUpNotificationEntity> forAll = popUpnotificationrepository.getpopUpMessageForAll();
				List<PopupResp> response = new ArrayList<>();
//				String[] msgString = new String[forAll.size()];
				// new msgString[forAll.size() +1]
				for (int i = 0; i < forAll.size(); i++) {
					PopupResp result = new PopupResp();
//					List<String> message = new ArrayList<>();
//					message.add(forAll.get(i).getMessage());
					if (StringUtil.isEqual(forAll.get(i).getUserType(), "all")) {
						result.setMessage(StringUtil.split(forAll.get(i).getMessage(), "@"));
					} else {
						String[] msgString = new String[1];
						msgString[0] = forAll.get(i).getMessage();
						result.setMessage(msgString);
					}
//					result.setMessage(forAll.get(i).getMessage());
					result.setCancelExist(forAll.get(i).getCancelExist());
					result.setCancelTitle(forAll.get(i).getCancelTitle());
					result.setDestination(forAll.get(i).getDestination());
					result.setDisplayType(forAll.get(i).getDisplayType());
					result.setMessageTitle(forAll.get(i).getMessageTitle());
					result.setOkExist(forAll.get(i).getOkExist());
					result.setOkTitle(forAll.get(i).getOkTitle());
					result.setSourceExist(forAll.get(i).getSourceExist());
					result.setSourceLink(forAll.get(i).getSourceLink());
					result.setSourceMsg(forAll.get(i).getSourceMsg());
					result.setStatus(forAll.get(i).getStatus());
					result.setUserType(forAll.get(i).getUserType());
//					result.setSsoLogin(forAll.get(i).getSsoLogin());
					response.add(result);
				}
				return prepareResponse.prepareSuccessResponseObject(response);
			} else {
				List<PopUpNotificationEntity> message = popUpnotificationrepository.getpopUpMessage(popUpid);
				if (StringUtil.isListNullOrEmpty(message) || message.size() <= 0)
					return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);

				List<PopupResp> response = new ArrayList<>();
//				String[] msgString = new String[message.size()];
				for (int i = 0; i < message.size(); i++) {
					PopupResp result = new PopupResp();
//					List<String> messages = new ArrayList<>();
//					messages.add(message.get(i).getMessage());

					if (StringUtil.isEqual(message.get(i).getUserType(), "all")) {
						result.setMessage(StringUtil.split(message.get(i).getMessage(), "@"));
					} else {
						String[] msgString = new String[1];
						msgString[0] = message.get(i).getMessage();
						result.setMessage(msgString);
					}
					result.setCancelExist(message.get(i).getCancelExist());
					result.setCancelTitle(message.get(i).getCancelTitle());
					result.setDestination(message.get(i).getDestination());
					result.setDisplayType(message.get(i).getDisplayType());
					result.setMessageTitle(message.get(i).getMessageTitle());
					result.setOkExist(message.get(i).getOkExist());
					result.setOkTitle(message.get(i).getOkTitle());
					result.setSourceExist(message.get(i).getSourceExist());
					result.setSourceLink(message.get(i).getSourceLink());
					result.setSourceMsg(message.get(i).getSourceMsg());
					result.setStatus(message.get(i).getStatus());
					result.setUserType(message.get(i).getUserType());
//					result.setSsoLogin(message.get(i).getSsoLogin());
					response.add(result);
				}
				return prepareResponse.prepareSuccessResponseObject(response);
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

}
