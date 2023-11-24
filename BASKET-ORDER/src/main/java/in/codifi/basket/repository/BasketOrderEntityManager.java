package in.codifi.basket.repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.basket.model.request.RetrieveBasketModel;
import in.codifi.basket.model.response.GenericResponse;
import in.codifi.basket.utils.AppConstants;
import in.codifi.basket.utils.PrepareResponse;
import io.quarkus.logging.Log;

@ApplicationScoped
public class BasketOrderEntityManager {

	@Inject
	EntityManager entityManager;
	@Inject
	PrepareResponse prepareResponse;

	/**
	 * Method to get basket order report by user Id
	 * 
	 * @author Gowthaman M
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<RetrieveBasketModel> getBasketReports(String userId) {
		List<RetrieveBasketModel> response = new ArrayList<>();
		List<Object[]> responseList = new ArrayList<>();
		try {
			Query query = entityManager.createNativeQuery(
					"select a.basket_id as basketId, a.basket_name as basketName, a.is_executed as executedStatus, "
							+ "a.created_on as createdOn, (select count(*) from tbl_basket_order_scrip b where b.basket_id = a.basket_id) "
							+ "as scripCount from tbl_basket_order a where user_id = :user_id");
			query.setParameter("user_id", userId);
			responseList = query.getResultList();
			for (Object[] object : responseList) {
				RetrieveBasketModel model = new RetrieveBasketModel();
				int boId = ((BigInteger) object[0]).intValue();
				model.setBasketId(boId);
				model.setBasketName(String.valueOf(object[1]));
				model.setIsExecuted(String.valueOf(object[2]));
				Date createdOn = (Date) object[3];
				model.setCreatedOn(createdOn);
				int scripCount = ((BigInteger) object[4]).intValue();
				model.setScripCount(scripCount);
				response.add(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return response;
	}

	/**
	 * 
	 * Method to delete expired scrips from basket
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	@Transactional
	public RestResponse<GenericResponse> deleteExpiredScrip() {
		try {

			Query query = entityManager
					.createNativeQuery("DELETE FROM tbl_basket_order_scrip e where e.expiry < current_date");
			long deleteCount = query.executeUpdate();
			Log.info("Expired basket Contract ->" + deleteCount + "-" + AppConstants.RECORD_DELETED);
			return prepareResponse.prepareSuccessMessage(deleteCount + "-" + AppConstants.RECORD_DELETED);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.DELETE_FAILED);
	}

	/**
	 * 
	 * Method to delete scrips from basket
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param basketId
	 * @param list
	 * @return
	 */
	@Transactional
	public int deleteBasketScrip(long basketId, List<Long> list) {
		int deleteCount = 0;
		try {

			Query query = entityManager.createNativeQuery(
					"DELETE FROM tbl_basket_order_scrip e where e.basket_id = :basketId and e.id IN (:idS)");
			query.setParameter("basketId", basketId);
			query.setParameter("idS", list);
			deleteCount = query.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return deleteCount;
	}

}
