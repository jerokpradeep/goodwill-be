package in.codifi.feedback.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TBL_FEEDBACK_DATA")
public class FeedbackEntity extends CommonEntity implements Serializable {

	/**
	 * @author LOKESH
	 */

	private static final long serialVersionUID = 1L;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "date")
	private String date;

	@Column(name = "comments")
	private String comments;

	@Column(name = "ratings")
	private int ratings;

	@Column(name = "source")
	private String source;

	@Column(name = "version")
	private String version;

}