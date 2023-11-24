package in.codifi.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import com.esotericsoftware.minlog.Log;

import in.codifi.common.config.HazelcastConfig;
import in.codifi.common.entity.VersionEntity;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.model.response.VersionModel;
import in.codifi.common.reposirory.VersionRepository;
import in.codifi.common.service.spec.VersionServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;

@ApplicationScoped
public class VersionService implements VersionServiceSpec {

	@Inject
	VersionRepository versionRepository;
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * method to verify version
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> findVersion(VersionEntity versionEntity) {
		if (StringUtil.isNotNullOrEmpty(versionEntity.getVersion())
				&& StringUtil.isNotNullOrEmpty(versionEntity.getOs())) {
			try {
				List<VersionEntity> version = new ArrayList<>();
				VersionModel versionModel = new VersionModel();
				if (HazelcastConfig.getInstance().getVersion().containsKey(AppConstants.HAZEL_KEY_VERSION)) {
					version = HazelcastConfig.getInstance().getVersion().get(AppConstants.HAZEL_KEY_VERSION);
				} else {
					version = loadVersionData();
				}
				// updateRequired = 2 -> Mandatory update required
				int updateRequired = 2;
				if (StringUtil.isListNotNullOrEmpty(version)) {
					for (VersionEntity iterEntity : version) {
						if (iterEntity.getVersion().equalsIgnoreCase(versionEntity.getVersion())
								&& iterEntity.getOs().equalsIgnoreCase(versionEntity.getOs())) {
							versionModel.setIsUpdateAvailable(iterEntity.getIsUpdateAvailable());
							return prepareResponse.prepareSuccessResponseObject(versionModel);
						}
					}
					versionModel.setIsUpdateAvailable(updateRequired);
					return prepareResponse.prepareSuccessResponseObject(versionModel);
				} else {
					versionModel.setIsUpdateAvailable(updateRequired);
					return prepareResponse.prepareSuccessResponseObject(versionModel);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.error(e.getMessage());

			}
		} else {
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to load version data
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public List<VersionEntity> loadVersionData() {
		List<VersionEntity> version = new ArrayList<>();
		try {
			version = versionRepository.findAll();
			if (StringUtil.isListNotNullOrEmpty(version)) {
				HazelcastConfig.getInstance().getVersion().clear();
				HazelcastConfig.getInstance().getVersion().put(AppConstants.HAZEL_KEY_VERSION, version);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return version;
	}

}
