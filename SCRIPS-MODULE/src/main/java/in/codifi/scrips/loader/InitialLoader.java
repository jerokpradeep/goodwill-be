package in.codifi.scrips.loader;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import in.codifi.scrips.config.HazelcastConfig;
import in.codifi.scrips.repository.ContractEntityManager;
import in.codifi.scrips.repository.ScripSearchEntityManager;
import in.codifi.scrips.service.ContractService;
import in.codifi.scrips.service.ScripsService;
import in.codifi.scrips.service.StockReturnService;
import in.codifi.scrips.utility.AppConstants;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;

@SuppressWarnings("serial")
@ApplicationScoped
public class InitialLoader extends HttpServlet {

	@Inject
	ScripSearchEntityManager scripSearchRepo;
	@Inject
	ContractService contractService;
	@Inject
	StockReturnService stockService;
	@Inject
	ScripsService scripsService;
	@Inject
	ContractEntityManager contractEntityManager;

	public void init(@Observes StartupEvent ev) throws ServletException {
		Log.info("Started to Index contract scrips");
		HazelcastConfig.getInstance().getFetchDataFromCache().put(AppConstants.FETCH_DATA_FROM_CACHE, true);
//		scripSearchRepo.loadDistintValue(2);
//		scripSearchRepo.loadIndexValue();
//		contractService.loadMTFData();
		Log.info("Started to loading contract master");
//		contractService.reloadContractMasterFile();
		contractService.loadContractMaster();
		contractService.loadPnlLotSize();
		Log.info("PnlLotSize loaded successfully");
//		stockService.loadStockReturn();
		contractService.loadPromptData();
		contractEntityManager.loadIsinByToken();
		Log.info("Prompt loaded successfully");
		Log.info("All the pre-Lodings are ended");
	}

}
