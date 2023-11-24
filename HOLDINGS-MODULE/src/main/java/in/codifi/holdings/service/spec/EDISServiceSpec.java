package in.codifi.holdings.service.spec;

import java.util.List;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.holdings.model.request.EdisReqModel;
import in.codifi.holdings.model.response.GenericResponse;

public interface EDISServiceSpec {

	/**
	 * method to initialized edis request
	 * 
	 * @author SOWMIYA
	 * @param model
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> initializeEdisRequest(List<EdisReqModel> model, ClinetInfoModel info);

	/**
	 * method to get hs token
	 * @author SowmiyaThangaraj
	 * @param info
	 * @return
	 */
	RestResponse<GenericResponse> getHSToken(ClinetInfoModel info);
}

//	/**
//	 * method to revocation return
//	 * 
//	 * @author SowmiyaThangaraj
//	 * @param req
//	 * @param userId
//	 */
//	void revocationReturn(String req, String userId);
//
//}
