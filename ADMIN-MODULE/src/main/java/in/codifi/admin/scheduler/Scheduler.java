package in.codifi.admin.scheduler;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.ServletException;

import in.codifi.admin.service.LogsService;
import in.codifi.admin.service.UserService;
import in.codifi.cache.CacheService;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;

@ApplicationScoped
public class Scheduler {
	@Inject
	LogsService adminService;
	@Inject
	CacheService cacheService;
	@Inject
	UserService userService;

	@Scheduled(cron = "0 30 6 * * ?")
	public void run(ScheduledExecution execution) throws ServletException {

		Log.info("Scheduler started at -" + new Date());
		adminService.checkAccessLogTable();
		adminService.checkRestAccessLogTable();
		cacheService.loadTokenForPosition();
	}

	@Scheduled(cron = "0 5 0 * * ?")
	public void truncate(ScheduledExecution execution) throws ServletException {

		Log.info("truncate started at -" + new Date());
		userService.truncateUserLoggedInDetails();
		Log.info("truncate done successfully ");

	}
}
