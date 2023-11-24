package in.codifi.common.ws.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.codifi.cache.model.AnalysisRespModel;
import in.codifi.common.config.HazelcastConfig;
import in.codifi.common.config.RestServiceProperties;
import in.codifi.common.model.request.RestAccessLogModel;
import in.codifi.common.repo.entitymanager.AccessLogManager;
import in.codifi.common.repo.entitymanager.ContractEntityManger;
import in.codifi.common.reposirory.AnnoucementsDataRepository;
import in.codifi.common.reposirory.NetValueRepository;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import in.codifi.common.ws.model.AnalysisRestResponseModel;
import in.codifi.common.ws.model.WIResponse;
import in.codifi.common.ws.model.WIRestResultset;
import in.codifi.common.ws.model.WIResultResponse;
import io.quarkus.logging.Log;

@ApplicationScoped
public class AnalysisRestService {

	@Inject
	RestServiceProperties props;
	@Inject
	PrepareResponse prepareResponse;
	@Inject
	AccessLogManager accessLogManager;
	@Inject
	ContractEntityManger entityManager;
	@Inject
	NetValueRepository netValueRepo;
	@Inject
	AnnoucementsDataRepository annoucementsDataRepository;

	/**
	 * Method to insert rest service access logs
	 * 
	 * @author Gowthaman M
	 *
	 * @param accessLogModel
	 */
	public void insertRestAccessLogs(RestAccessLogModel accessLogModel) {

		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					accessLogManager.insertRestAccessLog(accessLogModel);
				} catch (Exception e) {
					Log.error(e);
				}
			}
		});
		pool.shutdown();
	}

	/**
	 * Method to bind World indices Data
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	public WIResponse bindWorldIndicesData(List<WIRestResultset> resultset) {
		DateFormat originalFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		DateFormat originalFormats = new SimpleDateFormat("dd-MMM-yyyy");
		DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		WIResponse response = new WIResponse();
		List<WIResultResponse> resultList = new ArrayList<>();
		try {
			for (WIRestResultset rSet : resultset) {
				WIResultResponse result = new WIResultResponse();
				result.setToken(null);
				if (rSet.getDate().length() > 12) {
					date = originalFormat.parse(rSet.getDate());
				} else {
					date = originalFormats.parse(rSet.getDate());
				}
				String formattedDate = targetFormat.format(date);
				result.setDate(formattedDate);
				result.setHighlight("LTP");
				result.setSymbol(rSet.getIndexName());
				result.setLtp(rSet.getClosePrice());
				result.setClosePerChg(rSet.getPercentageChange());
				result.setDirection(null);
				result.setIsin(null);
				result.setPClose(rSet.getPreviousClosePrice());
				result.setExchange(null);
				result.setIndexID(rSet.getIndexID());
				result.setIndexName(rSet.getIndexName());
				resultList.add(result);
			}
			response.setEquityresult(resultList);
		} catch (Exception e) {
			Log.error(e);
		}

		return response;
	}

	/**
	 * method to get analysisData from server
	 * 
	 * @author SOWMIYA
	 * @param baseUrl
	 * @return
	 */
	public List<AnalysisRespModel> getFundamentalAnalysisData(String topGainerUrl) {
		ObjectMapper mapper = new ObjectMapper();
		List<AnalysisRespModel> response = new ArrayList<>();
		String output = "";
		try {
			URL url = new URL(props.getAnalysisBaseUrl() + topGainerUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader((conn.getInputStream())));
			output = reader.readLine();
			if (StringUtil.isNotNullOrEmpty(output)) {
				List<AnalysisRestResponseModel> analysisRestResponseModels = mapper.readValue(output,
						new TypeReference<List<AnalysisRestResponseModel>>() {
						});
				response = bindResp(analysisRestResponseModels);
			}
		} catch (Exception e) {
			Log.error(e);
		}
		return response;
	}

	/**
	 * Method to bind response
	 * 
	 * @author Dinesh Kumar
	 * @param responseModels
	 * @return
	 */
	private List<AnalysisRespModel> bindResp(List<AnalysisRestResponseModel> responseModels) {
		List<AnalysisRespModel> response = new ArrayList<>();
		try {
			if (HazelcastConfig.getInstance().getNseTokenCache().isEmpty()) {
				entityManager.loadNSEData();
			}
			for (AnalysisRestResponseModel model : responseModels) {
				AnalysisRespModel analysisRespModel = new AnalysisRespModel();
				String symbol = model.getSymbol().trim();
				analysisRespModel.setSymbol(symbol);
				analysisRespModel.setDateval(model.getDateval());
				analysisRespModel.setClosePerChg(model.getClosePerChg());
				analysisRespModel.setDirection(model.getDirection());
				analysisRespModel.setHighlight(model.getHighlight());
				analysisRespModel.setIsin(model.getIsin());
				analysisRespModel.setLtp(model.getLtp());
				analysisRespModel.setPdc(model.getPdc());
				String token = "";
				if (HazelcastConfig.getInstance().getNseTokenCache().get(symbol) != null) {
					token = HazelcastConfig.getInstance().getNseTokenCache().get(model.getSymbol());
				}
				analysisRespModel.setToken(token);
				analysisRespModel.setExch("NSE");
				response.add(analysisRespModel);

			}
		} catch (Exception e) {
			Log.error(e);
		}
		return response;
	}

}
