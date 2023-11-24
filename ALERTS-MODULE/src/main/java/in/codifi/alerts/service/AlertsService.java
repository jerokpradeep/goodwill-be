package in.codifi.alerts.service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.alerts.config.HazelcastConfig;
import in.codifi.alerts.entity.primary.AlertsEntity;
import in.codifi.alerts.entity.primary.DeviceEntity;
import in.codifi.alerts.model.request.RequestModel;
import in.codifi.alerts.model.response.GenericResponse;
import in.codifi.alerts.repository.AlertsRepository;
import in.codifi.alerts.repository.DeviceRepository;
import in.codifi.alerts.service.spec.IAlertsService;
import in.codifi.alerts.utility.AppConstants;
import in.codifi.alerts.utility.AppUtil;
import in.codifi.alerts.utility.EmailUtils;
import in.codifi.alerts.utility.PrepareResponse;
import in.codifi.alerts.utility.StringUtil;
import in.codifi.alerts.ws.service.AlertsRestService;
import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.ws.model.kc.GetIntroSpectResponse;
import io.quarkus.logging.Log;

@ApplicationScoped
public class AlertsService implements IAlertsService {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AlertsRepository alertsRepository;
	@Inject
	DeviceRepository deviceRepository;
	@Inject
	AlertsRestService alertsRestService;
	@Inject
	HttpServletRequest httpServletRequest;
	@Inject
	AppUtil appUtil;
	@Inject
	EmailUtils emailUtils;

