package in.codifi.funds.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.funds.entity.primary.BOPaymentLogEntity;

public interface BOPaymentLogRepository extends JpaRepository<BOPaymentLogEntity, Long> {

	@Modifying
	@Query("UPDATE TBL_BO_PAYOUT_LOG SET active_status = 0, updated_on = CURRENT_TIMESTAMP, updated_by = :userId WHERE user_id = :userId")
	void updateBOPaymentStatus(@Param("userId") String userId);

//	@Query("SELECT e FROM TBL_BO_PAYOUT_LOG e WHERE e.userId = :userId ORDER BY e.createdOn DESC")
//	List<BOPaymentLogEntity> findByUserIdWithLimit(@Param("userId") String userId, int limit);
	
	
	List<BOPaymentLogEntity> findAllByUserIdOrderByCreatedOnDesc(String userId, Pageable limit);
	
}
