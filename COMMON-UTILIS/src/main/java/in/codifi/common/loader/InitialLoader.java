package in.codifi.common.loader;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import in.codifi.common.service.DashboardPreferencesService;
import in.codifi.common.service.EQSectorService;
import in.codifi.common.service.EtfService;
import in.codifi.common.service.FutureMonthService;
import in.codifi.common.service.FutureService;
import in.codifi.common.service.IndicesService;
import in.codifi.common.service.MarketingService;
import in.codifi.common.service.ProductMasterService;
import in.codifi.common.service.SectorHeatMapService;
import in.codifi.common.service.StockReturnService;
import in.codifi.common.service.VersionService;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;

@SuppressWarnings("serial")
@ApplicationScoped
public class InitialLoader extends HttpServlet {

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
	@Inject
	StockReturnService stockReturnService;
	@Inject
	DashboardPreferencesService dashboardPreferencesService;

	public void init(@Observes StartupEvent ev) throws ServletException {
		Log.info("Started to load all pre-Lodings");
		dashboardPreferencesService.loadPrefernces();
		productMasterService.loadProductMaster();
		etfService.loadEtfData();
		eqSectorService.loadEQSectorData();
		futureService.loadFutureDetails();
		futureMonthService.loadFutureMonth();
		indicesService.loadIndicesDetailsData();
		marketingService.loadMarketingData();
		heatMapService.loadHeatMapSectorMasterData();
		versionService.loadVersionData();

		Log.info("All the pre-Lodings are ended");
	}

}
