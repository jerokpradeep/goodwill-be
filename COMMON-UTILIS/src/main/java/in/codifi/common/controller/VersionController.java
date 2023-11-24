package in.codifi.common.controller;

import javax.ws.rs.Path;

import org.jboss.resteasy.reactive.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;

import in.codifi.common.controller.spec.VersionControllerSpec;
import in.codifi.common.entity.VersionEntity;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.service.spec.VersionServiceSpec;

@Path("/version")
public class VersionController implements VersionControllerSpec {

	@Autowired
	VersionServiceSpec versionServiceSpec;

	/**
	 * method to verify version
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> findVersion(VersionEntity versionEntity) {
		return versionServiceSpec.findVersion(versionEntity);
	}

}
