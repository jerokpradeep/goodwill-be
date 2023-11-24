package in.codifi.sso.auth.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.sso.auth.entity.primary.VendorAppEntity;
import in.codifi.sso.auth.model.response.GenericResponse;
import in.codifi.sso.auth.model.response.VendorAppRespModel;
import in.codifi.sso.auth.repository.VendorRepository;
import in.codifi.sso.auth.service.spec.VendorServiceSpec;
import in.codifi.sso.auth.utility.AppConstants;
import in.codifi.sso.auth.utility.CommonUtils;
import in.codifi.sso.auth.utility.PrepareResponse;
import in.codifi.sso.auth.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class VendorService implements VendorServiceSpec {

	@Inject
	VendorRepository vendorRepository;

	@Inject
	PrepareResponse prepareResponse;

	/**
	 * Method to get vendor app details
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getVendorAppDetails(String userId) {

		List<VendorAppEntity> vendorAppEntities = new ArrayList<>();
		List<VendorAppRespModel> response = new ArrayList<>();
		try {
			vendorAppEntities = vendorRepository.findAllByClientIdAndActiveStatus(userId, 1);

			if (vendorAppEntities == null || vendorAppEntities.size() <= 0)
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORD_FOUND);

			response = prepareResponse(vendorAppEntities);

			if (response == null || response.size() <= 0)
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORD_FOUND);

			return prepareResponse.prepareSuccessResponseObject(response);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to prepare response for vendor app model
	 * 
	 * @author Dinesh Kumar
	 * @param vendorAppEntities
	 * @return
	 */
	private List<VendorAppRespModel> prepareResponse(List<VendorAppEntity> vendorAppEntities) {

		List<VendorAppRespModel> responseModel = new ArrayList<>();
		try {

			for (VendorAppEntity entity : vendorAppEntities) {
				VendorAppRespModel result = new VendorAppRespModel();
				result.setAppId(entity.getId());
				result.setAppName(entity.getAppName());
				result.setApiKey(entity.getApiKey());
				result.setApiSecret(entity.getApiSecret());
				result.setClientId(entity.getClientId());
				result.setRedirectUrl(entity.getRedirectUrl());
				result.setPostbackUrl(entity.getPostbackUrl());
				result.setDescription(entity.getDescription());
				result.setAuthorizationStatus(entity.getAuthorizationStatus());
				result.setContactName(entity.getContactName());
				result.setMobieNo(entity.getMobileNo());
				result.setEmail(entity.getEmail());
				result.setType(entity.getType());
				responseModel.add(result);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return responseModel;
	}

	/**
	 * Method to create new vendor
	 * 
	 * @author Dinesh Kumar
	 */
	@Override
	public RestResponse<GenericResponse> createNewVendorApp(VendorAppEntity vendorAppEntity, String userId) {

		try {

			/** Validate Request **/
			if (!validateNewVendorAppReq(vendorAppEntity))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			// Create API key and API Secrete
			String apiKey = CommonUtils.generateRandomAlpaString(15);
			vendorAppEntity.setApiKey(apiKey);
			String apiSecret = CommonUtils.generateRandomAlpaString(100);
			vendorAppEntity.setApiSecret(apiSecret);
			vendorAppEntity.setCreatedBy(userId);
			vendorAppEntity.setClientId(userId);
			vendorAppEntity.setAuthorizationStatus(1);
			VendorAppEntity vendorAppEntityNew = vendorRepository.saveAndFlush(vendorAppEntity);
			if (vendorAppEntityNew != null)
				return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);

			return prepareResponse.prepareFailedResponse(AppConstants.INTERNAL_ERROR);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to validate create vendor request
	 * 
	 * @param vendorAppReqModel
	 * @return
	 */
	private boolean validateNewVendorAppReq(VendorAppEntity vendorAppReqModel) {
		if (StringUtil.isNotNullOrEmpty(vendorAppReqModel.getAppName())
				&& StringUtil.isNotNullOrEmpty(vendorAppReqModel.getRedirectUrl())
				&& StringUtil.isNotNullOrEmpty(vendorAppReqModel.getPostbackUrl())
				&& StringUtil.isNotNullOrEmpty(vendorAppReqModel.getDescription())
				&& StringUtil.isNotNullOrEmpty(vendorAppReqModel.getContactName())
				&& StringUtil.isNotNullOrEmpty(vendorAppReqModel.getMobileNo())
				&& StringUtil.isNotNullOrEmpty(vendorAppReqModel.getEmail())
				&& StringUtil.isNotNullOrEmpty(vendorAppReqModel.getType())) {
			return true;
		}
		return false;
	}

	/**
	 * Method to update vendor details
	 * 
	 * @author Dinesh Kumar
	 * @param entity
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> updateVendorApp(VendorAppEntity entity, String userId) {
		try {

			/** Validate Request **/
			if (!validateUpdateVendorAppReq(entity))
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			Optional<VendorAppEntity> optional = vendorRepository.findById(entity.getId());

			if (optional == null || optional.isEmpty())
				return prepareResponse.prepareSuccessMessage(AppConstants.INVALID_PARAMETER);

			VendorAppEntity dbData = new VendorAppEntity();
			dbData = optional.get();

			dbData.setAppName(entity.getAppName());
			dbData.setRedirectUrl(entity.getRedirectUrl());
			dbData.setPostbackUrl(entity.getPostbackUrl());
			dbData.setDescription(entity.getDescription());
			dbData.setContactName(entity.getContactName());
			dbData.setMobileNo(entity.getMobileNo());
			dbData.setEmail(entity.getEmail());
			dbData.setType(entity.getType());
			dbData.setUpdatedBy(userId);
			VendorAppEntity vendorAppEntityNew = vendorRepository.saveAndFlush(dbData);
			if (vendorAppEntityNew != null)
				return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);

			return prepareResponse.prepareFailedResponse(AppConstants.INTERNAL_ERROR);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to validate update request
	 * 
	 * @param vendorAppReqModel
	 * @return
	 */
	private boolean validateUpdateVendorAppReq(VendorAppEntity vendorAppReqModel) {
		if (vendorAppReqModel.getId() > 0 && StringUtil.isNotNullOrEmpty(vendorAppReqModel.getAppName())
				&& StringUtil.isNotNullOrEmpty(vendorAppReqModel.getRedirectUrl())
				&& StringUtil.isNotNullOrEmpty(vendorAppReqModel.getPostbackUrl())
				&& StringUtil.isNotNullOrEmpty(vendorAppReqModel.getDescription())
				&& StringUtil.isNotNullOrEmpty(vendorAppReqModel.getContactName())
				&& StringUtil.isNotNullOrEmpty(vendorAppReqModel.getMobileNo())
				&& StringUtil.isNotNullOrEmpty(vendorAppReqModel.getType())
				&& StringUtil.isNotNullOrEmpty(vendorAppReqModel.getEmail())) {
			return true;
		}
		return false;
	}

	/**
	 * Method to reset secret key
	 * 
	 * @author Dinesh Kumar
	 */
	@Override
	public RestResponse<GenericResponse> restAPISecret(long appId, String userId) {
		try {
			/** Validate Request **/
			if (appId <= 0)
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			VendorAppEntity dbData = vendorRepository.findByIdAndClientId(appId, userId);

			if (dbData != null) {
				String apiSecret = CommonUtils.generateRandomAlpaString(100);
				dbData.setApiSecret(apiSecret);
				dbData.setUpdatedBy(userId);
				VendorAppEntity vendorAppEntityNew = vendorRepository.saveAndFlush(dbData);
				if (vendorAppEntityNew != null)
					return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);

				return prepareResponse.prepareFailedResponse(AppConstants.INTERNAL_ERROR);
			} else {
				return prepareResponse.prepareSuccessMessage(AppConstants.INVALID_PARAMETER);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to delete vendor app details
	 * 
	 * @author Dinesh Kumar
	 */
	@Override
	public RestResponse<GenericResponse> deleteVendor(long appId, String userId) {
		try {
			/** Validate Request **/
			if (appId <= 0)
				return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETER);

			VendorAppEntity dbData = vendorRepository.findByIdAndClientId(appId, userId);

			if (dbData != null) {
				dbData.setUpdatedBy(userId);
				dbData.setActiveStatus(0);
				VendorAppEntity vendorAppEntityNew = vendorRepository.saveAndFlush(dbData);
				if (vendorAppEntityNew != null)
					return prepareResponse.prepareSuccessMessage(AppConstants.SUCCESS_STATUS);

				return prepareResponse.prepareFailedResponse(AppConstants.INTERNAL_ERROR);
			} else {
				return prepareResponse.prepareSuccessMessage(AppConstants.INVALID_PARAMETER);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}
}
