package in.codifi.basket.repository;

import java.util.List;

import javax.enterprise.context.control.ActivateRequestContext;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.basket.entity.primary.BasketNameEntity;

public interface BasketOrderRepository extends JpaRepository<BasketNameEntity, Long> {

	/**
	 * method to delete Basket Order
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	long deleteByBasketId(@Param("basketId") long basketId);

	@ActivateRequestContext
	List<BasketNameEntity> findAllByUserId(@Param("user_id") String userId);

	BasketNameEntity findById(@Param("basketId") long basketId);

	/**
	 * method to update Basket name
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Modifying
	@Query(value = " update TBL_BASKET_ORDER set BASKET_NAME = :basket_name, UPDATED_BY = :updated_by WHERE USER_ID = :user_id and BASKET_ID = :basketId ")
	int updateBasketName(@Param("basket_name") String basketName, @Param("updated_by") String updatedBy,
			@Param("user_id") String userId, @Param("basketId") long basketId);

	/**
	 * Method to find Basket Name is exist or not
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Transactional
	@Query(value = " select a from TBL_BASKET_ORDER a WHERE BASKET_NAME = :basket_name and USER_ID = :user_id  ")
	BasketNameEntity findByBasketName(@Param("basket_name") String basketName, @Param("user_id") String userId);

	/**
	 * method to get userId
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Transactional
	@Query(value = " select distinct(userId) from TBL_BASKET_ORDER ")
	List<String> getUserId();

	/**
	 * method to find basket id is exist or not
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	BasketNameEntity findByBasketId(@Param("basketId") long basketId);

	/**
	 * Method to find Basket Name is exist or not
	 * 
	 * @author Gowthaman M
	 * @return
	 */
	@Transactional
	@Query(value = " select a from TBL_BASKET_ORDER a WHERE BASKET_ID = :basketId and USER_ID = :user_id  ")
	BasketNameEntity findValiedBasketId(@Param("basketId") long basketId, @Param("user_id") String userId);

	/**
	 * method to update execution status
	 * 
	 * @author DINESH KUMAR
	 * @return
	 */
	@Modifying
	@Query(value = " update TBL_BASKET_ORDER set IS_EXECUTED = 1, UPDATED_BY = :updated_by WHERE USER_ID = :user_id and BASKET_ID = :basketId ")
	int updateExecutionStatus(@Param("exeStatus") String exeStatus, @Param("updated_by") String updatedBy,
			@Param("user_id") String userId, @Param("basketId") long basketId);

}
