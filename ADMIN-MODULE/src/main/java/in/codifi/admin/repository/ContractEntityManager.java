package in.codifi.admin.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;

import in.codifi.admin.config.HazelcastConfig;
import in.codifi.admin.model.response.ContractMasterRespModel;
import in.codifi.admin.model.response.ContractResultModel;
import in.codifi.admin.model.response.ExchangeResponseModel;

@ApplicationScoped
public class ContractEntityManager {

	@Inject
	DataSource dataSource;
	@Inject
	EntityManager entityManager;

	/**
	 * Method to load token for position
	 *
	 * @author Gowthaman M
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public void loadTokenForPosition() {
		List<Object[]> result = null;
		try {
			String exchanges = "MCX,NFO,CDS";
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
	 * Method to load token for holdings
	 *
	 * @author Gowthaman M
	 * @return
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
				if (values[6] != null) {
					model.setOptionType(values[6].toString());
				}
//				model.setOptionType(values[6].toString());
//				model.setStrikePrice(values[7].toString());
				if (values[7] != null) {
					model.setOptionType(values[7].toString());
				}
				model.setInstrumentName(values[8].toString());
				model.setFormattedInsName(values[9].toString());
				model.setTradingSymbol(values[10].toString());
//				model.setExpiryDate(values[11].toString());
				if (values[11] != null) {
					model.setOptionType(values[11].toString());
				}
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
							+ " FROM tbl_global_contract_master_details"
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

	public boolean addNewContractInMaster(ExchangeResponseModel exchangeModel, int sortOrder1, int sortOrder2,
			int sortOrder3) {
		Connection conn = null;
		boolean isSuccessful = false;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
//		java.sql.Timestamp timestamp = new java.sql.Timestamp(Calendar.getInstance().getTimeInMillis());
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"INSERT INTO tbl_global_contract_master_details(exch, exchange_segment, group_name, symbol, token, instrument_type, "
							+ " option_type, strike_price, formatted_ins_name, trading_symbol,company_name, expiry_date, lot_size, tick_size,pdc, "
							+ " alter_token,freeze_qty,isin,week_tag,sort_order_1, sort_order_2, sort_order_3,instrument_name) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			int paramPos = 1;
			pStmt.setString(paramPos++, exchangeModel.getExch());
			pStmt.setString(paramPos++, exchangeModel.getExchange_segment());
			pStmt.setString(paramPos++, exchangeModel.getGroup_name());
			pStmt.setString(paramPos++, exchangeModel.getSymbol());
			pStmt.setString(paramPos++, exchangeModel.getToken());
			pStmt.setString(paramPos++, exchangeModel.getInstrument_type());
			pStmt.setString(paramPos++, exchangeModel.getOption_type());
			pStmt.setString(paramPos++, exchangeModel.getStrike_price());
			pStmt.setString(paramPos++, exchangeModel.getFormatted_ins_name());
			pStmt.setString(paramPos++, exchangeModel.getTrading_symbol());
			pStmt.setString(paramPos++, exchangeModel.getCompany_name());
			pStmt.setString(paramPos++, exchangeModel.getExpiry_date());
			pStmt.setString(paramPos++, exchangeModel.getLot_size());
			pStmt.setString(paramPos++, exchangeModel.getTick_size());
			pStmt.setString(paramPos++, exchangeModel.getPdc());
			pStmt.setString(paramPos++, exchangeModel.getAlter_token());
			pStmt.setString(paramPos++, exchangeModel.getFreeze_qty());
			pStmt.setString(paramPos++, exchangeModel.getIsin());
			pStmt.setString(paramPos++, exchangeModel.getWeek_tag());
			pStmt.setInt(paramPos++, sortOrder1);
			pStmt.setInt(paramPos++, sortOrder2);
			pStmt.setInt(paramPos++, sortOrder3);
			pStmt.setString(paramPos++, exchangeModel.getInstrument_name());
//			pStmt.setInt(paramPos++, sortOrder);
			isSuccessful = pStmt.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rSet != null) {
					rSet.close();
				}
				if (pStmt != null) {
					pStmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isSuccessful;
	}

	public List<String> getDuplicateSymbols() {
		List<String> duplicateSymbols = new ArrayList<String>();
		Connection conn1 = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		ResultSet rSet1 = null;
		try {
			conn1 = dataSource.getConnection();
			pStmt = conn1.prepareStatement("select distinct exchange_segment as exSeg from"
					+ "  tbl_global_contract_master_details  where exchange_segment in (\"nse_cm\",\"bse_cm\")");
			rSet1 = pStmt.executeQuery();
			if (rSet1 != null) {
				while (rSet1.next()) {
					String symbol = rSet1.getString("exSeg");
					pStmt = conn1.prepareStatement(
							"select distinct(symbol) as sym, count(symbol) as con  FROM tbl_global_contract_master_details "
									+ "where exchange_segment = ? group by symbol having count(symbol) > 1");
					int paramPos = 1;
					pStmt.setString(paramPos++, symbol);
					rSet = pStmt.executeQuery();
					if (rSet != null) {
						while (rSet.next()) {
							duplicateSymbols.add(symbol + "-" + rSet.getString("sym") + "-" + rSet.getString("con"));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rSet != null) {
					rSet.close();
				}
				if (pStmt != null) {
					pStmt.close();
				}
				if (conn1 != null) {
					conn1.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return duplicateSymbols;
	}
}
