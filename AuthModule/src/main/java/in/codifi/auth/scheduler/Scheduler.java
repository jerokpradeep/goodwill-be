package in.codifi.auth.scheduler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import in.codifi.auth.cache.CacheService;
import in.codifi.auth.servcie.AuthService;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;

@ApplicationScoped
public class Scheduler {

	@Inject
	CacheService cacheService;
	@Inject
	AuthService authService;

	/**
	 * 
	 * Scheduler to clear cache at morning 6:30 AM (1:00 AM UTC)
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param se
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void schedule(ScheduledExecution se) {

		/** method to clear cache **/
		cacheService.clearCache();
		authService.loadTwoFAUserPreference();

	}

}
