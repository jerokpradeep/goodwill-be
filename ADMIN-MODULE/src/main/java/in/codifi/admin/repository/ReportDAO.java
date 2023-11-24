package in.codifi.admin.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

import org.json.simple.JSONArray;

import in.codifi.admin.model.request.PaymentRefReqModel;
import in.codifi.admin.model.request.PostionAvgPriceReqModel;
import in.codifi.admin.model.response.BankDetailsResponseModel;
import in.codifi.admin.model.response.HoldingCountResponseModel;
import in.codifi.admin.model.response.HoldingsResponseModel;
import in.codifi.admin.model.response.PaymentOutResponseModel;
import in.codifi.admin.model.response.PaymentRefResponseModel;
import in.codifi.admin.model.response.PostionAvgPriceResponseModel;
import in.codifi.admin.utility.StringUtil;
import io.quarkus.logging.Log;

@SuppressWarnings("unchecked")
@ApplicationScoped
public class ReportDAO {
	@Inject
	DataSource datasource;

	/**
	 * Method to get holdings data
	 * 
	 * @author LOKESH
	 * @return
	 */

	public List<HoldingsResponseModel> getHoldingsData(HoldingsResponseModel holdingsModel) {
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		PreparedStatement pStmt1 = null;
		ResultSet rSet1 = null;
		List<HoldingsResponseModel> jsonList = new ArrayList<HoldingsResponseModel>();
		try {
			conn = datasource.getConnection();
			String date = holdingsModel.getCreatedOn();
			Calendar cal = Calendar.getInstance();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String currentDate = dateFormat.format(cal.getTime());
			if (currentDate.equalsIgnoreCase(date)) {
				String table = "tbl_holdings_data";
				pStmt = conn.prepareStatement("SELECT id,user_id,buy_value,buy_avg,isin,quantity,symbol,"
						+ " bse_code,nse_code,holdings_type,req_id,txn_id,poa_status,auth_flag,auth_qty,auth_time,"
						+ " created_on,updated_on FROM " + table + " where user_id= ? and created_on like ? ");
				int paramPos = 1;
				pStmt.setString(paramPos++, holdingsModel.getUserId());
				pStmt.setString(paramPos++, holdingsModel.getCreatedOn() + "%");
				rSet = pStmt.executeQuery();
				if (rSet != null) {
					while (rSet.next()) {
						HoldingsResponseModel result = new HoldingsResponseModel();
						result.setId(rSet.getInt("id"));
						result.setUserId(rSet.getString("user_id"));
						result.setUserId(rSet.getString("buy_value"));
						result.setUserId(rSet.getString("buy_avg"));
						result.setUserId(rSet.getString("isin"));
						result.setUserId(rSet.getString("quantity"));
						result.setUserId(rSet.getString("symbol"));
						result.setUserId(rSet.getString("bse_code"));
						result.setUserId(rSet.getString("nse_code"));
						result.setUserId(rSet.getString("holdings_type"));
						result.setUserId(rSet.getString("req_id"));
						result.setUserId(rSet.getString("txn_id"));
						result.setUserId(rSet.getString("poa_status"));
						result.setUserId(rSet.getString("auth_flag"));
						result.setUserId(rSet.getString("auth_qty"));
						result.setUserId(rSet.getString("auth_time"));
						result.setUserId(rSet.getString("created_on"));
						result.setUserId(rSet.getString("updated_on"));

						jsonList.add(result);
					}
				}
			} else {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
				Date date5 = formatter.parse(date);
				DateFormat dateFormat1 = new SimpleDateFormat("ddMMyyyy");
				String strDate = dateFormat1.format(date5);
				pStmt1 = conn.prepareStatement("show tables where tables_in_alice_blue_db like ? ");
				int parampos = 1;
				pStmt1.setString(parampos, "tbl_holdings_data_" + strDate + "%");
				rSet1 = pStmt1.executeQuery();
				if (rSet1 != null) {
					while (rSet1.next()) {
						String table = (rSet1.getString(1));
						pStmt = conn.prepareStatement("SELECT id,user_id,buy_value,buy_avg,isin,quantity,symbol,"
								+ " bse_code,nse_code,holdings_type,req_id,txn_id,poa_status,auth_flag,auth_qty,auth_time,"
								+ " created_on,updated_on  FROM " + table + " where user_id= ? and created_on like ? ");
						int paramPos = 1;
						pStmt.setString(paramPos++, holdingsModel.getUserId());
						pStmt.setString(paramPos++, holdingsModel.getCreatedOn() + "%");
						rSet = pStmt.executeQuery();
						if (rSet != null) {
							while (rSet.next()) {
								HoldingsResponseModel result = new HoldingsResponseModel();
								result.setId(rSet.getInt("id"));
								result.setUserId(rSet.getString("user_id"));
								result.setUserId(rSet.getString("buy_value"));
								result.setUserId(rSet.getString("buy_avg"));
								result.setUserId(rSet.getString("isin"));
								result.setUserId(rSet.getString("quantity"));
								result.setUserId(rSet.getString("symbol"));
								result.setUserId(rSet.getString("bse_code"));
								result.setUserId(rSet.getString("nse_code"));
								result.setUserId(rSet.getString("holdings_type"));
								result.setUserId(rSet.getString("req_id"));
								result.setUserId(rSet.getString("txn_id"));
								result.setUserId(rSet.getString("poa_status"));
								result.setUserId(rSet.getString("auth_flag"));
								result.setUserId(rSet.getString("auth_qty"));
								result.setUserId(rSet.getString("auth_time"));
								result.setUserId(rSet.getString("created_on"));
								result.setUserId(rSet.getString("updated_on"));

								jsonList.add(result);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
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
				Log.error(e.getMessage());
			}
		}
		return jsonList;
	}

	/**
	 * Method to get Position Avg User
	 * 
	 * @author LOKESH
	 * @return
	 */

	public List<PostionAvgPriceResponseModel> getPositionAvgUser(PostionAvgPriceResponseModel postionResponseModel) {
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		List<PostionAvgPriceResponseModel> jsonList = new ArrayList<PostionAvgPriceResponseModel>();
		try {
			conn = datasource.getConnection();
			pStmt = conn.prepareStatement("SELECT id, exchange, client_id, instrument_name, symbol, expiry_date, "
					+ "strike_price, option_type, net_qty, net_rate, token, created_on, created_by "
					+ " FROM  tbl_position_avg_price where client_id = ?");
			int paramPos = 1;
			pStmt.setString(paramPos++, postionResponseModel.getClientId());
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					PostionAvgPriceResponseModel result = new PostionAvgPriceResponseModel();
					result.setId(rSet.getInt("id"));
					result.setExchange(rSet.getString("exchange"));
					result.setClientId(rSet.getString("client_id"));
					result.setInstrumentName(rSet.getString("instrument_name"));
					result.setSymbol(rSet.getString("symbol"));
					result.setExpiryDate(rSet.getString("expiry_date"));
					result.setStrikePrice(rSet.getString("strike_price"));
					result.setOptionType(rSet.getString("option_type"));
					result.setNetQty(rSet.getString("net_qty"));
					result.setNetRate(rSet.getString("net_rate"));
					result.setToken(rSet.getString("token"));
					result.setCreatedOn(rSet.getString("created_on"));
					jsonList.add(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
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
				Log.error(e.getMessage());
			}
		}
		return jsonList;
	}

	/**
	 * Method to get the User Bank details for the given User Id
	 * 
	 * @author LOKESH
	 * @return
	 */
	public JSONArray getUserBankDetails(String clientId) {
		JSONArray response = new JSONArray();
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		try {
			int paromPos = 1;
			conn = datasource.getConnection();
			pStmt = conn.prepareStatement(
					"SELECT CLIENT_ID, CLIENT_NAME, BRANCH_CODE_NEW, BANK_ACNO, CLIENT_BANK_NAME, CLIENT_BANK_ADDRESS, DEFAULT_ACC_BANK, BANK_ACCTYPE, IFSCCODE "
							+ "from CLIENT_BANK_DETAILS WHERE CLIENT_ID =  ?  group by  "
							+ "CLIENT_ID, CLIENT_NAME, BRANCH_CODE_NEW, BANK_ACNO, CLIENT_BANK_NAME,CLIENT_BANK_ADDRESS, DEFAULT_ACC_BANK, BANK_ACCTYPE, IFSCCODE ");
			pStmt.setString(paromPos++, clientId);
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					BankDetailsResponseModel result = new BankDetailsResponseModel();
					result.setClientId(rSet.getString("CLIENT_ID"));
					result.setClientname(rSet.getString("CLIENT_NAME"));
					result.setBranchCode(rSet.getString("BRANCH_CODE_NEW"));
					result.setAccNo(rSet.getString("BANK_ACNO"));
					result.setBankName(rSet.getString("CLIENT_BANK_NAME"));
					result.setBankAddress(rSet.getString("CLIENT_BANK_ADDRESS"));
					result.setDefaultAccBank(rSet.getString("DEFAULT_ACC_BANK"));
					result.setAcctype(rSet.getString("BANK_ACCTYPE"));
					result.setIfscode(rSet.getString("IFSCCODE"));
					response.add(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
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
				Log.error(e.getMessage());
			}
		}
		return response;
	}

	/**
	 * Method to get the Position avg Price Count
	 * 
	 * @author LOKESH
	 * @return
	 */
	public List<PostionAvgPriceReqModel> getPosAvgCount() {
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		PostionAvgPriceReqModel result = null;
		List<PostionAvgPriceReqModel> response = new ArrayList<PostionAvgPriceReqModel>();
		try {
			conn = datasource.getConnection();
			pStmt = conn.prepareStatement(
					" SELECT exchange , count(*) as totalCount FROM  tbl_position_avg_price group by exchange ");
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					result = new PostionAvgPriceReqModel();
					result.setExchange(rSet.getString("exchange"));
					result.setTotalCount(rSet.getString("totalCount"));
					response.add(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
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
				Log.error(e.getMessage());
			}
		}
		return response;
	}

	public List<PaymentRefResponseModel> getPaymentRefreneceDetailsByDate(PaymentRefReqModel paymentRefModel) {
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		PreparedStatement pStmt1 = null;
		ResultSet rSet1 = null;
		String count = "";
		String fromdate = paymentRefModel.getFromDate() + " 00:00:00";
		String toDate = paymentRefModel.getToDate() + " 23:59:59";
		String fromAmount = paymentRefModel.getFromAmount();
		String toAmount = paymentRefModel.getToAmount();
		if (!fromAmount.contains(".")) {
			fromAmount = fromAmount + ".0";
		}
		if (!toAmount.contains(".")) {
			toAmount = toAmount + ".0";
		}
		List<PaymentRefResponseModel> jsonList = new ArrayList<PaymentRefResponseModel>();
		try {
			conn = datasource.getConnection();
			pStmt1 = conn.prepareStatement("select count(*) as con from  tbl_payment_ref where payment_status like ? "
					+ " and created_on between ? and ? and user_id like ? and amount between ? and ?"
					+ " and payment_method like ? and exch_seg like ?");
			int paramPos1 = 1;
			pStmt1.setString(paramPos1++, paymentRefModel.getPaymentStatus());
			pStmt1.setString(paramPos1++, fromdate);
			pStmt1.setString(paramPos1++, toDate);
			pStmt1.setString(paramPos1++, paymentRefModel.getUserId());
			pStmt1.setDouble(paramPos1++, Double.parseDouble(fromAmount));
			pStmt1.setDouble(paramPos1++, Double.parseDouble(toAmount));
			pStmt1.setString(paramPos1++, paymentRefModel.getPaymentMethod());
			pStmt1.setString(paramPos1++, paymentRefModel.getExchSeg());
			rSet1 = pStmt1.executeQuery();
			if (rSet1 != null) {
				while (rSet1.next()) {
					count = rSet1.getString("con");
				}
			}
			pStmt = conn.prepareStatement(
					"select user_id , order_id,receipt_id,bank_name,exch_seg,upi_id,amount,request,payment_status,bo_update,payment_method,"
							+ " voucher_no,created_on , acc_num  from  tbl_payment_ref where payment_status like ? "
							+ " and created_on between ? and ? and user_id like ? and amount between ? and ?"
							+ "  and payment_method like ? and exch_seg like ? limit 10 offset ? ");
			int paramPos = 1;
			pStmt.setString(paramPos++, paymentRefModel.getPaymentStatus());
			pStmt.setString(paramPos++, fromdate);
			pStmt.setString(paramPos++, toDate);
			pStmt.setString(paramPos++, paymentRefModel.getUserId());
			pStmt.setDouble(paramPos++, Double.parseDouble(fromAmount));
			pStmt.setDouble(paramPos++, Double.parseDouble(toAmount));
			pStmt.setString(paramPos++, paymentRefModel.getPaymentMethod());
			pStmt.setString(paramPos++, paymentRefModel.getExchSeg());
			pStmt.setInt(paramPos++, paymentRefModel.getOffSet());
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					PaymentRefResponseModel result = new PaymentRefResponseModel();
					result.setUser_id(rSet.getString("user_id"));
					result.setOrder_id(rSet.getString("order_id"));
					result.setReceipt_id(rSet.getString("receipt_id"));
					result.setBank_name(rSet.getString("bank_name"));
					result.setAcc_num(rSet.getString("acc_num"));
					result.setExch_seg(rSet.getString("exch_seg"));
					result.setPayment_method(rSet.getString("payment_method"));
					result.setUpi_id(rSet.getString("upi_id"));
					result.setAmount(rSet.getString("amount"));
					result.setRequest(rSet.getString("request"));
					result.setPayment_status(rSet.getString("payment_status"));
					result.setBo_update(rSet.getString("bo_update"));
					result.setVoucher_no(rSet.getString("voucher_no"));
					result.setCreated_on(rSet.getString("created_on"));
					result.setCount(count);
					jsonList.add(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
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
				Log.error(e.getMessage());
			}
		}
		return jsonList;
	}

	public List<PaymentOutResponseModel> getPaymentOutDetailsByDate(PaymentRefReqModel paymentOutModel) {
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		PreparedStatement pStmt1 = null;
		ResultSet rSet1 = null;
		String count = "";
		String fromAmount = paymentOutModel.getFromAmount();
		String toAmount = paymentOutModel.getToAmount();
		List<PaymentOutResponseModel> jsonList = new ArrayList<PaymentOutResponseModel>();
		String fromdate = paymentOutModel.getFromDate() + " 00:00:00";
		String toDate = paymentOutModel.getToDate() + " 23:59:59";
		if (!fromAmount.contains(".")) {
			fromAmount = fromAmount + ".0";
		}
		if (!toAmount.contains(".")) {
			toAmount = toAmount + ".0";
		}
		try {
			conn = datasource.getConnection();
			pStmt1 = conn.prepareStatement(
					"SELECT count(*) as con FROM tbl_bo_payout_log where created_on between ? and ? and user_id like ?"
							+ " and amount between ? and ? and exch_seg like ?");
			int paramPos1 = 1;
			pStmt1.setString(paramPos1++, fromdate);
			pStmt1.setString(paramPos1++, toDate);
			pStmt1.setString(paramPos1++, paymentOutModel.getUserId());
			pStmt1.setDouble(paramPos1++, Double.parseDouble(fromAmount));
			pStmt1.setDouble(paramPos1++, Double.parseDouble(toAmount));
			pStmt1.setString(paramPos1++, paymentOutModel.getExchSeg());
			rSet1 = pStmt1.executeQuery();
			if (rSet1 != null) {
				while (rSet1.next()) {
					count = rSet1.getString("con");
				}
			}
			pStmt = conn.prepareStatement(
					"SELECT user_id,bank_acc_no,ifsc_code,exch_seg,amount,request,response,created_on,payout_reason "
							+ " FROM tbl_bo_payout_log where created_on between ? and ? and user_id like ?"
							+ " and amount between ? and ? and exch_seg like ? limit 10 offset ? ");
			int paramPos = 1;
			pStmt.setString(paramPos++, fromdate);
			pStmt.setString(paramPos++, toDate);
			pStmt.setString(paramPos++, paymentOutModel.getUserId());
			pStmt.setDouble(paramPos++, Double.parseDouble(fromAmount));
			pStmt.setDouble(paramPos++, Double.parseDouble(toAmount));
			pStmt.setString(paramPos++, paymentOutModel.getExchSeg());
			pStmt.setInt(paramPos++, paymentOutModel.getOffSet());
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					PaymentOutResponseModel result = new PaymentOutResponseModel();
					result.setUser_id(rSet.getString("user_id"));
					result.setBank_acc_no(rSet.getString("bank_acc_no"));
					result.setIfsc_code(rSet.getString("ifsc_code"));
					result.setExch_seg(rSet.getString("exch_seg"));
					result.setAmount(rSet.getString("amount"));
					result.setRequest(rSet.getString("request"));
					result.setResponse(rSet.getString("response"));
					result.setCreated_on(rSet.getString("created_on"));
					String payOutReason = rSet.getString("payout_reason");
					if (StringUtil.isNotNullOrEmpty(payOutReason)) {
						result.setPayOutReason(payOutReason);
					} else {
						result.setPayOutReason("payOutReason");
					}
					result.setCount(count);
					jsonList.add(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
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
				Log.error(e.getMessage());
			}
		}
		return jsonList;
	}

	public HoldingCountResponseModel getHoldingsCount() {
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		HoldingCountResponseModel result = null;
		try {
			conn = datasource.getConnection();
			pStmt = conn.prepareStatement(" SELECT count(*) as totalCount FROM tbl_holdings_data");
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					result = new HoldingCountResponseModel();
					result.setExchange("NSE");
					result.setTotalCount(rSet.getString("totalCount"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
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
				Log.error(e.getMessage());
			}
		}
		return result;
	}

}
