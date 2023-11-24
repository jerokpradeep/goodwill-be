package in.codifi.common.reposirory;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.common.entity.MarketingEntity;

public interface MarketingRepository extends JpaRepository<MarketingEntity, Long> {

}
