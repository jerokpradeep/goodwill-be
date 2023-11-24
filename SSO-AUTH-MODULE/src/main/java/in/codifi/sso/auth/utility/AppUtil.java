package in.codifi.sso.auth.utility;

import javax.enterprise.context.ApplicationScoped;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.sso.auth.controller.DefaultRestController;

@ApplicationScoped
public class AppUtil extends DefaultRestController {

	/**
	 * 
	 * Method to get client info
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public ClinetInfoModel getClientInfo() {
		ClinetInfoModel model = clientInfo();
		return model;
	}

}