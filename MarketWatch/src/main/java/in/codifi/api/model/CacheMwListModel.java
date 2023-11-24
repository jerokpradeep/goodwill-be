package in.codifi.api.model;

import java.util.List;

public class CacheMwListModel {

	private String mwName;
	private int mwId;
	private List<CacheMwDetailsModel> scrips;

	public String getMwName() {
		return mwName;
	}

	public void setMwName(String mwName) {
		this.mwName = mwName;
	}

	public int getMwId() {
		return mwId;
	}

	public void setMwId(int mwId) {
		this.mwId = mwId;
	}

	public List<CacheMwDetailsModel> getScrips() {
		return scrips;
	}

	public void setScrips(List<CacheMwDetailsModel> scrips) {
		this.scrips = scrips;
	}

}
