package in.codifi.admin.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.config.KeyCloakConfig;
import in.codifi.admin.entity.DeviceMappingEntity;
import in.codifi.admin.entity.UserNotification;
import in.codifi.admin.entity.VendorAppEntity;
import in.codifi.admin.model.request.SendNoficationReqModel;
import in.codifi.admin.model.request.UserReqModel;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.model.response.KcUserDetailsRequest;
import in.codifi.admin.repository.CommonEntityManager;
import in.codifi.admin.repository.DeviceMappingRepository;
import in.codifi.admin.repository.VendorRepository;
import in.codifi.admin.req.model.MobVersionReqModel;
import in.codifi.admin.req.model.UserRoleMapReqModel;
import in.codifi.admin.req.model.VendorAppReqModel;
import in.codifi.admin.restservice.KcAdminRest;
import in.codifi.admin.service.spec.CommonServiceSpec;
import in.codifi.admin.service.spec.UserNotificationSpec;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.AppUtils;
import in.codifi.admin.utility.PrepareResponse;
import in.codifi.admin.utility.PushNoficationUtils;
import in.codifi.admin.utility.StringUtil;
import in.codifi.admin.ws.model.kc.CreateUserCredentialsModel;
import in.codifi.admin.ws.model.kc.CreateUserRequestModel;
import in.codifi.admin.ws.model.kc.GetUserInfoResp;
import in.codifi.admin.ws.model.kc.UserAttribute;
import io.quarkus.logging.Log;

