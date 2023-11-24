package in.codifi.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.config.HazelcastConfig;
import in.codifi.common.entity.FiiIndexEntity;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.reposirory.FiiIndexRepository;
import in.codifi.common.service.spec.FiiServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class FiiService implements FiiServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	FiiIndexRepository fiiIndexRepository;

	/**
	 * Method to get Fii index details
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getFiiIndexFutureData() {
		List<FiiIndexEntity> fiiIndexEntities = new ArrayList<>();
		try {
			if (HazelcastConfig.getInstance().getFutureDetails().containsKey(AppConstants.HAZEL_KEY_FII_INDEX)) {
				fiiIndexEntities = HazelcastConfig.getInstance().getFiiIndexEntity()
						.get(AppConstants.HAZEL_KEY_FII_INDEX);
			} else {
				fiiIndexEntities = loadFiiIndexDetails();
			}
			if (StringUtil.isListNotNullOrEmpty(fiiIndexEntities)) {
				return prepareResponse.prepareSuccessResponseObject(fiiIndexEntities);
			} else {
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
		}
	}

	/**
	 * Method to load fii index details
	 * 
	 * @author Dinesh
	 * 
	 * @return
	 */
	public List<FiiIndexEntity> loadFiiIndexDetails() {
		List<FiiIndexEntity> fiiIndexEntities = new ArrayList<>();
		try {
			fiiIndexEntities = fiiIndexRepository.findAll();
			if (StringUtil.isListNotNullOrEmpty(fiiIndexEntities)) {
				HazelcastConfig.getInstance().getFutureDetails().clear();
				HazelcastConfig.getInstance().getFiiIndexEntity().put(AppConstants.HAZEL_KEY_FII_INDEX,
						fiiIndexEntities);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return fiiIndexEntities;
	}

	/**
	 * Method to get fii index data for trading dashboard
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	public List<FiiIndexEntity> getFiiData() {
		List<FiiIndexEntity> fiiIndexEntities = new ArrayList<>();
		try {
			if (HazelcastConfig.getInstance().getFutureDetails().containsKey(AppConstants.HAZEL_KEY_FII_INDEX)) {
				fiiIndexEntities = HazelcastConfig.getInstance().getFiiIndexEntity()
						.get(AppConstants.HAZEL_KEY_FII_INDEX);
			} else {
				fiiIndexEntities = loadFiiIndexDetails();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return fiiIndexEntities;
	}
}
