package in.codifi.funds.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.funds.entity.primary.PaymentLogsEntity;

public interface PaymentLogsRepository extends JpaRepository<PaymentLogsEntity, Long> {

}
