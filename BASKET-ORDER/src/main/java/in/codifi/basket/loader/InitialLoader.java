package in.codifi.basket.loader;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import in.codifi.cache.CacheService;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;

@SuppressWarnings("serial")
@ApplicationScoped
public class InitialLoader extends HttpServlet {

	@Inject
	CacheService cacheService;

	public void init(@Observes StartupEvent ev) throws ServletException {

		cacheService.deleteExpiry();
		Log.info("All the pre-Lodings are loaded");
	}

}
