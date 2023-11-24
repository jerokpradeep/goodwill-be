package in.codifi.client.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.client.entity.primary.PreferenceEntity;

public interface PreferenceRepository extends JpaRepository<PreferenceEntity, Long> {

	/**
	 * method to get User Preference
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	List<PreferenceEntity> findByUserIdAndSource(@Param("user_id") String userId, @Param("source") String source);

	/**
	 * method to get source
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Transactional
	List<PreferenceEntity> findByUserId(@Param("user_id") String userId);

	/**
	 * method to delete old preference
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Transactional
	Long deleteByUserIdAndSource(@Param("userId") String userId, @Param("source") String source);

	/**
	 * method to update user preference
	 * 
	 * @author GOWTHAMAN M
	 * @param sourceWeb
	 * @return
	 */
	@Modifying
	@Query(value = "update TBL_USER_PREFERENCES set value =:value where userId =:user_id and tag=:tag and source =:source and id > 0")
	int updatePreferenceDetails(@Param("tag") String tag, @Param("value") String value, @Param("user_id") String userId,
			@Param("source") String sourceWeb);

	/**
	 * method to get User Preference
	 * 
	 * @author GOWTHAMAN M
	 * @return
	 */
	@Transactional
	@Query(value = " select distinct userId from TBL_USER_PREFERENCES ")
	List<String> getDistinctUserId();

	/**
	 * method to update user preference
	 * 
	 * @author SOWMIYA
	 * @param sourceWeb
	 * @return
	 */
	@Modifying
	@Query(value = "update TBL_USER_PREFERENCES set value =:value where userId =:user_id and tag=:tag and source =:source and id > 0")
	int updateMobilePreferenceDetails(@Param("tag") String tag, @Param("value") String value,
			@Param("user_id") String userId, @Param("source") String sourceWeb);

	/**
	 * method to update user preference
	 * 
	 * @author SOWMIYA
	 * @return
	 */
	@Modifying
	@Query(value = "update TBL_USER_PREFERENCES set value =:value where userId =:user_id and tag=:tag")
	int updatePreferenceDetails(@Param("tag") String tag, @Param("value") String value,
			@Param("user_id") String userId);

}
