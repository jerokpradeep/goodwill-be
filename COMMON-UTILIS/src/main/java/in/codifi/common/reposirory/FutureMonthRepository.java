package in.codifi.common.reposirory;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.common.entity.FutureMonthEntity;

public interface FutureMonthRepository extends JpaRepository<FutureMonthEntity, Long> {

}
