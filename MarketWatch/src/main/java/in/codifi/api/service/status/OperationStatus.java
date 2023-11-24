/**
 * 
 */
package in.codifi.api.service.status;

/**
 * @author mohup
 *
 */
public enum OperationStatus {

	SUCCESS("SUCCESS: %s-%d: %s completed successfully.", 200), ERROR("ERROR: %s: %s did not complete.", 500);

	private String moduleName = "MW-";

	private String message;
	private int statusId;

	private OperationStatus(String message, int statusId) {
		this.message = message;
		this.statusId = statusId;
	}

	public String get(String info) {
		return String.format(message, moduleName, statusId, info);
	}

	public int getStatusId() {
		return this.statusId;
	}
}
