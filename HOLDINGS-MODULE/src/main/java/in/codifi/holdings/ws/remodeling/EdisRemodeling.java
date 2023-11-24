package in.codifi.holdings.ws.remodeling;

import javax.enterprise.context.ApplicationScoped;

import in.codifi.holdings.model.transformation.EdisRespModel;
import in.codifi.holdings.ws.model.EDISRestRespModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class EdisRemodeling {

	/**
	 * Method to bind EDIS response
	 * 
	 * @author DINESH KUMAR
	 *
	 * @param respModel
	 * @return
	 */
	public EdisRespModel bindEdisResponse(EDISRestRespModel respModel) {

		EdisRespModel response = new EdisRespModel();
		try {
			if (respModel != null) {
				response.setDpId(respModel.getDPId());
				response.setEncData(respModel.getEncdata());
				response.setReqId(respModel.getReqid());
			}
		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
			throw new RuntimeException();
		}
		return response;
	}
}
