package in.codifi.admin.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.admin.entity.PayoutDetailsEntity;
import in.codifi.admin.model.response.GenericResponse;
import in.codifi.admin.model.response.PayoutResponse;
import in.codifi.admin.repository.PayoutDetailsRepository;
import in.codifi.admin.req.model.PayoutReq;
import in.codifi.admin.service.spec.PaymentServiceSpec;
import in.codifi.admin.utility.AppConstants;
import in.codifi.admin.utility.PrepareResponse;
import in.codifi.admin.utility.StringUtil;

@ApplicationScoped
public class PaymentService implements PaymentServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	PayoutDetailsRepository payoutDetailsRepository;

	/**
	 * Method to get payment Detail
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> getPayoutDetails(PayoutReq payoutReq) {
		if (payoutReq.getFromDate() == null && payoutReq.getToDate() == null)
			return prepareResponse.prepareFailedResponse(AppConstants.INVALID_PARAMETERS);

		List<PayoutDetailsEntity> response = payoutDetailsRepository.getPayoutDetails(payoutReq.getFromDate(), payoutReq.getToDate());
		if (StringUtil.isListNullOrEmpty(response))
			return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);

		List<PayoutResponse> payoutResponse = PreparePayoutResponse(response);
		return prepareResponse.prepareSuccessResponseObject(payoutResponse);
	}

	/**
	 * Method to Prepare Payout Response
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	private List<PayoutResponse> PreparePayoutResponse(List<PayoutDetailsEntity> response) {
		List<PayoutResponse> responses = new ArrayList<>();
		for(PayoutDetailsEntity rSet : response) {
			PayoutResponse result = new PayoutResponse();
			result.setAmount(rSet.getAmount());
			result.setClientBankAccountNumber(rSet.getBankActNo());
			result.setClientBankcode(rSet.getBankCode());
			result.setClientBankIfscCode(rSet.getIfscCode());
			Date date = rSet.getCreatedOn();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	        String formattedDate = dateFormat.format(date);
			result.setDate(formattedDate);
			result.setDEFAULT("0");
			result.setLdCode(rSet.getBackofficeCode());
			result.setPayment(rSet.getPayment());
			result.setPaymentMode(rSet.getPayMethod());
			responses.add(result);
		}
		return responses;
	}
	
	/**
	 * Method to download payout details
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Override
	public Response downloadPayoutDetails(PayoutReq payoutReq) {
	    List<PayoutDetailsEntity> response = payoutDetailsRepository.getPayoutDetails(payoutReq.getFromDate(), payoutReq.getToDate());
	    List<PayoutResponse> payoutResponse = PreparePayoutResponse(response);

	    StringBuilder fileContent = new StringBuilder();
	    fileContent.append("DEFAULT|DATE|LDCODE|AMOUNT|DEFAULT|CLIENT BANKCODE|PAYMENT MODE|CLIENT BANK ACCOUNT NUMBER|CLIENT BANK IFSC CODE|PAYMENT|\n");

	    for (PayoutResponse rSet : payoutResponse) {
	        String data = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|\n",
	        		rSet.getDEFAULT(),
	                rSet.getDate(),
	                rSet.getLdCode(),
	                rSet.getAmount(),
	                rSet.getDEFAULT(),
	                rSet.getClientBankcode(),
	                rSet.getPaymentMode(),
	                rSet.getClientBankAccountNumber(),
	                rSet.getClientBankIfscCode(),
	                rSet.getPayment());
	        fileContent.append(data);
	    }
	    
	    String fileName = "PayoutDetails";

	    try {
	        File outputFile = File.createTempFile(fileName, ".txt");
	        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
	            outputStream.write(fileContent.toString().getBytes(StandardCharsets.UTF_8));
	        }
	        return Response.ok(outputFile)
	                .header("Content-Disposition", "attachment; filename="+fileName+".txt")
	                .build();
	    } catch (IOException e) {
	        // Handle the exception
	        return Response.serverError().build();
	    }

}

//	/**
//	 * Method to download payout details
//	 * 
//	 * @author Gowthaman M
//	 * @return
//	 */
//	@Override
//	public Response downloadPayoutDetails(PayoutReq payoutReq) {
//		List<PayoutDetailsEntity> response = payoutDetailsRepository.getPayoutDetails(payoutReq.getFromDate(), payoutReq.getToDate());
//		List<PayoutResponse> payoutResponse = PreparePayoutResponse(response);
//		String fileContent = "";
//		fileContent = "DEFAULT|DATE|LDCODE|AMOUNT|DEFAULT|CLIENT BANKCODE|PAYMENT MODE|CLIENT BANK ACCOUNT NUMBER|CLIENT BANK IFSC CODE|PAYMENT|\n";
//		String datas = "";
//        	for(PayoutResponse rSet : payoutResponse) {
////        		rSet.getDEFAULT() + "|" +
////        		rSet.getDate() + "|"+
////        		rSet.getLdCode() + "|"+
////        		rSet.getDEFAULT() + "|"+
////        		rSet.getClientBankcode() + "|"+
////        		rSet.getPaymentMode() + "|"+
////        		rSet.getClientBankAccountNumber() + "|"+
////        		rSet.getClientBankcode() + "|"+
////        		rSet.getPayment() + "|\n"
//        	}
//        	
//        	 InputStream stream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
//
//             return Response.ok(stream)
//                     .header("Content-Disposition", "attachment;filename=myfile.txt")
//                     .build();
//
////		return prepareResponse.prepareSuccessResponseObject(payoutResponse);
//	}

}
