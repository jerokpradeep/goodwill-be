package in.codifi.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.entity.DashboardPreferencesEntity;
import in.codifi.common.entity.FiiIndexEntity;
import in.codifi.common.model.request.PreferenceModel;
import in.codifi.common.model.response.DashBoardPreferenceModel;
import in.codifi.common.model.response.SectorHeatMapModel;
import in.codifi.common.model.response.TradingDashBoardRespModel;
import in.codifi.common.repo.entitymanager.ContractEntityManger;
import in.codifi.common.service.spec.TradingServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class TradingService implements TradingServiceSpec {

	@Inject
	DashboardPreferencesService masterPreferencesService;
	@Inject
	SectorHeatMapService sectorService;
	@Inject
	ContractEntityManger contractEntityManger;
	@Inject
	FiiService fiiService;

	/**
	 * 
	 * Method to get trading dash board info for mobile
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Override
	public RestResponse<TradingDashBoardRespModel> getTradingInfo(String userId) {
		TradingDashBoardRespModel reponse = new TradingDashBoardRespModel();

		try {
			List<DashboardPreferencesEntity> masterPreferencesEntities = masterPreferencesService
					.getEnabledPreferences();
			List<PreferenceModel> preferenceModels = contractEntityManger.getPreference(userId);
			List<String> preferenceKeys = new ArrayList<>();
			List<SectorHeatMapModel> sectorHeatMap = new ArrayList<SectorHeatMapModel>();
			List<FiiIndexEntity> fiiIndex = new ArrayList<>();
			List<DashBoardPreferenceModel> prefernces = new ArrayList<>();

			/** Get key list which is enabled to display in front end **/
			for (DashboardPreferencesEntity entity : masterPreferencesEntities) {
				DashBoardPreferenceModel model = new DashBoardPreferenceModel();
				if (entity.getTag().equalsIgnoreCase(AppConstants.TRADING)) {
					if (!entity.getValue().equalsIgnoreCase(AppConstants.F)) {
						preferenceKeys.add(entity.getKeyVariable());
					}
					model.setKey(entity.getKeyVariable());
					model.setShow(entity.getValue());
					model.setSortOrder(entity.getSortOrder());

					for (PreferenceModel preference : preferenceModels) {
						if (entity.getKeyVariable().equalsIgnoreCase(preference.getTag())) {
							model.setIsEnabled(preference.getValue());
						}
					}
					if (StringUtil.isNullOrEmpty(model.getIsEnabled())) {
						model.setIsEnabled("1");
					}
					prefernces.add(model);
				}
			}

			for (String key : preferenceKeys) {
				switch (key) {
				case AppConstants.TRADING_HEATMAP:
					sectorHeatMap = sectorService.getHeatMapSectors();
					break;
				case AppConstants.TRADING_FII:
					fiiIndex = fiiService.getFiiData();
					break;

				default:
					break;
				}
			}
			reponse.setPreference(prefernces);
			reponse.setStatus(AppConstants.STATUS_OK);
			reponse.setMessage(AppConstants.SUCCESS_STATUS);
			reponse.setHeatMapData(sectorHeatMap);
			reponse.setFiiIndexData(fiiIndex);

		} catch (Exception e) {
			reponse.setStatus(AppConstants.STATUS_NOT_OK);
			reponse.setMessage(AppConstants.FAILED_STATUS);
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return RestResponse.ResponseBuilder.create(Status.OK, reponse).build();
	}

}
