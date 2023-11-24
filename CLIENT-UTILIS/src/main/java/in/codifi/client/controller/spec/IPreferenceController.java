package in.codifi.client.controller.spec;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.client.model.request.PreferenceReqModel;
import in.codifi.client.model.response.GenericResponse;

public interface IPreferenceController {

	/**
	 * method to get User Preference
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Path("/web")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getPreferenceForWeb();

	/**
	 * Method to update User Preference for web
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Path("/web/update")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> updatePreferenceForWeb(PreferenceReqModel reqModel);

	/**
	 * method to reset Preference foe web
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Path("/web/reset")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> resetPreferenceForWeb();

	/**
	 * Method to get User Preference for mobile
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Path("/mob")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> getPreferenceForMobile();

	/**
	 * Method to update Preference for Mobile
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Path("/mob/update")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> updatePreferenceForMobile(PreferenceReqModel reqModel);

	/**
	 * method to reset Preference for mob
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Path("/mob/reset")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> resetPreferenceForMobile();

	/**
	 * Method to load user preference
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Path("/load/userpref")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> loadUserPreference();

	/**
	 * Method to load master preferences
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Path("/load/masterpref")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> loadMasterPreference();

	/**
	 * Method to update multiple preference for mobile
	 * @author DINESH KUMAR
	 *
	 * @param reqModel
	 * @return
	 */
	@Path("/mob/set")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	RestResponse<GenericResponse> updatePreferenceListForMobile(List<PreferenceReqModel> reqModel);

}
