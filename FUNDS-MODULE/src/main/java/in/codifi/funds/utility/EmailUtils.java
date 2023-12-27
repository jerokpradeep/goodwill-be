package in.codifi.funds.utility;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import in.codifi.funds.config.EmailProperties;
import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;

@ApplicationScoped
public class EmailUtils {

	@Inject
	Mailer mailer;

	@Inject
	EmailProperties props;
	@Inject
	AppUtil appUtils;

	/**
	 * 
	 * Method to send Email via ZOHO
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param htmlBody
	 * @param subject
	 * @param recpient
	 */
	public void sendEmailWithZoho(String htmlBody, String subject, List<String> recpient) {
		try {
			Mail mail = new Mail().setTo(recpient).setSubject(subject).setHtml(htmlBody);
			mailer.send(mail);
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
	}

	/**
	 * 
	 * Method to send email for payment failure
	 * 
	 * @author Dinesh Kumar
	 *
	 * @param message
	 */
	public void paymentFailureEmail(String message) {
		ExecutorService pool = Executors.newSingleThreadExecutor();
		pool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					List<String> recpient = Arrays.asList(props.getRecipientIds().split(","));
					for (String emailId : recpient) {
						if (appUtils.isEmail(emailId)) {
							String subject = "";
							String body = "<!DOCTYPE html><html><head><style>*{font-family:'Open Sans',"
									+ " Helvetica, Arial;color: #1e3465}table {margin-left:100px;font-family: arial, sans-serif;border-collapse:"
									+ " separate;}td, th {border: 1px solid #1e3465;text-align: left;padding: 8px;}"
									+ "th{background :#1e3465;color:white;}</style></head><body><div>"
									+ "<div  style='font-size:14px'><p>Hi  ,</p><p>" + message + " </p></div>"
									+ "<div><p align='left'>" + "<b>Regards,"
									+ "<br>Goodwill.</b></p></div></div></body></html>";

							subject = AppConstants.PAYMENT_SUBJECT;
							Mail mail = Mail.withHtml(emailId, subject, body);

							mailer.send(mail);
						} else {
							Log.error("userId :" + emailId + "is mailId is null");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.error(e.getMessage());
				}
			}

		});
		pool.shutdown();
	}
}
