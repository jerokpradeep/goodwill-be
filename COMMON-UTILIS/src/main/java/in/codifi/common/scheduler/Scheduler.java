package in.codifi.common.scheduler;

import javax.inject.Inject;
import javax.servlet.ServletException;

import in.codifi.common.controller.AnalysisController;
import in.codifi.common.service.AnalysisService;
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
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;

public class Scheduler {

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
	AnalysisService analysisService;
	@Inject
	AnalysisController analysisController;

	@Scheduled(cron = "0 30 0 * * ?")
	public void removeContract(ScheduledExecution execution) throws ServletException {
		Log.info("Scheduler started to Load Cache data");
		productMasterService.loadProductMaster();
		etfService.insertEtfData();
		eqSectorService.insertEQSectorData();
		futureService.insertFutureData();
		futureMonthService.insertFutureMonthData();
		indicesService.insertIndicesData();
		marketingService.loadMarketingData();
		heatMapService.insertSectorHeatMapData();
		versionService.loadVersionData();
		Log.info("Predefined Market watch data pre-Lodings are ended");
	}

	@Scheduled(every = "3m")
	public void screeners(ScheduledExecution execution) throws ServletException {
		analysisService.getTopGainers();
		analysisService.getTopLosers();
		analysisController.get52WeekHigh();
		analysisController.get52WeekLow();
	}
}
