package in.codifi.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response.Status;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.entity.DashboardPreferencesEntity;
import in.codifi.common.entity.EQSectorEntity;
import in.codifi.common.entity.IndicesEntity;
import in.codifi.common.entity.MarketingEntity;
import in.codifi.common.model.request.PreferenceModel;
import in.codifi.common.model.request.ScanMasterModel;
import in.codifi.common.model.response.DashBoardPreferenceModel;
import in.codifi.common.model.response.InvestmentDashBoardRespModel;
import in.codifi.common.repo.entitymanager.ContractEntityManger;
import in.codifi.common.service.spec.InvestmentServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class InvestmentService implements InvestmentServiceSpec {

	@Inject
	DashboardPreferencesService masterPreferencesService;
	@Inject
	MarketingService marketingService;
	@Inject
	IndicesService indicesService;
	@Inject
	EQSectorService eqSectorService;
	@Inject
	ScanService scanService;
	@Inject
	ContractEntityManger contractEntityManger;

	/**
	 * 
	 * Method to get investment dashboard data
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Override
	public RestResponse<InvestmentDashBoardRespModel> getInvestmentInfo(String userId) {
		InvestmentDashBoardRespModel reponse = new InvestmentDashBoardRespModel();
		try {
			List<DashboardPreferencesEntity> masterPreferencesEntities = masterPreferencesService
					.getEnabledPreferences();
			List<PreferenceModel> preferenceModels = contractEntityManger.getPreference(userId);
			List<String> preferenceKeys = new ArrayList<>();
			List<MarketingEntity> marketingEntity = new ArrayList<>();
			List<IndicesEntity> indicesDetailsEntities = new ArrayList<>();
			List<EQSectorEntity> eqSectorListModels = new ArrayList<>();
			List<ScanMasterModel> scanMasterList = new ArrayList<ScanMasterModel>();
			List<DashBoardPreferenceModel> prefernces = new ArrayList<>();

			/** Get key list which is enabled to display in front end **/
			for (DashboardPreferencesEntity entity : masterPreferencesEntities) {
				DashBoardPreferenceModel model = new DashBoardPreferenceModel();
				if (entity.getTag().equalsIgnoreCase(AppConstants.INVESTING)) {
					if (!entity.getValue().equalsIgnoreCase(AppConstants.F)) {
						preferenceKeys.add(entity.getKeyVariable());
					}
					model.setKey(entity.getKeyVariable());
					model.setShow(entity.getValue());
					for (PreferenceModel preference : preferenceModels) {
						if (entity.getKeyVariable().equalsIgnoreCase(preference.getTag())) {
							model.setIsEnabled(preference.getValue());
						}
					}
					if (StringUtil.isNullOrEmpty(model.getIsEnabled())) {
						model.setIsEnabled("1");
					}
					model.setSortOrder(entity.getSortOrder());
					prefernces.add(model);
				}
			}

			for (String key : preferenceKeys) {
				switch (key) {
				case AppConstants.INVESTING_MCARD:
					marketingEntity = marketingService.getMarketingCards();
					break;

				case AppConstants.INVESTING_INDICES:
					indicesDetailsEntities = indicesService.getIndicesDetails();
					break;

				case AppConstants.INVESTING_TOP_SECTORS:
					eqSectorListModels = eqSectorService.getEqSector();
					break;

				case AppConstants.INVESTING_SCANNERS:
					scanMasterList = scanService.getEqScanner();
					break;

				default:
					break;
				}
			}
			reponse.setPreference(prefernces);
			reponse.setStatus(AppConstants.STATUS_OK);
			reponse.setMessage(AppConstants.SUCCESS_STATUS);
			reponse.setTopSectorData(eqSectorListModels);
			reponse.setIndicesData(indicesDetailsEntities);
			reponse.setMCardData(marketingEntity);
			reponse.setScannersData(scanMasterList);
		} catch (Exception e) {
			reponse.setStatus(AppConstants.STATUS_NOT_OK);
			reponse.setMessage(AppConstants.FAILED_STATUS);
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return RestResponse.ResponseBuilder.create(Status.OK, reponse).build();
	}

}
