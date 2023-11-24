package in.codifi.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import com.esotericsoftware.minlog.Log;

import in.codifi.common.entity.EquityScanEntity;
import in.codifi.common.entity.FutureScanEntity;
import in.codifi.common.model.request.ScanMasterModel;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.reposirory.EquityScanRepository;
import in.codifi.common.reposirory.FutureScanRepository;
import in.codifi.common.service.spec.ScanServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;

@ApplicationScoped
public class ScanService implements ScanServiceSpec {

	@Inject
	PrepareResponse prepareResponse;
	@Inject
	FutureScanRepository futureScanRepo;
	@Inject
	EquityScanRepository equityScanRepo;

	/**
	 * method to get equity scan details by scan master model
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	public RestResponse<GenericResponse> findEquityScan() {
		try {
			List<ScanMasterModel> scanMasterList = new ArrayList<ScanMasterModel>();
			List<EquityScanEntity> equityScan = equityScanRepo.findAll();
			if (StringUtil.isListNullOrEmpty(equityScan))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			for (EquityScanEntity entity : equityScan) {
				ScanMasterModel master = new ScanMasterModel();
				master.setScanId(entity.getScannerId());
				master.setScanName(entity.getScannerName());
				master.setVolume(entity.getVolume());
				scanMasterList.add(master);
			}
			return prepareResponse.prepareSuccessResponseObject(scanMasterList);

		} catch (Exception e) {
			Log.error(e.getMessage());
			e.printStackTrace();

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);

	}

	/**
	 * 
	 * Method to get EQ Scanner list
	 * 
	 * @author Dinesh Kumar
	 *
	 * @return
	 */
	public List<ScanMasterModel> getEqScanner() {
		List<ScanMasterModel> scanMasterList = new ArrayList<ScanMasterModel>();
		try {

			List<EquityScanEntity> equityScan = equityScanRepo.findAll();
			if (StringUtil.isListNotNullOrEmpty(equityScan)) {
				for (EquityScanEntity entity : equityScan) {
					ScanMasterModel master = new ScanMasterModel();
					master.setScanId(entity.getScannerId());
					master.setScanName(entity.getScannerName());
					master.setVolume(entity.getVolume());
					scanMasterList.add(master);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return scanMasterList;
	}

	/**
	 * method to get equtiy scan from database
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> findEquityScanAll() {
		try {
			List<EquityScanEntity> scanMasterList = equityScanRepo.findAll();
			if (StringUtil.isListNullOrEmpty(scanMasterList))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			return prepareResponse.prepareSuccessResponseObject(scanMasterList);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());

		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get Future Scan details by scan master model
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> findFutureScan() {
		try {
			List<ScanMasterModel> scanMasterList = new ArrayList<ScanMasterModel>();
			List<FutureScanEntity> futureScan = futureScanRepo.findAll();
			if (StringUtil.isListNullOrEmpty(futureScan))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			for (FutureScanEntity entity : futureScan) {
				ScanMasterModel master = new ScanMasterModel();
				master.setScanId(entity.getScanId());
				master.setScanName(entity.getScanName());
				scanMasterList.add(master);
			}
			return prepareResponse.prepareSuccessResponseObject(scanMasterList);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

	/**
	 * method to get Future Scan from database
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> findFutureScanAll() {
		try {
			List<FutureScanEntity> scanMasterList = futureScanRepo.findAll();
			if (StringUtil.isListNullOrEmpty(scanMasterList))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			return prepareResponse.prepareSuccessResponseObject(scanMasterList);

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
