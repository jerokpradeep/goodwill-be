package in.codifi.alerts.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.alerts.model.response.GenericResponse;
import in.codifi.cache.model.ClinetInfoModel;

public interface MessagesServiceSpec {

	/**
	 * method to get exch msg
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> exchMsg(ClinetInfoModel info, String exch);

	/**
	 * method to get brokerage message
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> getBrokerageMsg(ClinetInfoModel info);

	/**
	 * method to get exchange status
	 * 
	 * @author SOWMIYA
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> getExchStatus(ClinetInfoModel info);

}