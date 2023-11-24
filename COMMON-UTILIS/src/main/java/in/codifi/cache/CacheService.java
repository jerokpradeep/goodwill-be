package in.codifi.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.service.EQSectorService;
import in.codifi.common.service.EtfService;
import in.codifi.common.service.FutureMonthService;
import in.codifi.common.service.FutureService;
import in.codifi.common.service.IndicesService;
import in.codifi.common.service.MarketingService;
import in.codifi.common.service.ProductMasterService;
import in.codifi.common.service.SectorHeatMapService;
import in.codifi.common.service.VersionService;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import io.quarkus.logging.Log;

@ApplicationScoped
public class CacheService {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	ProductMasterService productMasterService;
	@Inject
	EtfService etfService;
	@Inject
	EQSectorService eqSectorService;
	@Inject
	FutureService futureService;
	@Inject
	FutureMonthService futureMonthService;
	@Inject
	IndicesService indicesService;
	@Inject
	MarketingService marketingService;
	@Inject
	SectorHeatMapService heatMapService;
	@Inject
	VersionService versionService;

	/**
	 * Method to load cache data
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	public RestResponse<GenericResponse> loadCacheData() {

		try {
			productMasterService.loadProductMaster();
			etfService.loadEtfData();
			eqSectorService.loadEQSectorData();
			futureService.loadFutureDetails();
			futureMonthService.loadFutureMonth();
			indicesService.loadIndicesDetailsData();
			marketingService.loadMarketingData();
			heatMapService.loadHeatMapSectorMasterData();
			versionService.loadVersionData();
			return prepareResponse.prepareSuccessMessage(AppConstants.LOADED);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * Method to update latest data into cache by fetching data from mapping table
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	public RestResponse<GenericResponse> updateCacheData() {

		try {
			productMasterService.loadProductMaster();
			etfService.insertEtfData();
			eqSectorService.insertEQSectorData();
			futureService.insertFutureData();
			futureMonthService.insertFutureMonthData();
			indicesService.insertIndicesData();
			marketingService.loadMarketingData();
			heatMapService.insertSectorHeatMapData();
			versionService.loadVersionData();
			return prepareResponse.prepareSuccessMessage(AppConstants.LOADED);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}
}
