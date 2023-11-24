package in.codifi.cache;

import java.util.Map;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CacheController {

	private static Map<String, String> userRec = new TreeMap<String, String>();

	public static Map<String, String> getUserRec() {
		return userRec;
	}

	public static void setUserRec(Map<String, String> userRec) {
		CacheController.userRec = userRec;
	}
	
	
}
