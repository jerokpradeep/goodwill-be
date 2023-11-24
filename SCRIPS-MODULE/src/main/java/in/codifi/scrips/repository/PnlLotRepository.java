package in.codifi.scrips.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.scrips.entity.primary.PnlLotEntity;

public interface PnlLotRepository extends JpaRepository<PnlLotEntity, Long> {

}
