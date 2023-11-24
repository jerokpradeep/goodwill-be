package in.codifi.common.reposirory;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.common.entity.FutureScanEntity;

public interface FutureScanRepository extends JpaRepository<FutureScanEntity, Long> {

}
