package in.codifi.common.reposirory;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.common.entity.EquityScanEntity;

public interface EquityScanRepository extends JpaRepository<EquityScanEntity, Long> {

}
