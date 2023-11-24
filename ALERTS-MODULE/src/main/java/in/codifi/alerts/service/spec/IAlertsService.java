package in.codifi.alerts.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.alerts.entity.primary.AlertsEntity;
import in.codifi.alerts.model.request.RequestModel;
import in.codifi.alerts.model.response.GenericResponse;
import in.codifi.cache.model.ClinetInfoModel;

public interface IAlertsService {

	/**
	 * method to create Alert
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> createAlert(RequestModel reqModel, ClinetInfoModel info);

	/**
	 * method to get alert Details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> getAlertDetails(ClinetInfoModel info);

	/**
	 * method to update Alert
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> updateAlert(AlertsEntity alertsEntity, ClinetInfoModel info);

	/**
	 * method to delete Alert
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> deleteAlert(int id, ClinetInfoModel info);

	/**
	 * method to update Trigger Status
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	RestResponse<GenericResponse> updateTriggerStatus(int id);

}
