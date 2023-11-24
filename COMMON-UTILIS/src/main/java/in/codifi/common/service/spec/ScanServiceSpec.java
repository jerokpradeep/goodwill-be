package in.codifi.common.service.spec;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.common.model.response.GenericResponse;

public interface ScanServiceSpec {

	/**
	 * method to get equity scan details by scan master model
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	RestResponse<GenericResponse> findEquityScan();

	/**
	 * method to get equtiy scan from data base
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	RestResponse<GenericResponse> findEquityScanAll();

	/**
	 * method to get Future Scan details by scan master model
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	RestResponse<GenericResponse> findFutureScan();

	/**
	 * method to get Future Scan details from database
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	RestResponse<GenericResponse> findFutureScanAll();

}
