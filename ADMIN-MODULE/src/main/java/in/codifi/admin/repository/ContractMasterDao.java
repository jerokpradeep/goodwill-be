package in.codifi.admin.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import in.codifi.admin.config.HazelcastConfig;
import in.codifi.admin.model.response.ContractMasterRespModel;
import in.codifi.admin.model.response.ContractResultModel;

@ApplicationScoped
public class ContractMasterDao {

	@Inject
	EntityManager entityManager;

	/**
	 * 
	 * Method to load token for position
	 *
	 * @author SOWMIYA
	 *
	 */
	@SuppressWarnings("unchecked")
	public void loadTokenForPosition() {
		List<Object[]> result = null;
		try {
			String exchanges = "MCX,NFO,CDS,BFO";
			List<String> exch = Arrays.asList(exchanges.split(","));
			Query query = entityManager.createNativeQuery(
					"select instrument_name,formatted_ins_name,token,exch from tbl_global_contract_master_details"
							+ " where exch IN(:exch) and instrument_type not in (:instrType)");

			query.setParameter("exch", exch);// TODO BCD,BFO
			query.setParameter("instrType", "INDEX");
			result = query.getResultList();
			for (Object[] values : result) {
				String instrumentName = values[0].toString();
				String token = values[1].toString() + "_" + values[2].toString() + "_" + values[3].toString();
				HazelcastConfig.getInstance().getPositionContract().put(instrumentName, token);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Method to load token for holdings
	 *
	 * @author SOWMIYA
	 *
	 */
	@SuppressWarnings("unchecked")
	public void loadTokenForHoldings() {
		List<Object[]> result = null;
		try {
			Query query = entityManager.createNativeQuery(
					"SELECT exch , isin , token FROM tbl_global_contract_master_details where isin is not null and isin != ''");

			result = query.getResultList();
			for (Object[] values : result) {
				HazelcastConfig.getInstance().getHoldingsContract()
						.put(values[0].toString() + "_" + values[1].toString(), values[2].toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * method to get contract master list
	 * 
	 * @author SOWMIYA
	 * @param exch
	 * @param expiry
	 * @param group
	 * @param symbol
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ContractMasterRespModel getContractMasterList(String exch, String expiry, String group, String symbol) {
		ContractMasterRespModel responseModel = new ContractMasterRespModel();
		List<ContractResultModel> resultModel = new ArrayList<>();
		List<Object[]> result = null;
		try {
			Query query = entityManager
					.createNativeQuery("SELECT id,exchange_segment,group_name,symbol,token,instrument_type,"
							+ " option_type,strike_price,instrument_name,formatted_ins_name,"
							+ " trading_symbol,expiry_date,lot_size,tick_size,isin"
							+ " FROM tbl_global_contract_master_details where exchange_segment = :exchSeg"
							+ " and (symbol like :symbol or instrument_name like :instName or formatted_ins_name like :forInsName)"
							+ " and (expiry_date like :expiryDate or expiry_date is null) and (group_name like :groupName or group_name is null)limit 100 ");

			query.setParameter("exchSeg", exch);
			query.setParameter("symbol", symbol);
			query.setParameter("instName", symbol);
			query.setParameter("forInsName", symbol);
			query.setParameter("expiryDate", expiry);
			query.setParameter("groupName", group);
			result = query.getResultList();
			for (Object[] values : result) {
				ContractResultModel model = new ContractResultModel();
				model.setExchangeSegment(values[1].toString());
				if (values[2] != null) {
					model.setGroupName(values[2].toString());
				}
				model.setSymbol(values[3].toString());
				model.setToken(values[4].toString());
				model.setInstrumentType(values[5].toString());
				model.setOptionType(values[6].toString());
				model.setStrikePrice(values[7].toString());
				model.setInstrumentName(values[8].toString());
				model.setFormattedInsName(values[9].toString());
				model.setTradingSymbol(values[10].toString());
				model.setExpiryDate(values[11].toString());
				model.setLotSize(values[12].toString());
				model.setTickSize(values[13].toString());
				if (values[14] != null) {
					model.setIsin(values[14].toString());
				}
				resultModel.add(model);
			}
			responseModel.setResult(resultModel);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseModel;
	}

	/**
	 * method to get distinct exch segment
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public List<String> getDistinctExchSeg() {
		List<String> response = new ArrayList<>();
		try {
			List<String> exch = new ArrayList<>();
			exch.add("nse_cm");
			exch.add("bse_cm");
			Query query = entityManager.createNativeQuery(
					"select distinct exchange_segment as exSeg from tbl_global_contract_master_details where exchange_segment In (:exch)");

			query.setParameter("exch", exch);
			@SuppressWarnings("unchecked")
			List<String> result = query.getResultList();

			response.addAll(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * method to get newly added list symbol
	 * 
	 * @author SOWMIYA
	 * @param exchangeSeg
	 * @return
	 */
	public List<String> getNewlyAddedListSymbol(String exchangeSeg) {
		List<String> newlyAddedList = new ArrayList<>();
		String groupName = "";
		try {
			Query query = entityManager
					.createNativeQuery("select exchange_segment, group_name, symbol, instrument_name ,token"
							+ " FROM tbl_global_contract_master_details"
							+ " where exchange_segment = :exchSeg AND token NOT IN "
							+ " (SELECT token FROM tbl_global_contract_details_archive where exchange_segment = :exchSeg)");
			query.setParameter("exchSeg", exchangeSeg);
			@SuppressWarnings("unchecked")
			List<Object[]> result = query.getResultList();
			for (Object[] values : result) {
				String exchangeSegment = values[0].toString();
				if (values[1] != null) {
					groupName = values[1].toString();
				}
				String symbol = values[2].toString();
				String insName = values[3].toString();
				String token = values[4].toString();
				String newlyAddedSymbol = exchangeSegment + "-" + groupName + "-" + symbol + "-" + insName + "-"
						+ token;
				newlyAddedList.add(newlyAddedSymbol);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return newlyAddedList;
	}

	/**
	 * method to get deactivated symbol list
	 * 
	 * @author SOWMIYA
	 * @param exchangeSeg
	 * @return
	 */
	public List<String> getDeactivatedSymbol(String exchangeSeg) {
		List<String> deactivatedList = new ArrayList<>();
		String groupName = "";
		try {
			Query query = entityManager
					.createNativeQuery("select exchange_segment, group_name, symbol, instrument_name ,token"
							+ " FROM tbl_global_contract_details_archive"
							+ " where exchange_segment = :exchSeg AND token NOT IN "
							+ " (SELECT token FROM tbl_global_contract_master_details where exchange_segment = :exchSeg)");
			query.setParameter("exchSeg", exchangeSeg);
			@SuppressWarnings("unchecked")
			List<Object[]> result = query.getResultList();
			for (Object[] values : result) {
				String exchangeSegment = values[0].toString();
				if (values[1] != null) {
					groupName = values[1].toString();
				}
				String symbol = values[2].toString();
				String insName = values[3].toString();
				String token = values[4].toString();
				String deActivatedSymbol = exchangeSegment + "," + groupName + "," + symbol + "," + insName + ","
						+ token;
				deactivatedList.add(deActivatedSymbol);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return deactivatedList;
	}

}
