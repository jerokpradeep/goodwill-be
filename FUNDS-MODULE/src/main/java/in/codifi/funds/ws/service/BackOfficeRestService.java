package in.codifi.funds.ws.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.funds.config.RestServiceProperties;
import in.codifi.funds.entity.primary.BOPaymentLogEntity;
import in.codifi.funds.entity.primary.BoPayInLogsEntity;
import in.codifi.funds.entity.primary.PaymentLogsEntity;
import in.codifi.funds.repository.BOPayInLogsRepository;
import in.codifi.funds.repository.BOPaymentLogRepository;
import in.codifi.funds.repository.PaymentLogsRepository;
import in.codifi.funds.utility.AppConstants;
import in.codifi.funds.utility.EmailUtils;
import in.codifi.funds.utility.PrepareResponse;
import io.quarkus.logging.Log;

@ApplicationScoped
public class BackOfficeRestService {
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	RestServiceProperties props;
	@Inject
	PaymentLogsRepository paymentLogRepo;
	@Inject
	BOPaymentLogRepository boPaymentLogRepository;
	@Inject
	EmailUtils emailUtils;
	@Inject
	BOPayInLogsRepository boPayInLogsRepository;

	public Object loginBackOffice(String userId, int retryCount) {
		Object object = new Object();
		// int retryCount = 0;
		try {
			Log.info("loginBackOffice to get bank details - " + props.getBoLoginUrl());
			URL url = new URL(props.getBoLoginUrl());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			Map<String, List<String>> headerName = conn.getHeaderFields();
			List<String> cookie = headerName.get("Set-Cookie");
			if (cookie != null && cookie.size() > 0) {
				BufferedReader br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output;
				while ((output = br1.readLine()) != null) {
					if (output.contains("Login Successfuly")) {
						object = getBankDetails(userId, cookie);
					} else {
						Log.error("BackOfficeLogin-" + output);
						retryCount++;
						if (retryCount > 2) {
							return null;
						} else {
							loginBackOffice(userId, retryCount);
						}

					}
				}
			} else {
				retryCount++;
				if (retryCount > 2) {
					return null;
				} else {
					loginBackOffice(userId, retryCount);
				}
			}

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}

		return object;
	}

