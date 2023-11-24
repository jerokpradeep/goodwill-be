package in.codifi.client.scheduler;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.springframework.scheduling.annotation.Scheduled;

import in.codifi.client.service.DataFeedService;
import in.codifi.client.service.PinStartBarService;
import in.codifi.client.service.PreferenceService;
import io.quarkus.scheduler.ScheduledExecution;

@ApplicationScoped
public class Scheduler {

	@Inject
	DataFeedService dataFeedService;

	@Inject
	PreferenceService preferenceService;

	@Inject
	PinStartBarService pinStartBarService;

	@Scheduled(cron = "0 30 0 * * ?")
	public void schedule(ScheduledExecution se) {

		/** method to get User Preference **/
		dataFeedService.loadStockData();
		preferenceService.loadMasterPreference();
		preferenceService.loadUserPreference();
		pinStartBarService.loadPinToStartBar();

	}

}
