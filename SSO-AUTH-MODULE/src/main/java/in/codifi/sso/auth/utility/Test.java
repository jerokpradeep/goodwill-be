package in.codifi.sso.auth.utility;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Test {
	public static void main(String args[]) {

		String apiKey = CommonUtils.generateRandomAlpaString(15);
		System.out.println(apiKey);
		String apiSecret = CommonUtils.generateRandomAlpaString(100);
		System.out.println(apiSecret);
//		long timestamp = 1694226274L;
//
//		Date date = new Date(timestamp * 1000L);
//
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//		String formattedDate = sdf.format(date);
//
//		System.out.println(formattedDate);
//		long currentTimestamp = Instant.now().getEpochSecond();
//		System.out.println(currentTimestamp);

	}

}
