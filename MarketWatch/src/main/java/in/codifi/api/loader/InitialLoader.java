package in.codifi.api.loader;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.beans.factory.annotation.Autowired;

import in.codifi.api.cache.MwCacheController;
import in.codifi.api.service.AdvanceMWService;
import in.codifi.api.service.Cacheservice;
import in.codifi.api.service.MarketWatchService;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
@SuppressWarnings("serial")
public class InitialLoader extends HttpServlet {
	@Autowired
	Cacheservice cacheservice;
	@Autowired
	MarketWatchService marketWatchService;
	@Inject
	AdvanceMWService advanceMW;

	public void init(@Observes StartupEvent ev) throws ServletException {

		cacheservice.loadUserMWData();
		MwCacheController.getAdvanceMWListByUserId().clear();
		System.out.println(" Market watch data pre-Lodings are ended");
		advanceMW.loadPredefinedMWData();
		System.out.println("Predefined Market watch data pre-Lodings are ended");
//		advanceMW.getTopGainers();
//		advanceMW.getTopLosers();
//		advanceMW.getFiftytwoWeekHigh();
//		advanceMW.getFiftytwoWeekLow();
//		advanceMW.predefinedAdvanceKey();
		Log.info("loaded successfully");

	}
}
