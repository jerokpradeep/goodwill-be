package in.codifi.scrips.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.scrips.config.HazelcastConfig;
import in.codifi.scrips.model.response.GenericResponse;
import in.codifi.scrips.utility.AppConstants;
import in.codifi.scrips.utility.PrepareResponse;
import io.quarkus.logging.Log;

@ApplicationScoped
public class ContractEntityManager {

	@Inject
	EntityManager entityManager;

	@Inject
	PrepareResponse prepareResponse;

	/**
	 * Method to delete expired contract in DB
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param currentDate
	 * @return
	 */
	@Transactional
	public RestResponse<GenericResponse> deleteExpiredContract(String currentDate) {
		try {

			Query query = entityManager
					.createNativeQuery("DELETE FROM tbl_global_contract_master_details a where a.expiry_date < :date");
			query.setParameter("date", currentDate);
			int deleteCount = query.executeUpdate();
			Log.info("Expired Contract ->" + deleteCount + "-" + AppConstants.RECORD_DELETED);
			return prepareResponse.prepareSuccessMessage(deleteCount + "-" + AppConstants.RECORD_DELETED);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.DELETE_FAILED);
	}

	/**
	 * 
	 * Method to Delete BSE contract
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Transactional
	public RestResponse<GenericResponse> deleteBSEContract() {
		try {

			Query query = entityManager.createNativeQuery(
					"DELETE FROM tbl_global_contract_master_details a where exch in('BSE','BFO','BCD')");
			int deleteCount = query.executeUpdate();
			Log.info("BSE Contract ->" + deleteCount + "-" + AppConstants.RECORD_DELETED);
			return prepareResponse.prepareSuccessMessage(deleteCount + "-" + AppConstants.RECORD_DELETED);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.DELETE_FAILED);
	}

	/**
	 * 
	 * Method to load isin into cache
	 * 
	 * @author Dinesh Kumar
	 *
	 */
	public void loadIsinByToken() {
		List<Object[]> result = null;
		try {
			Query query = entityManager.createNativeQuery(
					"SELECT token,exch,isin FROM tbl_global_contract_master_details where isin is not null and isin != '' and exch in('BSE','NSE')");
			result = query.getResultList();
			for (Object[] values : result) {
				HazelcastConfig.getInstance().getIsinByTokenKB().put(values[0].toString() + "_" + values[1].toString(),
						values[2].toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
