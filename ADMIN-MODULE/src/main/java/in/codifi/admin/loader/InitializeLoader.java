package in.codifi.admin.loader;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import in.codifi.admin.service.LogsService;
import in.codifi.cache.CacheService;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
@SuppressWarnings("serial")
public class InitializeLoader extends HttpServlet {
	@Inject
	LogsService adminService;
	@Inject
	CacheService cacheService;

	public void init(@Observes StartupEvent ev) throws ServletException {

		adminService.checkAccessLogTable();
		adminService.checkRestAccessLogTable();
		Log.info("tables are created successfully");
		cacheService.loadTokenForPosition();
		cacheService.loadTokenForHoldings();

	}

}
