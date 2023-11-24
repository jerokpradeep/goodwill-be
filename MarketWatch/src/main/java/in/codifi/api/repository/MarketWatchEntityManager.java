package in.codifi.api.repository;

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

import in.codifi.api.cache.HazelCacheController;
import in.codifi.api.model.CacheMwAdvDetailsModel;
import in.codifi.api.model.CacheMwDetailsModel;
import in.codifi.api.model.ResponseModel;
import in.codifi.api.util.AppConstants;
import in.codifi.api.util.PrepareResponse;
import in.codifi.cache.model.ContractMasterModel;
import io.quarkus.logging.Log;

@ApplicationScoped
public class MarketWatchEntityManager {

	@Inject
	EntityManager entityManager;

	@Inject
	PrepareResponse prepareResponse;

	/**
	 * Method to get market watch data by user Id
	 * 
	 * @author Dinesh Kumar
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<CacheMwDetailsModel> getMarketWatchByUserId(String userId) {
		List<CacheMwDetailsModel> response = new ArrayList<>();
		List<Object[]> responseList = new ArrayList<>();
		try {

			Query query = entityManager
					.createNativeQuery("SELECT A.mw_name, A.user_id, case when A.mw_id is null then 0 else A.mw_id end,"
							+ " B.exch, B.exch_seg, B.token, B.symbol, B.trading_symbol, B.formatted_ins_name, B.expiry_date, B.pdc,"
							+ " case when B.sorting_order is null then 0 else B.sorting_order end"
							+ " FROM tbl_market_watch_name as A  LEFT JOIN tbl_market_watch B on  A.mw_id = B.mw_id and"
							+ " A.user_id = B.user_id where A.user_id = :userId  order by A.user_id, A.mw_id , B.sorting_order");
			query.setParameter("userId", userId);
			responseList = query.getResultList();
			for (Object[] object : responseList) {
				CacheMwDetailsModel model = new CacheMwDetailsModel();
				model.setMwName(String.valueOf(object[0]));
				model.setUserId(String.valueOf(object[1]));
				int mwId = ((BigInteger) object[2]).intValue();
				model.setMwId(mwId);
				model.setExchange((String) object[3]);
				model.setSegment((String) object[4]);
				model.setToken((String) object[5]);
				model.setSymbol((String) object[6]);
				model.setTradingSymbol((String) object[7]);
				model.setFormattedInsName((String) object[8]);
				model.setExpiry((Date) object[9]);
				model.setPdc((String) object[10]);
				int sortOrder = ((BigInteger) object[11]).intValue();
				model.setSortOrder(sortOrder);
				response.add(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return response;
	}

	/**
	 * Method to get all market watch data
	 * 
	 * @author Dinesh Kumar
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<CacheMwAdvDetailsModel> getAllMarketWatch() {
		List<CacheMwAdvDetailsModel> response = new ArrayList<>();
		List<Object[]> responseList = new ArrayList<>();
		try {

			Query query = entityManager
					.createNativeQuery("SELECT A.mw_name, A.user_id, case when A.mw_id is null then 0 else A.mw_id end,"
							+ " B.exch, B.exch_seg, B.token, B.symbol, B.trading_symbol, B.formatted_ins_name, B.expiry_date, B.pdc,"
							+ " case when B.sorting_order is null then 0 else B.sorting_order end"
							+ " FROM tbl_market_watch_name as A  LEFT JOIN tbl_market_watch B on  A.mw_id = B.mw_id and"
							+ " A.user_id = B.user_id order by A.user_id, A.mw_id , B.sorting_order");

			responseList = query.getResultList();
			for (Object[] object : responseList) {
				CacheMwAdvDetailsModel model = new CacheMwAdvDetailsModel();
				model.setMwName(String.valueOf(object[0]));
				model.setUserId(String.valueOf(object[1]));
				int mwId = ((BigInteger) object[2]).intValue();
				model.setMwId(mwId);
				model.setExchange((String) object[3]);
				model.setSegment((String) object[4]);
				model.setToken((String) object[5]);
				model.setSymbol((String) object[6]);
				model.setTradingSymbol((String) object[7]);
				model.setFormattedInsName((String) object[8]);
				model.setExpiry((Date) object[9]);
				model.setPdc((String) object[10]);
				int sortOrder = ((BigInteger) object[11]).intValue();
				;
				model.setSortOrder(sortOrder);
				response.add(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return response;
	}

	/**
	 * Method to delete expired contract in MW List on DB
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param currentDate
	 * @return
	 */
	@Transactional
	public RestResponse<ResponseModel> deleteExpiredContract(String currentDate) {
		try {

			Query query = entityManager.createNativeQuery(
					"DELETE FROM tbl_market_watch a where a.expiry_date IS NOT NULL and a.expiry_date < :date");
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
	 * method to get market watch advance by userId
	 * 
	 * @author sowmiya
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<CacheMwAdvDetailsModel> getMarketWatchAdvByUserId(String userId) {
		List<CacheMwAdvDetailsModel> response = new ArrayList<>();
		List<Object[]> responseList = new ArrayList<>();
		try {

			Query query = entityManager
					.createNativeQuery("SELECT A.mw_name, A.user_id, case when A.mw_id is null then 0 else A.mw_id end,"
							+ " B.exch, B.exch_seg, B.token, B.symbol, B.trading_symbol, B.formatted_ins_name, B.expiry_date, B.pdc,"
							+ " case when B.sorting_order is null then 0 else B.sorting_order end"
							+ " FROM tbl_market_watch_name as A  LEFT JOIN tbl_market_watch B on  A.mw_id = B.mw_id and"
							+ " A.user_id = B.user_id where A.user_id = :userId  order by A.user_id, A.mw_id , B.sorting_order");
			query.setParameter("userId", userId);
			responseList = query.getResultList();
			for (Object[] object : responseList) {
				CacheMwAdvDetailsModel model = new CacheMwAdvDetailsModel();
				model.setMwName(String.valueOf(object[0]));
				model.setUserId(String.valueOf(object[1]));
				int mwId = ((BigInteger) object[2]).intValue();
				model.setMwId(mwId);
				model.setExchange((String) object[3]);
				model.setSegment((String) object[4]);
				model.setToken((String) object[5]);
				model.setSymbol((String) object[6]);
				model.setTradingSymbol((String) object[7]);
				model.setFormattedInsName((String) object[8]);
				model.setExpiry((Date) object[9]);
				model.setPdc((String) object[10]);
				int sortOrder = ((BigInteger) object[11]).intValue();
				model.setSortOrder(sortOrder);
				String key = model.getExchange() + "_" + model.getToken();
				if (HazelCacheController.getInstance().getContractMaster().get(key) != null) {
					ContractMasterModel contractModel = HazelCacheController.getInstance().getContractMaster().get(key);
					if (contractModel != null) {
						model.setCompanyName(contractModel.getCompanyName());
						model.setPdc(contractModel.getPdc());
					}
				}
				response.add(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return response;
	}

	/**
	 * Method to get Advance market watch data by user Id
	 * 
	 * @author sowmiya
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<CacheMwAdvDetailsModel> getAdvMarketWatchByUserId(String userId) {
		List<CacheMwAdvDetailsModel> response = new ArrayList<>();
		List<Object[]> responseList = new ArrayList<>();
		try {

			Query query = entityManager
					.createNativeQuery("SELECT A.mw_name, A.user_id, case when A.mw_id is null then 0 else A.mw_id end,"
							+ " B.exch, B.exch_seg, B.token, B.symbol, B.trading_symbol, B.formatted_ins_name, B.expiry_date, B.pdc,"
							+ " case when B.sorting_order is null then 0 else B.sorting_order end"
							+ " FROM tbl_market_watch_name as A  LEFT JOIN tbl_market_watch B on  A.mw_id = B.mw_id and"
							+ " A.user_id = B.user_id where A.user_id = :userId  order by A.user_id, A.mw_id , B.sorting_order");
			query.setParameter("userId", userId);
			responseList = query.getResultList();
			for (Object[] object : responseList) {
				CacheMwAdvDetailsModel model = new CacheMwAdvDetailsModel();
				model.setMwName(String.valueOf(object[0]));
				model.setUserId(String.valueOf(object[1]));
				int mwId = ((BigInteger) object[2]).intValue();
				model.setMwId(mwId);
				model.setExchange((String) object[3]);
				model.setSegment((String) object[4]);
				model.setToken((String) object[5]);
				model.setSymbol((String) object[6]);
				model.setTradingSymbol((String) object[7]);
				model.setFormattedInsName((String) object[8]);
				model.setExpiry((Date) object[9]);
				model.setPdc((String) object[10]);
				int sortOrder = ((BigInteger) object[11]).intValue();
				model.setSortOrder(sortOrder);
				response.add(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return response;
	}

}
