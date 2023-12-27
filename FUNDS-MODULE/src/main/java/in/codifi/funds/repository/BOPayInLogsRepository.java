package in.codifi.funds.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.funds.entity.primary.BoPayInLogsEntity;

public interface BOPayInLogsRepository extends JpaRepository<BoPayInLogsEntity, Long> {

}
