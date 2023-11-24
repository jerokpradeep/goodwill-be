package in.codifi.auth.repository;

import javax.enterprise.context.control.ActivateRequestContext;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.auth.entity.primary.TotpDetailsEntity;

public interface TotpRepository extends JpaRepository<TotpDetailsEntity, Long> {

	@ActivateRequestContext
	TotpDetailsEntity findByUserId(@Param("user_id") String userId);

	@Modifying
	@Query("UPDATE TBL_TOTP_DETAILS SET active_status = 1, updated_on = CURRENT_TIMESTAMP, updated_by = :updatedBy WHERE user_id = :userId")
	int enableTotp(@Param("userId") String userId, @Param("updatedBy") String userName);

}
