package in.codifi.admin.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.admin.entity.PayoutDetailsEntity;

public interface PayoutDetailsRepository extends JpaRepository<PayoutDetailsEntity, Long> {

	@Transactional
	@Query(value = " select a from TBL_PAYOUT_DETAILS a WHERE a.activeStatus = 1 and Date(a.createdOn) BETWEEN :fromDate and :toDate ")
	List<PayoutDetailsEntity> getPayoutDetails(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

}
