package in.codifi.scrips.scheduler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;

import in.codifi.scrips.service.ContractService;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;

@ApplicationScoped
public class Scheduler {

//	@Inject
//	ScripSearchEntityManager entityManager;
	@Inject
	ContractService contractService;
//	@Inject
//	StockReturnService stockReturnService;

	/**
	 * 
	 * Scheduler to load latest data into cache at morning 6:30 AM (1:00 AM UTC)
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param execution
	 * @throws ServletException
	 */
//	@Scheduled(cron = "0 0 1 * * ?")
//	public void run(ScheduledExecution execution) throws ServletException {
//		Log.info("Scheduler started to clear cache and reload ");
//		HazelcastConfig.getInstance().getFetchDataFromCache().clear();
//		HazelcastConfig.getInstance().getDistinctSymbols().clear();
//		HazelcastConfig.getInstance().getLoadedSearchData().clear();
//		HazelcastConfig.getInstance().getFetchDataFromCache().clear();
//		HazelcastConfig.getInstance().getFetchDataFromCache().put(AppConstants.FETCH_DATA_FROM_CACHE, true);
//		entityManager.loadDistintValue(2);
//		entityManager.loadDistintValue(3);
//		contractService.loadContractMaster();
//		Log.info("Scheduler Completed");
//	}

	/**
	 * Scheduler to Load contract at morning 7:00 AM
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param execution
	 * @throws ServletException
	 */
	@Scheduled(cron = "0 0 7 * * ?")
	public void loadContractMaster(ScheduledExecution execution) throws ServletException {
		Log.info("Scheduler started to Load Contract Master");
		contractService.reloadContractMasterFile();
//		stockReturnService.reloadStockReturnFile();
		Log.info("Scheduler completed to Load Contract Master");
	}

	/**
	 * 
	 * Scheduler to delete contract at morning 6:30 AM (0:30 AM UTC)
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param execution
	 * @throws ServletException
	 */
	@Scheduled(cron = "0 30 6 * * ?")
	public void removeContract(ScheduledExecution execution) throws ServletException {
		Log.info("Scheduler started to Delete contracts");
		System.out.println("Scheduler started to Delete contracts");
//		contractService.deleteBSEContract();
		contractService.deleteExpiredContract();
		System.out.println("Scheduler completed to Delete contracts");
		Log.info("Scheduler completed to Delete contracts");
	}
}
