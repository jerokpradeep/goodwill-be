package in.codifi.api.model;

import java.util.List;

public class MwRequestModel {
	private int mwId;
	private String userId;
	private String mwName;
	private List<MwScripModel> scripData;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getMwId() {
		return mwId;
	}

	public void setMwId(int mwId) {
		this.mwId = mwId;
	}

	public String getMwName() {
		return mwName;
	}

	public void setMwName(String mwName) {
		this.mwName = mwName;
	}

	public List<MwScripModel> getScripData() {
		return scripData;
	}

	public void setScripData(List<MwScripModel> scripData) {
		this.scripData = scripData;
	}

}
