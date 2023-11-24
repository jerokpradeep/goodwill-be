package in.codifi.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import in.codifi.common.config.HazelcastConfig;
import in.codifi.common.entity.DashboardPreferencesEntity;
import in.codifi.common.reposirory.DashboardPreferencesRepository;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class DashboardPreferencesService {

	@Inject
	DashboardPreferencesRepository preferencesRepository;
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * 
	 * Method to load preference service into cache
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public List<DashboardPreferencesEntity> loadPrefernces() {
		List<DashboardPreferencesEntity> masterPreferencesEntities = new ArrayList<>();
		try {
			masterPreferencesEntities = preferencesRepository.findAll();
			if (StringUtil.isListNotNullOrEmpty(masterPreferencesEntities)) {
				HazelcastConfig.getInstance().getPreferences().clear();
				HazelcastConfig.getInstance().getPreferences().put(AppConstants.HAZEL_KEY_PREFERENCES,
						masterPreferencesEntities);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return masterPreferencesEntities;
	}

	/**
	 * 
	 * Method to get preference keys list which is enabled to display in front end
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public List<DashboardPreferencesEntity> getEnabledPreferences() {
		List<DashboardPreferencesEntity> masterPreferencesEntities = new ArrayList<>();
		try {
			/** Get preference from cache **/
			masterPreferencesEntities = HazelcastConfig.getInstance().getPreferences()
					.get(AppConstants.HAZEL_KEY_PREFERENCES);
			/** If data does not exist get it from DB and load it into cache **/
			if (masterPreferencesEntities == null || StringUtil.isListNullOrEmpty(masterPreferencesEntities)) {
				masterPreferencesEntities = loadPrefernces();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return masterPreferencesEntities;
	}
}
