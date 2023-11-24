package in.codifi.funds.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.funds.entity.primary.PaymentRefEntity;

public interface PaymentRefRepository extends JpaRepository<PaymentRefEntity, Long> {

	@Modifying
	@Query("UPDATE TBL_PAYMENT_REF SET  payment_status = :paymentStatus, updated_on = CURRENT_TIMESTAMP, updated_by = :userId ,bo_update = 1, voucher_no = :voucherNo WHERE user_id = :userId"
			+ " and order_id = :orderId")
	int updateboStatus(@Param("userId") String userId, @Param("orderId") String orderId,
			@Param("paymentStatus") String paymentStatus, @Param("voucherNo") String voucherNo);

	@Modifying
	@Query("UPDATE TBL_PAYMENT_REF SET  payment_status = :paymentStatus, updated_on = CURRENT_TIMESTAMP, updated_by = :userId  WHERE user_id = :userId"
			+ " and order_id = :orderId")
	int updatePaymentDetails(@Param("userId") String userId, @Param("orderId") String orderId,
			@Param("paymentStatus") String paymentStatus);

	PaymentRefEntity findByOrderId(@Param("order_id") String orderId);

	@Transactional
	@Query(value = " select a from TBL_PAYMENT_REF a WHERE order_id = :orderId")
	PaymentRefEntity getByOrderId(@Param("orderId") String orderId);
}
