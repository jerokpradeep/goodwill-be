package in.codifi.auth.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.auth.entity.primary.TwoFAPreferenceEntity;

public interface TwoFAPreferenceRepository extends JpaRepository<TwoFAPreferenceEntity, Long> {

	TwoFAPreferenceEntity findByUserId(@Param("user_id") String userId);

	TwoFAPreferenceEntity findByUserIdAndActiveStatus(@Param("user_id") String userId,
			@Param("active_status") int activeStatus);

	@Transactional
	@Query(value = " select distinct userId from TBL_TWO_FA_PREFERENCE")
	List<String> getUserIds();
}
