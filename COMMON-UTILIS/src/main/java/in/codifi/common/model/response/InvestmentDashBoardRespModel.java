package in.codifi.common.model.response;

import java.util.List;

import in.codifi.common.entity.EQSectorEntity;
import in.codifi.common.entity.IndicesEntity;
import in.codifi.common.entity.MarketingEntity;
import in.codifi.common.model.request.ScanMasterModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvestmentDashBoardRespModel {

	private String status;
	private String message;
	private List<DashBoardPreferenceModel> preference;
	private List<IndicesEntity> indicesData;
	private List<MarketingEntity> mCardData;
	private List<EQSectorEntity> topSectorData;
	private List<ScanMasterModel> scannersData;

}
