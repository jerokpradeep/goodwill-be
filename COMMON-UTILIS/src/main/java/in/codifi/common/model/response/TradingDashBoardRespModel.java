package in.codifi.common.model.response;

import java.util.ArrayList;
import java.util.List;

import in.codifi.common.entity.FiiIndexEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TradingDashBoardRespModel {

	private String status;
	private String message;
	private List<DashBoardPreferenceModel> preference;
	private List<Object> futuresData = new ArrayList<>();
	private List<Object> fnoScannersData = new ArrayList<>();
	private List<SectorHeatMapModel> heatMapData;
	private List<FiiIndexEntity> fiiIndexData = new ArrayList<>();
}
