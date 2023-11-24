package in.codifi.scrips.repository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import in.codifi.scrips.config.HazelcastConfig;
import in.codifi.scrips.model.request.SearchScripReqModel;
import in.codifi.scrips.model.response.ScripSearchResp;
import in.codifi.scrips.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class ScripSearchEntityManager {

	@Inject
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Transactional
	public List<ScripSearchResp> getScrips(SearchScripReqModel reqModel) {
		List<ScripSearchResp> respone = new ArrayList<>();
		try {

			String symbol = reqModel.getSymbol();
			String[] exch = reqModel.getExchange();
			if (exch == null || exch.length <= 0) {
				return respone;
			}
			if (Arrays.stream(exch).anyMatch("all"::equalsIgnoreCase)) {
				exch = null;
			}
			String stringQuery = "";
			String questionCount = "";
			String whereClause = "";
			String caseCondition = "";
			if (exch != null && exch.length > 0) {
				List<String> exchList = new ArrayList<String>(Arrays.asList(exch));
				exch = exchList.toArray(new String[0]);
				String ques = "";
				for (int i = 0; i < exch.length; i++) {
					ques = ques + "?,";
				}
				questionCount = ques.substring(0, ques.length() - 1);
			}

//			String sqlQuery1 = "SELECT exch,exchange_segment,symbol,token,instrument_name FROM tbl_global_contract_details_master ";
//			String sqlQuery2 = " sort_order1, symbol, expiry_date, strike_price, sort_order2, sort_order3 limit 50";
			String sqlQuery1 = "SELECT exch, exchange_segment, group_name, symbol, token, instrument_type, formatted_ins_name,week_tag,company_name,expiry_date FROM tbl_global_contract_master_details ";
			String sqlQuery2 = " sort_order_1, symbol, expiry_date, strike_price, sort_order_2, sort_order_3 limit 50";

			/**
			 * To Add no of question mark in where condition base of exchange. If exchange
			 * is all no need to put in where condition
			 **/
			String[] keys = symbol.trim().split(" ");
			if (exch != null && exch.length > 0) {
				whereClause = " WHERE active_status = ? and exch IN(" + questionCount + ")";
			} else {
				whereClause = " WHERE active_status = ? ";
			}

			if (keys != null && keys.length > 0) {
				String tempWhereClause = "";
				String tempCaseClause = "";

				/**
				 * To add where class based on search key If search key is like 'NIFTY BANK',
				 * check in instrument_name column else search key is like 'NIFTY', check in
				 * symbol column
				 **/

				if (keys.length == 1 && keys[0] != null && keys[0].trim().length() < 4) {
					tempWhereClause = tempWhereClause + " and (symbol like '" + keys[0] + "%' )";
				} else {
					for (String tempSymbol : keys) {
						tempWhereClause = tempWhereClause + " and ( instrument_name like '%" + tempSymbol
//						tempWhereClause = tempWhereClause + " and ( trading_symbol like '%" + tempSymbol
								+ "%' or symbol like '" + tempSymbol + "%' )";
					}
				}
				if (keys[0] != null && keys[0].trim().length() > 0) {
					tempCaseClause = " case" + tempCaseClause + " when symbol like '" + keys[0] + "%' then -1 ";
					tempCaseClause = tempCaseClause + " when instrument_name like '" + keys[0]
//					tempCaseClause = tempCaseClause + " when trading_symbol like '" + keys[0]
							+ "%' then 0  else 3 end ,";
				}
				whereClause = whereClause + tempWhereClause;
				caseCondition = caseCondition + tempCaseClause;
			}

			stringQuery = sqlQuery1 + whereClause + " ORDER BY  " + caseCondition + sqlQuery2;

			Query query = entityManager.createNativeQuery(stringQuery);

			/** set param position **/
			int paramPosition = 1;
			query.setParameter(paramPosition++, 1);
			if (exch != null && exch.length > 0) {
				for (int i = 0; i < exch.length; i++) {
					query.setParameter(paramPosition++, exch[i]);
				}
			}

			List<Object[]> result = query.getResultList();
			for (Object[] object : result) {
				ScripSearchResp model = new ScripSearchResp();
				String exchange = String.valueOf(object[0]);
				String segment = String.valueOf(object[1]);
				String groupName = String.valueOf(object[2]);
				String resSymbol = String.valueOf(object[3]);
				String token = String.valueOf(object[4]);
				String insType = String.valueOf(object[5]);
				String formattedInsName = String.valueOf(object[6]);
				String weekTag = String.valueOf(object[7]);
				String companyName = String.valueOf(object[8]);
				if (object[9] != null) {
					Date expiry = (Date) object[9];
					model.setExpiry(expiry);
				}

				model.setExchange(exchange);
				model.setSegment(segment);
				model.setToken(token);
				model.setFormattedInsName(formattedInsName);
				model.setWeekTag(weekTag);
				model.setCompanyName(companyName);

				if (exchange.equalsIgnoreCase("NSE")) {
					model.setSymbol(resSymbol + "-" + groupName);
				} else {
					model.setSymbol(resSymbol);
				}
				if ((exchange.equalsIgnoreCase("NSE") || exchange.equalsIgnoreCase("BSE")
						|| exchange.equalsIgnoreCase("MCX")) && StringUtil.isNotNullOrEmpty(insType)
						&& insType.equalsIgnoreCase("INDEX")) {
					model.setSegment("INDEX");
					model.setSymbol(resSymbol);
				}
				respone.add(model);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return respone;
	}

	/**
	 * To get symbol name
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param symbolLength
	 */
	public void loadDistintValue(int symbolLength) {
		List<String> result = null;

		try {
			Query query = entityManager.createNativeQuery(
					"Select distinct(symbol) as symbol from tbl_global_contract_master_details where CHAR_LENGTH(symbol) = ?");

			int paramPos = 1;
			query.setParameter(paramPos++, symbolLength);
			result = query.getResultList();
			if (result != null && result.size() > 0) {
				HazelcastConfig.getInstance().getDistinctSymbols().put(symbolLength, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
	}

	/**
	 * To load index scrips
	 * 
	 * @author Dinesh Kumar
	 *
	 */
	public void loadIndexValue() {

		try {
//			String sqlQuery1 = "SELECT exch, exchange_segment, group_name, symbol, token, instrument_type,instrument_name, formatted_ins_name,"
//		    + " display_name FROM tbl_global_contract_details_master where instrument_type = ?";
			String sqlQuery1 = "SELECT exch, exchange_segment, group_name, symbol, token, instrument_type, formatted_ins_name"
					+ " FROM tbl_global_contract_master_details where instrument_type = ?";
			Query query = entityManager.createNativeQuery(sqlQuery1);
			int paramPos = 1;
			query.setParameter(paramPos++, "INDEX");

			List<Object[]> result = query.getResultList();
			for (Object[] object : result) {
				ScripSearchResp dto = new ScripSearchResp();
				String exch = String.valueOf(object[0]);
				String segment = String.valueOf(object[1]);
				String groupName = String.valueOf(object[2]);
				String symbol = String.valueOf(object[3]);
				String token = String.valueOf(object[4]);
				String insType = String.valueOf(object[5]);
				String formattedInsName = String.valueOf(object[6]);
				dto.setExchange(exch);
				dto.setSegment(segment);
				dto.setToken(token);
				dto.setFormattedInsName(formattedInsName);
				if (exch.equalsIgnoreCase("NSE")) {
					dto.setSymbol(symbol + "-" + groupName);
				} else {
					dto.setSymbol(symbol);
				}

				if ((exch.equalsIgnoreCase("NSE") || exch.equalsIgnoreCase("BSE") || exch.equalsIgnoreCase("MCX"))
						&& StringUtil.isNotNullOrEmpty(insType) && insType.equalsIgnoreCase("INDEX")) {
					dto.setSegment("INDEX");
					dto.setSymbol(symbol);
				}
				HazelcastConfig.getInstance().getIndexDetails().put(symbol, dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
	}

}
