package in.codifi.funds.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.sql.DataSource;

import in.codifi.cache.model.ClinetInfoModel;
import in.codifi.funds.config.HazelcastConfig;
import in.codifi.funds.model.response.BankDetails;
import in.codifi.funds.utility.MariaDBDataSourceFactory;
import in.codifi.funds.utility.StringUtil;
import in.codifi.funds.ws.model.BankDetailsRestResp;
import in.codifi.funds.ws.service.RazorpayRestService;
import io.quarkus.logging.Log;

@ApplicationScoped
public class BankDetailsEntityManager {

	@Inject
	DataSource dataSource;

	@Inject
	RazorpayRestService razorpayRestService;

	/**
	 * 
	 * Method to get
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param userId
	 * @return
	 */
	public List<BankDetails> getBankDetails(String userId) {
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		List<BankDetails> bankDetailsList = new ArrayList<>();
		try {
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"SELECT CLIENT_ID, CLIENT_NAME, BRANCH_CODE_NEW, BANK_ACNO, CLIENT_BANK_NAME,CLIENT_BANK_ADDRESS,"
							+ " DEFAULT_ACC_BANK, BANK_ACCTYPE, IFSCCODE from CLIENT_BANK_DETAILS WHERE CLIENT_ID = ? group by CLIENT_ID, CLIENT_NAME, BRANCH_CODE_NEW,"
							+ " BANK_ACNO, CLIENT_BANK_NAME,CLIENT_BANK_ADDRESS, DEFAULT_ACC_BANK, BANK_ACCTYPE, IFSCCODE");

			int paramPos = 1;
			pStmt.setString(paramPos++, userId);
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					BankDetails result = new BankDetails();
					String ifscCode = rSet.getString("IFSCCODE");
					String bankName = rSet.getString("CLIENT_BANK_NAME");
					String bankAccNo = rSet.getString("BANK_ACNO");
					ifscCode = ifscCode.replace(",", "");
					bankAccNo = bankAccNo.replace("'", "");
					result.setIfscCode(ifscCode);
					result.setBankName(bankName);
					bankAccNo = bankAccNo.replaceAll(".(?=.{4})", "*");
					result.setBankActNo(bankAccNo);
					String bankCode = "";
					if (HazelcastConfig.getInstance().getIfscCodeMapping().containsKey(ifscCode)) {
						bankCode = HazelcastConfig.getInstance().getIfscCodeMapping().get(ifscCode);
					} else {
						/** Get bank details by IFSC CODE **/
						BankDetailsRestResp bankdetails = razorpayRestService.getBankDetails(ifscCode);
						if (bankdetails != null && StringUtil.isNotNullOrEmpty(bankdetails.getBankcode())) {
							bankCode = bankdetails.getBankcode();
						}
					}
					if (StringUtil.isNotNullOrEmpty(bankCode)) {
						result.setBankCode(bankCode);
					} else {
						result.setBankCode("NA");
					}
					bankDetailsList.add(result);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} finally {
			try {
				rSet.close();
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				Log.error(e.getMessage());
			}
		}
		return bankDetailsList;
	}

	/**
	 * 
	 * Method to get bank details by bank number and ifsc code
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param ifsc
	 * @param accNo
	 * @param userId
	 * @return
	 */
	public BankDetails getBankDetailsByBankNo(String ifsc, String accNo, String userId) {
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		BankDetails result = null;
		try {
			dataSource = MariaDBDataSourceFactory.createDataSource();
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					" SELECT AccountId,  AccountName, BranchId, BankAccountNo,BankName, BankAddress, AccountType,IFSCode1"
							+ " FROM InvestorClient WHERE IFSCode1 = ? AND AccountId = ?"
							+ " GROUP BY AccountId,AccountName,BranchId,BankAccountNo, BankName, BankAddress,AccountType, IFSCode1");
			int paramPos = 1;
			pStmt.setString(paramPos++, ifsc);
			pStmt.setString(paramPos++, userId);
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					result = new BankDetails();
					String ifscCode = rSet.getString("IFSCode1");
					String bankName = rSet.getString("BankName");
					String bankAccNo = rSet.getString("BankAccountNo");
					String clientName = rSet.getString("AccountName");
					ifscCode = ifscCode.replace(",", "");
					bankAccNo = bankAccNo.replace("'", "");
					result.setIfscCode(ifscCode);
					result.setBankName(bankName);
					result.setBankActNo(bankAccNo);
					result.setClientName(clientName);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		} finally {
			try {
				rSet.close();
				pStmt.close();
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				Log.error(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * method to get user bank details from noren database
	 * 
	 * @author SowmiyaThangaraj
	 * @param info
	 * @return
	 */
	public List<BankDetails> getUserBankDetailsFromDB(ClinetInfoModel info) {
		List<BankDetails> bankDetails = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rSet = null;
		try {
			dataSource = MariaDBDataSourceFactory.createDataSource();
			conn = dataSource.getConnection();
			pStmt = conn.prepareStatement(
					"SELECT AccountId, AccountName, BranchId, BankAccountNo, BankName, BankAddress, AccountType, IFSCode1"
							+ "	FROM InvestorClient WHERE AccountId = ?"
							+ " GROUP BY AccountId,AccountName,BranchId,BankAccountNo, BankName, BankAddress,AccountType, IFSCode1");

			int paramPos = 1;
			pStmt.setString(paramPos++, info.getUserId());
			rSet = pStmt.executeQuery();
			if (rSet != null) {
				while (rSet.next()) {
					BankDetails result = new BankDetails();
					String ifscCode = rSet.getString("IFSCode1");
					String bankName = rSet.getString("BankName");
					String bankAccNo = rSet.getString("BankAccountNo");
					ifscCode = ifscCode.replace(",", "");
					bankAccNo = bankAccNo.replace("'", "");
					result.setIfscCode(ifscCode);
					result.setBankName(bankName);
					result.setClientName(rSet.getString("AccountName"));
					bankAccNo = bankAccNo.replaceAll(".(?=.{4})", "*");
					result.setBankActNo(bankAccNo);
					String bankCode = "";
					if (HazelcastConfig.getInstance().getIfscCodeMapping().containsKey(ifscCode)) {
						bankCode = HazelcastConfig.getInstance().getIfscCodeMapping().get(ifscCode);
					} else {
						/** Get bank details by IFSC CODE **/
						BankDetailsRestResp bankdetails = razorpayRestService.getBankDetails(ifscCode);
						if (bankdetails != null && StringUtil.isNotNullOrEmpty(bankdetails.getBankcode())) {
							bankCode = bankdetails.getBankcode();
						}
					}
					if (StringUtil.isNotNullOrEmpty(bankCode)) {
						result.setBankCode(bankCode);
					} else {
						result.setBankCode("NA");
					}
					bankDetails.add(result);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error("getUserBankDetailsFromDB", e);
		}
		return bankDetails;
	}

}
