package in.codifi.client.loader;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.http.HttpServlet;

import in.codifi.client.service.DataFeedService;
import in.codifi.client.service.PinStartBarService;
import in.codifi.client.service.PreferenceService;
import io.quarkus.runtime.StartupEvent;

@SuppressWarnings("serial")
public class LoadData extends HttpServlet {

	@Inject
	DataFeedService dataFeedService;
	@Inject
	PinStartBarService pinStartBarService;
	@Inject
	PreferenceService preferenceService;

	public void init(@Observes StartupEvent ev) throws Exception {

		/** method to load User Preference **/
		dataFeedService.loadStockData();
		preferenceService.loadMasterPreference();
		preferenceService.loadUserPreference();
//		pinStartBarService.loadPinToStartBar();

	}

}
