package in.codifi.cache.model;

import java.io.Serializable;
import java.util.List;


public class ClientDetailsModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	private String actId;
	private String clientName;
	private String actStatus;
	private String createdDate;
	private String createdTime;
	private String mobNo;
	private String email;
	private String pan;
	private String address;
	private String officeAddress;
	private String city;
	private String state;
	private List<String> exchange;
	private List<String> orders;
	private String branchId;
	private String brokerName;
	private List<Bankdetails> bankdetails;
	private List<String> products;
	private List<String> productTypes;
	private List<String> orderTypes;
	private List<String> priceTypes;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getActStatus() {
		return actStatus;
	}

	public void setActStatus(String actStatus) {
		this.actStatus = actStatus;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getMobNo() {
		return mobNo;
	}

	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<String> getExchange() {
		return exchange;
	}

	public void setExchange(List<String> exchange) {
		this.exchange = exchange;
	}

	public List<String> getOrders() {
		return orders;
	}

	public void setOrders(List<String> orders) {
		this.orders = orders;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public List<String> getProducts() {
		return products;
	}

	public void setProducts(List<String> products) {
		this.products = products;
	}

	public List<String> getProductTypes() {
		return productTypes;
	}

	public void setProductTypes(List<String> productTypes) {
		this.productTypes = productTypes;
	}

	public List<String> getOrderTypes() {
		return orderTypes;
	}

	public void setOrderTypes(List<String> orderTypes) {
		this.orderTypes = orderTypes;
	}

	public List<String> getPriceTypes() {
		return priceTypes;
	}

	public void setPriceTypes(List<String> priceTypes) {
		this.priceTypes = priceTypes;
	}

	public List<Bankdetails> getBankdetails() {
		return bankdetails;
	}

	public void setBankdetails(List<Bankdetails> bankdetails) {
		this.bankdetails = bankdetails;
	}

}