@ApplicationScoped
public class CommonService implements CommonServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	DeviceMappingRepository deviceMappingRepository;
	@Inject
	PushNoficationUtils pushNoficationUtils;
	@Inject
	CommonEntityManager mobileversion;
	@Inject
	VendorRepository vendorRepository;
	@Inject
	AppUtils appUtils;
	@Inject
	KcAdminRest kcAdminRest;
	@Inject
	KeyCloakConfig keycloakConfig;

	@Inject
	UserNotificationSpec userNotificationSpec;

	/**
	 * Method to send recommendation message
	 * 
	 * @author Gowthaman
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> sendRecommendationMessge(SendNoficationReqModel reqModel) {
		try {
			if (!validateMsgParams(reqModel))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);

			if (reqModel.getUserType().equalsIgnoreCase(AppConstants.ALL)) {

				List<DeviceMappingEntity> deviceMapping = deviceMappingRepository.findAll();
				if (!deviceMapping.isEmpty()) {
					userNotificationSpec.saveNotification(reqModel);
					sendRecommendationMsg(deviceMapping, reqModel);
					return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);
				} else {
					return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
				}
			} else if (reqModel.getUserType().equalsIgnoreCase(AppConstants.INDIVIDUAL)) {
				if (reqModel.getUserId() != null && reqModel.getUserId().length > 0) {

					List<String> userIdList = Arrays.stream(reqModel.getUserId()).collect(Collectors.toList());

					List<DeviceMappingEntity> deviceDetails = deviceMappingRepository.getByMultipleUserId(userIdList);
					if (StringUtil.isListNotNullOrEmpty(deviceDetails)) {
						userNotificationSpec.saveNotification(reqModel);
						sendRecommendationMsg(deviceDetails, reqModel);
					} else {
						return prepareResponse.prepareFailedResponse(AppConstants.NO_DEVICE_IS_FOR_USER);
					}
					return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);

				} else {
					List<DeviceMappingEntity> deviceDetails = deviceMappingRepository
							.findByUserId(reqModel.getUserId());
					if (!deviceDetails.isEmpty()) {
						userNotificationSpec.saveNotification(reqModel);
						sendRecommendationMsg(deviceDetails, reqModel);
					} else {
						return prepareResponse.prepareFailedResponse(AppConstants.NO_DEVICE_IS_FOR_USER);
					}
					return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);
				}
			}

		} catch (Exception e) {
			Log.error(e);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to validate message parameters
	 * 
	 * @author Gowthaman
	 * @param reqModel
	 * @return
	 */
	private boolean validateMsgParams(SendNoficationReqModel reqModel) {
		if (StringUtil.isNotNullOrEmpty(reqModel.getMessage()) && StringUtil.isNotNullOrEmpty(reqModel.getMessageType())
				&& StringUtil.isNotNullOrEmpty(reqModel.getTitle())
				&& StringUtil.isNotNullOrEmpty(reqModel.getUserType())) {
			if (reqModel.getUserType().equalsIgnoreCase(AppConstants.INDIVIDUAL)) {
				if (reqModel.getUserId() != null && reqModel.getUserId().length > 0) {
					return true;
				}
			} else if (reqModel.getUserType().equalsIgnoreCase(AppConstants.ALL)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Method to send recommendation message
	 * 
	 * @author Gowthaman
	 * @param deviceMapping
	 * @param reqModel
	 */
	public void sendRecommendationMsg(List<DeviceMappingEntity> deviceMapping, SendNoficationReqModel reqModel) {
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					int count = 0;
					List<String> deviceIds = new ArrayList<>();
					for (DeviceMappingEntity entity : deviceMapping) {
						String mobileDeviceId = entity.getDeviceId();
						if (StringUtil.isNotNullOrEmpty(mobileDeviceId)) {
							deviceIds.add(mobileDeviceId.trim());
						}
						if (count == 500) {
							pushNoficationUtils.sendNofification(deviceIds, reqModel);
							deviceIds = new ArrayList<String>();
							count = 0;
						}

						count++;
					}
					if (count > 0) {
						pushNoficationUtils.sendNofification(deviceIds, reqModel);
						count = 0;
					}

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					pool.shutdown();
				}
			}
		});
	}

	/**
	 * method to get mobile version
	 * 
	 * @author LOKESH
	 */
	@Override
	public RestResponse<GenericResponse> getMobileVersion() {
		try {
			List<MobVersionReqModel> getMobileVersion = mobileversion.getMobVersion();
			if (getMobileVersion.size() > 0)
				return prepareResponse.prepareSuccessResponseObject(getMobileVersion);
			return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to add version
	 * 
	 * @author LOKESH
	 */
	@Override
	public RestResponse<GenericResponse> addVersion(MobVersionReqModel model) {
		try {
			if (!validateVersionParam(model))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			boolean inserted = mobileversion.addMobVersion(model);
			if (!inserted) {
				return prepareResponse.prepareSuccessResponseObject(AppConstants.INSERTED);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INSERTED_FAILED);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to validate version parameters
	 * 
	 * @author LOKESH
	 * @param entity
	 * @return
	 */
	private boolean validateVersionParam(MobVersionReqModel model) {
		if (StringUtil.isNotNullOrEmpty(model.getVersion()) && StringUtil.isNotNullOrEmpty(model.getType())) {
			return true;
		}
		return false;
	}

	/**
	 * method to update version
	 * 
	 * @author LOKESH
	 */
	@Override
	public RestResponse<GenericResponse> updateVersion(MobVersionReqModel model) {
		try {
			if (!validateUpdateVersionParam(model))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			int updated = mobileversion.updateMobVersion(model.getUpdateAvailable(), model.getVersion());
			if (updated > 0)
				return prepareResponse.prepareSuccessResponseObject(AppConstants.UPDATED);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to validate update version params
	 * 
	 * @author LOKESH
	 * @param entity
	 * @return
	 */
	private boolean validateUpdateVersionParam(MobVersionReqModel model) {
		if (StringUtil.isNotNullOrEmpty(model.getVersion()) && model.getUpdateAvailable() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * method to delete version params
	 * 
	 * @author LOKESH
	 * @param entity
	 * @return
	 */
	public RestResponse<GenericResponse> deleteVersion(MobVersionReqModel model) {
		try {
			if (!validateDeleteParams(model))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
			long deleted = mobileversion.deleteMobVersion(model.getType(), model.getVersion());
			if (deleted > 0)
				return prepareResponse.prepareSuccessResponseObject(AppConstants.SUCCESS_STATUS);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to validate delete params
	 * 
	 * @author LOKESH
	 * @param entity
	 * @return
	 */
	private boolean validateDeleteParams(MobVersionReqModel model) {
		if (StringUtil.isNotNullOrEmpty(model.getVersion()) && StringUtil.isNotNullOrEmpty(model.getType())) {
			return true;
		}
		return false;
	}

	/**
	 * Method to get api from Vendor
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getVendor(VendorAppReqModel VendorAppModel) {
		try {
			List<VendorAppEntity> getVendorData = vendorRepository.getByClientId(VendorAppModel.getClient_id());
			if (getVendorData != null)
				return prepareResponse.prepareSuccessResponseObject(getVendorData);
			return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to Update authorization_status
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updateVendor(VendorAppReqModel VendorAppModel) {
		try {
			int getVendorData1 = vendorRepository.updateAuthorization(VendorAppModel.getAuthorization_status(),
					VendorAppModel.getApi_key());
			if (getVendorData1 > 0)
				return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);
			return prepareResponse.prepareFailedResponse(AppConstants.INTERNAL_ERROR);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get kc user details
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getKcUserDetails(KcUserDetailsRequest req) {
		try {
			if (StringUtil.isNullOrEmpty(req.getUserId()))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			if (appUtils.isMobileNumber(req.getUserId())) {
				return verifyClientByAttribute(AppConstants.ATTRIBUTE_MOBILE, req.getUserId());
			} else if (appUtils.isEmail(req.getUserId())) {
				return verifyClientByAttribute(AppConstants.ATTRIBUTE_MAIL, req.getUserId());
			} else if (appUtils.isPan(req.getUserId())) {
				return verifyClientByAttribute(AppConstants.ATTRIBUTE_PAN, req.getUserId());
			}
			List<GetUserInfoResp> userInfo = kcAdminRest.getUserInfo(req.getUserId());
			if (StringUtil.isListNotNullOrEmpty(userInfo)) {
				return prepareResponse.prepareSuccessResponseObject(userInfo);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to verify Client By Attribute
	 * 
	 * @author Gowthaman
	 * @return
	 */
	private RestResponse<GenericResponse> verifyClientByAttribute(String key, String value) {
		try {
			List<GetUserInfoResp> userInfo = kcAdminRest.getUserInfoByAttribute(key, value);
			if (StringUtil.isListNotNullOrEmpty(userInfo)) {
				/** If attribute linked with multiple userId return message **/
				if (userInfo.size() > 1)
					return prepareResponse.prepareFailedResponse("Given " + key + AppConstants.MULTIPLE_USER_LINKED);

				return prepareResponse.prepareSuccessResponseObject(userInfo);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get kc All user details
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getKcAllUserDetails(KcUserDetailsRequest req) {
		try {

			List<GetUserInfoResp> userInfo = kcAdminRest.getKcAllUserDetails(req.getMin(), req.getMax());
			if (StringUtil.isListNotNullOrEmpty(userInfo)) {
				return prepareResponse.prepareSuccessResponseObject(userInfo);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to get kc User details in csv
	 * 
	 * @author Gowthaman
	 * @return
	 */
	@SuppressWarnings("unused")
	@Override
	public RestResponse<GenericResponse> getUserDetailsCsv() {
		/** Method to get user count in keyclock */
		int count = kcAdminRest.getCount();

		String min = "0";
		String max = "10000";
		if (count >= 1000) {
			int counts = count / 10000;
			List<GetUserInfoResp> userInfo = kcAdminRest.getKcAllUserDetails(min, Integer.toString(count));
		} else {

		}
		return null;
	}

	/**
	 * Method to update Kc User Details
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updateKcUserDetails(CreateUserRequestModel user) {
		try {
			if (StringUtil.isNotNullOrEmpty(user.getUsername()) && StringUtil.isNotNullOrEmpty(user.getFirstName())) {
				String userInfo = kcAdminRest.updateKcUserDetails(user);
				return prepareResponse.prepareSuccessResponseObject(userInfo);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to add New User in keyclock
	 * 
	 * @author Gowthaman
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unused")
	@Override
	public RestResponse<GenericResponse> addNewUser(CreateUserRequestModel user) {
		try {
			if (StringUtil.isNotNullOrEmpty(user.getUsername())
					&& StringUtil.isNotNullOrEmpty(user.getAttributes().getUcc())
					&& StringUtil.isNotNullOrEmpty(user.getAttributes().getMaritalStatus())
					&& StringUtil.isNotNullOrEmpty(user.getAttributes().getGender())) {
				CreateUserRequestModel requestModel = new CreateUserRequestModel();
				UserAttribute attribute = new UserAttribute();
				List<CreateUserCredentialsModel> userCredentilList = new ArrayList<>();
				CreateUserCredentialsModel credentialsModel = new CreateUserCredentialsModel();
				List<CreateUserRequestModel> activeList = new ArrayList<>();
//				List<CreateUserRequestModel> dormatList = new ArrayList<>();

				requestModel.setUsername(user.getUsername());
				String firstName = "";
				if (StringUtil.isNullOrEmpty(user.getFirstName())) {
					firstName = user.getUsername();
				} else {
					firstName = user.getFirstName();
				}

				String fName = "";
				if (user.getAttributes().getMaritalStatus().contains("MARRIED")
						&& user.getAttributes().getGender().contains("F")) {
					fName = "MRS" + " " + firstName;
					requestModel.setFirstName(fName);
				} else if (user.getAttributes().getMaritalStatus().contains("SINGLE")
						&& user.getAttributes().getGender().contains("F")) {
					fName = "MS" + " " + firstName;
					requestModel.setFirstName(fName);
				} else if (user.getAttributes().getGender().contains("M")) {
					fName = "MR" + " " + firstName;
					requestModel.setFirstName(fName);
				}

				requestModel.setFirstName(firstName);
				requestModel.setLastName(user.getLastName());
				requestModel.setEnabled(user.getEnabled());
				requestModel.setEmail(user.getEmail());
				requestModel.setEmailVerified(user.getEmailVerified());

				attribute.setGender(user.getAttributes().getGender());
				attribute.setMaritalStatus(user.getAttributes().getMaritalStatus());
				attribute.setMobile(user.getAttributes().getMobile());
				attribute.setPan(user.getAttributes().getPan());
				attribute.setUcc(user.getAttributes().getUcc());
				requestModel.setAttributes(attribute);

				credentialsModel.setType("password");
				credentialsModel.setValue("Abc@1234");
				userCredentilList.add(credentialsModel);
				requestModel.setCredentials(userCredentilList);

				activeList.add(requestModel);

				String activeUserFileName = "ActiveUser";
				List<String> createActiveUserIntoKeycoack = insertUser(activeList, "Active");
				return prepareResponse.prepareSuccessResponseObject(createActiveUserIntoKeycoack.get(0));
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to insert new user
	 * 
	 * @author Gowthaman
	 * @param requestModel
	 * @param role
	 * @return
	 */
	public List<String> insertUser(List<CreateUserRequestModel> requestModel, String role) {
		List<Callable<String>> tasks = new ArrayList<>();
		for (CreateUserRequestModel model : requestModel) {
			Callable<String> task = () -> {
				String message = kcAdminRest.addNewUser(model);

				if (message.equals("User Created")) {
					if (role.equalsIgnoreCase("Active")) {
						activeUserRoleMapping(model.getUsername());
					}
//					else {
//						dormatUserRoleMapping(model.getUsername());
//					}
					return "User Created - " + model.getUsername();
				} else if (message.equals("User already exists")) {
					return "User already exists - " + model.getUsername();
				}
				return "Failed to Created - " + model.getUsername();
			};
			tasks.add(task);
		}
		return tasks.stream().map(callable -> {
			try {
				return callable.call();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}).collect(Collectors.toList());
	}

	/**
	 * Method to active User Role Mapping
	 * 
	 * @param userId
	 */
	public void activeUserRoleMapping(String userId) {
		try {
			List<UserRoleMapReqModel> mapReqModels = new ArrayList<>();
			UserRoleMapReqModel model = new UserRoleMapReqModel();
			model.setId(keycloakConfig.getActiveRoleId());
			model.setName(keycloakConfig.getActiveRoleName());
			mapReqModels.add(model);
			String message = kcAdminRest.userRoleMapping(mapReqModels, userId, keycloakConfig.getClientCholaId());
			if (StringUtil.isNotNullOrEmpty(message)) {
				Log.info("Role Mapping - " + message);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
	}

	@Override
	public RestResponse<GenericResponse> getUserNotificationList(UserReqModel reqModel) {
		try {

			if (StringUtils.isEmpty(reqModel.getUserId())) {
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);
			}
			List<UserNotification> notificationList = userNotificationSpec.getNotificationList(reqModel.getUserId());
			if (notificationList.isEmpty()) {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
			return prepareResponse.prepareSuccessResponseWithMessage(notificationList, AppConstants.SUCCESS_STATUS);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