	/**
	 * method to create Alert
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> createAlert(RequestModel reqModel, ClinetInfoModel info) {
		try {
			/** Method to validate create alert request **/
			if (!validateCreateAlert(reqModel))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			/** Method to prepare create alert request **/
			AlertsEntity alertsEntity = prepareCreateAlert(reqModel, info);
			AlertsEntity createAlert = alertsRepository.save(alertsEntity);
			if (createAlert == null)
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
			/** Method to set alert request **/
			alertsRestService.setAlert(alertsEntity, info.getUserId());
			return prepareResponse.prepareSuccessResponseMessage(AppConstants.SUCCESS_STATUS);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		}
	}

	/**
	 * method to create Alert
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public AlertsEntity prepareCreateAlert(RequestModel reqModel, ClinetInfoModel info) {
		AlertsEntity alertsEntity = new AlertsEntity();
		try {
			alertsEntity.setAlertName(reqModel.getAlertName());
			alertsEntity.setAlertType(reqModel.getAlertType());
			alertsEntity.setExch(reqModel.getExch());
			alertsEntity.setExchSeg(reqModel.getExchSeg());
			alertsEntity.setExpiry(reqModel.getExpiry());
			alertsEntity.setOperator(reqModel.getOperator());
			alertsEntity.setScripName(reqModel.getScripName());
			alertsEntity.setToken(reqModel.getToken());
			alertsEntity.setTriggeredTime(reqModel.getTriggeredTime());
			alertsEntity.setTriggerStatus(reqModel.getTriggerStatus());
			alertsEntity.setUserId(info.getUserId());
			alertsEntity.setValue(reqModel.getValue());
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return alertsEntity;
	}

	/**
	 * method to validate create alert
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public boolean validateCreateAlert(RequestModel reqModel) {
		try {
			if (StringUtil.isNotNullOrEmpty(reqModel.getAlertName())
					&& StringUtil.isNotNullOrEmpty(reqModel.getAlertType())
					&& StringUtil.isNotNullOrEmpty(reqModel.getExch())
					&& StringUtil.isNotNullOrEmpty(reqModel.getOperator())
					&& StringUtil.isNotNullOrEmpty(reqModel.getScripName())
					&& StringUtil.isNotNullOrEmpty(reqModel.getToken()) && reqModel.getValue() > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * method to get alert Details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getAlertDetails(ClinetInfoModel info) {
		try {
			if (StringUtil.isNullOrEmpty(info.getUserId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			/** Get alert details **/
			List<AlertsEntity> getAlert = alertsRepository.getAlertDetails(info.getUserId());
			if (StringUtil.isListNullOrEmpty(getAlert))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_DATA);

			return prepareResponse.prepareSuccessResponseObject(getAlert);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		}
	}

	/**
	 * method to update Alert
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updateAlert(AlertsEntity alertsEntity, ClinetInfoModel info) {
		try {
			/** Method to validate update alert request **/
			if (!validateUpdateAlert(alertsEntity))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			/** To check ID */
			Optional<AlertsEntity> findById = alertsRepository.findById(alertsEntity.getId());
			if (findById == null)
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_ALERT);

			alertsEntity.setUserId(info.getUserId());
			AlertsEntity updateAlertsEntity = alertsRepository.save(alertsEntity);
			if (updateAlertsEntity == null)
				return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

			/** Method to set alert */
			alertsRestService.setAlert(alertsEntity, info.getUserId());
			return prepareResponse.prepareSuccessResponseMessage(AppConstants.SUCCESS_STATUS);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		}
	}

	/**
	 * method to validate update Alert
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public boolean validateUpdateAlert(AlertsEntity alertsEntity) {
		try {
			if (alertsEntity.getId() > 0 && StringUtil.isNotNullOrEmpty(alertsEntity.getAlertName())
					&& StringUtil.isNotNullOrEmpty(alertsEntity.getAlertType())
					&& StringUtil.isNotNullOrEmpty(alertsEntity.getExch())
					&& StringUtil.isNotNullOrEmpty(alertsEntity.getOperator())
					&& StringUtil.isNotNullOrEmpty(alertsEntity.getScripName())
					&& StringUtil.isNotNullOrEmpty(alertsEntity.getToken()) && alertsEntity.getValue() > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return false;
	}

	/**
	 * method to delete Alert
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> deleteAlert(int id, ClinetInfoModel info) {
		try {
			if (id < 1)
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			/** update active status to zero */
			int deleteAlert = alertsRepository.updateActiveStatus(id);
			if (deleteAlert < 1)
				return prepareResponse.prepareFailedResponse(AppConstants.NO_DATA);
			return prepareResponse.prepareSuccessResponseObject(AppConstants.EMPTY_ARRAY);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to update Trigger Status
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updateTriggerStatus(int id) {
		try {
			/** Method to update trigger status to data base */
			boolean isUpdated = updateToDataBase(id);
			if (isUpdated)
				return prepareResponse.prepareSuccessResponseObject(AppConstants.EMPTY_ARRAY);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to update trigger in the Database in thread
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	private boolean updateToDataBase(long triggerId) {
		try {
			/** get Alert Details */
			java.sql.Timestamp triggerTimestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
			Optional<AlertsEntity> alertDetail = alertsRepository.findById(triggerId);
			AlertsEntity alertDetails = alertDetail.get();
			if (alertDetails != null && alertDetails.getId() > 0) {
				/** update trigger status */
				alertsRepository.updateTriggerStatus(triggerId, triggerTimestamp);

				String operator = "";
				if (alertDetails.getOperator().equalsIgnoreCase("greater")) {
					operator = ">";
				} else if (alertDetails.getOperator().equalsIgnoreCase("equal")) {
					operator = ">= or <=";
				} else if (alertDetails.getOperator().equalsIgnoreCase("lesser")) {
					operator = "<";
				} else if (alertDetails.getOperator().equalsIgnoreCase("greaterequal")) {
					operator = ">=";
				} else if (alertDetails.getOperator().equalsIgnoreCase("lesserequal")) {
					operator = "<=";
				}
				/** Form the message sent to the user */
				String message = alertDetails.getScripName() + " " + operator + " " + alertDetails.getValue()
						+ " triggered at  " + triggerTimestamp;
				/** Send Alert to user via mail */
				sendEmailAlert(alertDetails.getUserId(), alertDetails.getScripName(), message);
				/** Send Alert to user via Push Notification */
				sendPushNotificationAlert(alertDetails.getUserId(), alertDetails.getScripName(), message);

				return true;
			} else {
				Log.error("No record fond to update triggered status for alert");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return false;
	}

	private void sendPushNotificationAlert(String pUserId, String scripName, String message) {
		/** Fetch the device id for the given User id */
		List<DeviceEntity> deviceIdList = deviceRepository.getDeviceIdForUser(pUserId);
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					for (int i = 0; i < deviceIdList.size(); i++) {
						/** Method to send push notification */
						alertsRestService.sendRecommendationMessage(deviceIdList.get(i).getDeviceId(), scripName,
								message);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.error(e.getMessage());
				}
			}
		});
		pool.shutdown();
	}

	/**
	 * Method to send the email alert when the price is triggered
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	private void sendEmailAlert(String pUserId, String scripName, String message) {
		/** Get User Details from the Data base */
		try {
			GetIntroSpectResponse info = new GetIntroSpectResponse();
			if (HazelcastConfig.getInstance().getKeycloakUserInfo() != null) {
				if (HazelcastConfig.getInstance().getKeycloakUserInfo().containsKey(pUserId + "_WEB")) {
					info = HazelcastConfig.getInstance().getKeycloakUserInfo().get(pUserId + "_WEB");
				} else if (HazelcastConfig.getInstance().getKeycloakUserInfo().containsKey(pUserId + "_MOB")) {
					info = HazelcastConfig.getInstance().getKeycloakUserInfo().get(pUserId + "_MOB");
				}

				if (info != null && StringUtil.isNotNullOrEmpty(info.getEmail())
						&& StringUtil.isNotNullOrEmpty(info.getName())) {
					/** Method to send email **/
					emailUtils.sendAlert(info.getName(), message, scripName, info.getEmail());
				} else {
					Log.error(AppConstants.EMAIL_EXCEPTION);
				}
			} else {
				System.out.println("Failed to send email - Keycloak User Info is null");
				Log.error("Failed to send email - Keycloak User Info is null");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
	}

}
