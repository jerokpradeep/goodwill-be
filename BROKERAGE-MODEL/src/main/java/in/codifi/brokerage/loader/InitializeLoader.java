package in.codifi.brokerage.loader;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import in.codifi.brokerage.repository.BrokerageRepository;
import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
@SuppressWarnings("serial")
public class InitializeLoader extends HttpServlet {
	@Inject
	BrokerageRepository repository;

	public void init(@Observes StartupEvent ev) throws ServletException {
		repository.loadBrokerage();
	}

}
