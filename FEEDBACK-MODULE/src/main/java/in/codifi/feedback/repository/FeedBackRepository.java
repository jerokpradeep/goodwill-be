package in.codifi.feedback.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import org.jboss.resteasy.reactive.DateFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.feedback.entity.FeedbackEntity;

public interface FeedBackRepository extends JpaRepository<FeedbackEntity, Long> {

	@Transactional
	@Query(value = "SELECT a FROM TBL_FEEDBACK_DATA a WHERE DATE(a.createdOn) BETWEEN :date1 AND :date2")
	List<FeedbackEntity> getFeedBackMessageWithDate(@Param("date1") @DateFormat(pattern = "yyyy-MM-dd") Date fromDate,
			@Param("date2") @DateFormat(pattern = "yyyy-MM-dd") Date toDate);
}
