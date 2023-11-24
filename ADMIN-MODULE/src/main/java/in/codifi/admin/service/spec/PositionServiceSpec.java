package in.codifi.admin.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.model.request.FormDataModel;
import in.codifi.admin.model.request.PositionReqModel;
import in.codifi.admin.model.response.GenericResponse;

public interface PositionServiceSpec {

	/**
	 * Method to upload position file
	 *
	 * @author SOWMIYA
	 *
	 * @param file
	 * @param exchange
	 * @return
	 */
	RestResponse<GenericResponse> uploadPositionFile(FormDataModel file, String exchange);

	/**
	 * Method to
	 *
	 * @author SOWMIYA
	 *
	 * @return
	 */
	RestResponse<GenericResponse> insertPositionFile();

	/**
	 * method to get positionAvg user
	 * 
	 * @author SOWMIYA
	 * @param reqModel
	 * @return
	 */
	RestResponse<GenericResponse> getPositionAvgUser(PositionReqModel reqModel);

	/**
	 * method to get position count by exchange
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	RestResponse<GenericResponse> getPositionCountByExch();

}
