package in.codifi.auth.utility;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

@ApplicationScoped
public class EmailUtils {

	@Inject
	Mailer mailer;

	/**
	 * method to send email otp
	 * 
	 * @author SowmiyaThangaraj
	 * @param userName
	 * @param otp
	 * @param emailId
	 */
	public void sendEmail(String otp, String emailId) {
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String bodyMsg = "<p>Dear User,</p><p>" + otp
							+ " is your verification code as requested online, this code is valid for next 10 minutes.</p><br>"
							+ "Thanks and Regards,<br>Go-Pocket.";

					String subject = "Go-Pocket Login OTP";
					Mail mail = Mail.withHtml(emailId, subject, bodyMsg);
					mailer.send(mail);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					pool.shutdown();
				}
			}
		});
	}

}
