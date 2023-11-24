package in.codifi.sso.auth.utility;

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
	 * method to send api key through mail
	 * 
	 * @author SowmiyaThangaraj
	 */
	public void sendEmailAPIKey(String userName, String msg, String apiKey, String emailId) {
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String bodyMsg = "<!DOCTYPE html><html><head><style>*{font-family:'Open Sans',"
							+ " Helvetica, Arial;color: #1e3465}table {margin-left:100px;font-family: arial, sans-serif;border-collapse:"
							+ " separate;}td, th {border: 1px solid #1e3465;text-align: left;padding: 8px;}"
							+ "th{background :#1e3465;color:white;}</style></head><body><div>"
							+ "<div  style='font-size:14px'><p>Dear " + userName + " ,</p>" + "<p>" + msg
							+ "Please note this API key for programmatic trade through Go-Pocket. </p>API KEY :  <strong><u>"
							+ apiKey + "</u></strong>" + "<br></div>"
							+ "<div><br><strong>Note : </strong> <p>Please don't share the API key with others as this may lead to fraudulent activity in your account.</p> </div>"
							+ "<div><p align='left'>" + "<b>Regards,"
							+ "<br>Alice Blue Team.</b></p></div></div></body></html>";

					String subject = "API key for Go-Pocket";
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
