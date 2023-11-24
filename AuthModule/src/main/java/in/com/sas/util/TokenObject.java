/**
 * 
 */
package in.com.sas.util;

import java.io.Serializable;

/**
 * @author Pradeep k
 *
 */
public class TokenObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String user;
	private long expiry;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public long getExpiry() {
		return expiry;
	}

	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}

//	@Override
//	public void writeData(ObjectDataOutput out) throws IOException {
//		out.writeString(user);
//		out.writeLong(expiry);
//	}
//
//	@Override
//	public void readData(ObjectDataInput in) throws IOException {
//		user = in.readString();
//		expiry = in.readLong();
//	}
}