	private Object getBankDetails(String userId, List<String> cookie) {
		Object object = new Object();
		try {
			URL url = new URL(props.getBoBankDetails() + userId);
			Log.info("getBankDetails url - " + props.getBoBankDetails() + userId);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			StringBuilder sb = new StringBuilder();
			if (cookie != null && cookie.size() > 0) {
				for (String tempCookiw : cookie) {
					sb.append(tempCookiw + " ");
				}
			}
			String sbStr = sb.toString().replaceAll("HTTPOnly", "");
			conn.setRequestProperty("Cookie", sbStr);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br1.readLine()) != null) {
				Log.info("getBankDetails response - " + output);
				if (output.contains("Please Defiend DataYear")) {
					loginBackOffice(userId, 0);
				} else {
					Log.info(userId + " - BankDetails - " + output);
					object = JSONValue.parse(output);
				}
			}

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();
		}

		return object;
	}

	/*
	 * method to bo pay in
	 * 
	 * @author SOWMIYA
	 *
	 */
	public Object boPayIn(String userId, String exchSegment, String orderId, double amount, String bnkAccNum,
			List<String> cookie, String paymentId, String paymentMethod, int retryCount) {
		Object responseObject = new JSONObject();
		String errorMessage = "";
		String originalResponse = "";
		URL url = null;
		// int retryCount = 0;
		try {
			String voucherDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			voucherDate = URLEncoder.encode(voucherDate, "UTF-8");
			String companyCode = exchSegment;
			String paymentReferenceNumber = orderId;
			String postingBankAccount = "RAZORPAY";
			String bankAccountNumber = bnkAccNum;
			String narration = "Razorpay Amount Ref." + orderId;
			narration = URLEncoder.encode(narration, "UTF-8");
			String entrytype = "PG";
			String liveExport = "0";
			String recodate = "";
			String cheque_can = "";
			String cheque_image = "";
			String unautho_flag = "";
			String actualTime = "";
			String mode = "";
			String refno = paymentId;
			url = new URL(props.getBoPayIn() + "VoucherDate=" + voucherDate + "&AccountCode=" + userId + "&COMPANYCODE="
					+ companyCode + "&PAYMENTREFERENCENUMBER=" + paymentReferenceNumber + "&Amount=" + amount
					+ "&PostingBankAccount=" + postingBankAccount + "&BankAccountNumber=" + bankAccountNumber
					+ "&NARRATION=" + narration + "&ENTRYTYPE=" + entrytype + "&LiveExport=" + liveExport + "&RecoDate="
					+ recodate + "&CHEQUE_CAN=" + cheque_can + "&Cheque_Image=" + cheque_image + "&Unautho_Flag="
					+ unautho_flag + "&REFNO=" + refno + "&ActualTime=" + actualTime + "&Mode=" + mode);
			Log.info("Back Office -Pay In Req at -" + System.currentTimeMillis() + "url - " + url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			StringBuilder sb = new StringBuilder();
			if (cookie != null && cookie.size() > 0) {
				for (String tempCookiw : cookie) {
					sb.append(tempCookiw + " ");
				}
			}
			String sbStr = sb.toString().replaceAll("HTTPOnly", "");
			conn.setRequestProperty("Cookie", sbStr);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br1.readLine()) != null) {
				originalResponse = originalResponse + output;
				Log.info("Back Ofiice -Pay In Res at -" + System.currentTimeMillis() + "Resp - " + output);
				if (output.contains("Please Defiend DataYear")) {
					retryCount++;
					if (retryCount > 2) {
						return null;
					} else {
						boPayIn(userId, exchSegment, paymentReferenceNumber, amount, bnkAccNum, cookie, paymentId,
								paymentMethod, retryCount);
					}
				} else {
					Log.info(userId + " - Payin - " + output);
					responseObject = JSONValue.parse(output);
				}
			}
			prepareBoPaymentLogs(orderId, userId, url, responseObject);
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				ObjectMapper mapper = new ObjectMapper();
				BoPayInLogsEntity entity = new BoPayInLogsEntity();
				entity.setOrderId(orderId);
				entity.setRequest(mapper.writeValueAsString(url));
				entity.setResponse(mapper.writeValueAsString(originalResponse));
				entity.setUserId(userId);
				entity.setErrorMsg(errorMessage);
				boPayInLogsRepository.save(entity);

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return responseObject;
	}

	/*
	 * method to prepare bo payments logs
	 * 
	 * @author SOWMIYA
	 */
	private PaymentLogsEntity prepareBoPaymentLogs(String orderId, String userId, URL url, Object responseObject) {
		ObjectMapper mapper = new ObjectMapper();
		PaymentLogsEntity paymentLogEntity = new PaymentLogsEntity();
		try {

			paymentLogEntity.setOrderId(orderId);
			paymentLogEntity.setUserId(userId);
			paymentLogEntity.setRequest(mapper.writeValueAsString(url));
			paymentLogEntity.setResponse(mapper.writeValueAsString(responseObject));
			paymentLogRepo.save(paymentLogEntity);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return paymentLogEntity;

	}

	/**
	 * Method to login for the back office login
	 * 
	 * @author SOWMIYA
	 * @param UserId
	 * @param exchSegment
	 * @param order       id
	 * @param amount
	 * @param bnkAccNum
	 * @return
	 */
	public Object loginBackOfficePayIn(String pUserId, String exchSegment, String referenceNu, double amount,
			String bnkAccNum, String paymentId, String paymentMethod, int retryCount) {
		BoPayInLogsEntity entity = new BoPayInLogsEntity();
		JSONObject object = new JSONObject();
		JSONArray arrayResponse = new JSONArray();
		URL url = null;
		String originalResponse = "";
		String errorMessage = "";
		// int retryCount = 0;
		try {
			url = new URL(props.getBoPayInLogin());
			System.out.println("bopayin login" + url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			Map<String, List<String>> headerName = conn.getHeaderFields();
			List<String> cookie = headerName.get("Set-Cookie");
			if (cookie != null && cookie.size() > 0) {
				BufferedReader br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output;
				while ((output = br1.readLine()) != null) {
					originalResponse = originalResponse + output;
					if (output.contains("Login Successfuly")) {
						object = (JSONObject) boPayIn(pUserId, exchSegment, referenceNu, amount, bnkAccNum, cookie,
								paymentId, paymentMethod, 0);
						if (object != null && !object.isEmpty()) {
							arrayResponse.add(object);
						}
						ObjectMapper mapper = new ObjectMapper();
						System.out.println(mapper.writeValueAsString(object));
						System.out.println("bopayin login" + url);
					} else {
						retryCount++;
						if (retryCount > 2) {
							return null;
						} else {
							loginBackOfficePayIn(pUserId, exchSegment, referenceNu, amount, bnkAccNum, paymentId,
									paymentMethod, retryCount);
						}
					}
				}
			}
		} catch (Exception e) {
			errorMessage = e.getMessage();
			String message = " Payment of user " + pUserId + " with bank account no : " + bnkAccNum + " with amount "
					+ amount + " is Failed with razorpay Id : " + paymentId + "   reason: " + errorMessage;

			entity.setIsError(1);
			emailUtils.paymentFailureEmail(message);
			e.printStackTrace();
		} finally {
			try {
				ObjectMapper mapper = new ObjectMapper();
				entity.setOrderId(referenceNu);
				entity.setRequest(mapper.writeValueAsString(url));
				entity.setResponse(mapper.writeValueAsString(originalResponse));
				entity.setUserId(pUserId);
				entity.setErrorMsg(errorMessage);
				boPayInLogsRepository.save(entity);

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return arrayResponse;
	}

	/*
	 * method to login back office to check bank balance
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	public Object loginBackOfficeCheckBalance(String userId, int retryCount) {
		JSONArray object = new JSONArray();
		// int retryCount = 0;
		try {
			String boPayInLogin = props.getBoPayInLogin();
			Log.info("boPayInLogin-" + boPayInLogin);
			URL url = new URL(boPayInLogin);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.GET_METHOD);
			conn.setDoOutput(true);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			Map<String, List<String>> headerName = conn.getHeaderFields();
			List<String> cookie = headerName.get("Set-Cookie");
			if (cookie != null && cookie.size() > 0) {
				BufferedReader br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output;
				while ((output = br1.readLine()) != null) {
					Log.info("boPayInLogin resp -" + output);
					if (output.contains("Login Successfuly")) {
						object = (JSONArray) getCheckBalance(userId, cookie, 0);
					} else {
						retryCount++;
						if (retryCount > 2) {
							return null;
						} else {
							loginBackOfficeCheckBalance(userId, retryCount);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return object;
	}

	/*
	 * method to check bank balance
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private JSONArray getCheckBalance(String userId, List<String> cookie, int retryCount) {
		JSONArray response = new JSONArray();
		// int retryCount = 0;
		try {
			String voucherDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			voucherDate = URLEncoder.encode(voucherDate, "UTF-8");
			String checkBalanceUrl = props.getBoPayoutCheckMargin() + userId + AppConstants.SYMBOL_AND + "From_date"
					+ AppConstants.SYMBOL_EQUAL + voucherDate;
			Log.info("checkBalanceUrl-" + checkBalanceUrl);
			URL url = new URL(checkBalanceUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.GET_METHOD);
			conn.setDoOutput(true);
			StringBuilder sb = new StringBuilder();
			if (cookie != null && cookie.size() > 0) {
				for (String tempCookiw : cookie) {
					sb.append(tempCookiw + " ");
				}
			}
			String sbStr = sb.toString().replaceAll("HTTPOnly", "");
			conn.setRequestProperty("Cookie", sbStr);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br1.readLine()) != null) {
				Log.info("checkBalanceResp-" + output);
				if (output.contains("Please Defiend DataYear")) {
					retryCount++;
					if (retryCount > 2) {
						return null;
					} else {
						getCheckBalance(userId, cookie, retryCount);
					}
				} else {
//					checkBalanceResp = mapper.readValue(output, PayoutCheckBalanceRestResp.class);
					Log.info(userId + " - CheckBalance - " + output);
					JSONObject jsonObject = (JSONObject) JSONValue.parse(output);
					if (jsonObject != null && !jsonObject.isEmpty()) {
						response.add(jsonObject);
					}
//					response = (JSONArray) JSONValue.parse(output);
//					System.out.println(jsonObject);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/*
	 * method to login back office to bo payout
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	public JSONArray loginBackOfficeBopayOut(String userId, String accNum, String ifscCode, String exchSeg, double amt,
			String payoutReason, int retryCount) {
		JSONArray object = new JSONArray();
//		//int retryCount = 0;
		try {
			String boBankUrl = props.getBoLoginUrl();
			Log.info("boBankUrl - " + boBankUrl);
			URL url = new URL(boBankUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(AppConstants.GET_METHOD);
			conn.setDoOutput(true);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			Map<String, List<String>> headerName = conn.getHeaderFields();
			List<String> cookie = headerName.get("Set-Cookie");
			if (cookie != null && cookie.size() > 0) {
				BufferedReader br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output;
				while ((output = br1.readLine()) != null) {
					Log.info("boBank resp - " + output);
					if (output.contains("Login Successfuly")) {
						object = (JSONArray) boPayout(userId, accNum, ifscCode, exchSeg, amt, cookie, payoutReason, 0);
					} else {
						retryCount++;
						if (retryCount > 2) {
							return null;
						} else {
							loginBackOfficeBopayOut(userId, accNum, ifscCode, exchSeg, amt, payoutReason, retryCount);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return object;
	}

	/*
	 * method to bo payout
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	private JSONArray boPayout(String userId, String accNum, String ifscCode, String exchSeg, double amt,
			List<String> cookie, String payoutReason, int retryCount) {
		JSONArray response = new JSONArray();
		// int retryCount = 0;
		try {
			String payoutUrl = props.getBoPayOut() + userId + AppConstants.SYMBOL_AND + "branch_code"
					+ AppConstants.SYMBOL_EQUAL + ifscCode + AppConstants.SYMBOL_AND + "company_code"
					+ AppConstants.SYMBOL_EQUAL + exchSeg + AppConstants.SYMBOL_AND + "Amount"
					+ AppConstants.SYMBOL_EQUAL + amt + AppConstants.SYMBOL_AND + "BankAccountNumber"
					+ AppConstants.SYMBOL_EQUAL + accNum + AppConstants.SYMBOL_AND + "BankNo"
					+ AppConstants.SYMBOL_EQUAL + accNum;
			URL url = new URL(payoutUrl);

			Log.info("Payout url - " + payoutUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoOutput(true);
			conn.setConnectTimeout(15000);
			StringBuilder sb = new StringBuilder();
			if (cookie != null && cookie.size() > 0) {
				for (String tempCookiw : cookie) {
					sb.append(tempCookiw + " ");
				}
			}
			String sbStr = sb.toString().replaceAll("HTTPOnly", "");
			conn.setRequestProperty("Cookie", sbStr);
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader br1 = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			String output;
			while ((output = br1.readLine()) != null) {
				Log.info("Payout resp - " + output);
				if (output.contains("Please Defiend DataYear")) {
					retryCount++;
					if (retryCount > 2) {
						return null;
					} else {
						boPayout(userId, accNum, ifscCode, exchSeg, amt, cookie, payoutReason, retryCount);
					}
				} else {
					int activeStatus = 1;
					if (amt == 0) {
						activeStatus = 0;
					}
					Log.info(userId + " - Payout - " + output);
					response = (JSONArray) JSONValue.parse(output);
					boPaymentLogRepository.updateBOPaymentStatus(userId);
					boPaymentLogRepository.flush();
					BOPaymentLogEntity paymentLogEntity = prepareBOPayment(userId, String.valueOf(amt), url, response,
							accNum, ifscCode, exchSeg, activeStatus, payoutReason);
					if (paymentLogEntity != null)
						paymentLogEntity = boPaymentLogRepository.saveAndFlush(paymentLogEntity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/*
	 * method to prepare bo payout details
	 * 
	 * @author SOWMIYA
	 * 
	 * @return
	 */
	private BOPaymentLogEntity prepareBOPayment(String userId, String amt, URL url, JSONArray response, String accNum,
			String ifscCode, String exchSeg, int activeStatus, String payoutReason) {
		BOPaymentLogEntity entity = new BOPaymentLogEntity();
		ObjectMapper mapper = new ObjectMapper();
		try {
			entity.setActiveStatus(activeStatus);
			entity.setUserId(userId);
			entity.setSegment(exchSeg);
			entity.setIfscCode(ifscCode);
			entity.setRequest(mapper.writeValueAsString(url));
			entity.setResponse(mapper.writeValueAsString(response));
			entity.setAmount(amt);
			entity.setBankActNo(accNum);
			entity.setPayoutReason(payoutReason);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return entity;
	}
}
