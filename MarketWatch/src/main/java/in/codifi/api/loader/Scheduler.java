package in.codifi.api.loader;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;

import in.codifi.api.cache.MwCacheController;
import in.codifi.api.service.Cacheservice;
import in.codifi.api.service.MarketWatchService;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;

@ApplicationScoped
public class Scheduler {

	@Inject
	MarketWatchService service;
	@Autowired
	Cacheservice cacheservice;

	/**
	 * 
	 * Scheduler to delete contract at morning 6 AM (0:30 AM UTC)
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param execution
	 * @throws ServletException
	 */
	@Scheduled(cron = "0 30 0 * * ?")
	public void removeContract(ScheduledExecution execution) throws ServletException {
		Log.info("Scheduler started to Delete contracts");
		System.out.println("Scheduler started to Delete contracts");
		service.deleteExpiredContract();
		System.out.println("Scheduler completed to Delete contracts");
		Log.info("Scheduler completed to Delete contracts");
		Log.info("Scheduler started to reload Cache");
		cacheservice.loadUserMWData();
		MwCacheController.getAdvanceMWListByUserId().clear();
		Log.info("Scheduler completed to reload Cache");
		Log.info(" Predefined Market watch data pre-Lodings are started");
		cacheservice.loadPreDefinedMWData();
		Log.info("Predefined Market watch data pre-Lodings are ended");
	}
}
