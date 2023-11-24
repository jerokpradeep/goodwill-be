package in.codifi.basket.scheduler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.springframework.scheduling.annotation.Scheduled;

import in.codifi.cache.CacheService;
import io.quarkus.scheduler.ScheduledExecution;

@ApplicationScoped
public class Scheduler {

	@Inject
	CacheService cacheService;

	/**
	 * 
	 * Scheduler to delete expired contract at morning 6:30 AM (1:00 AM UTC)
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param se
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void schedule(ScheduledExecution se) {

		/** method to load Cache **/
//		cacheService.loadCache();

		/** method to delete expired scrip **/
		cacheService.deleteExpiry();

	}

}
