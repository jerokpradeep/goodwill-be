package in.codifi.common.reposirory;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.common.entity.VersionEntity;

public interface VersionRepository extends JpaRepository<VersionEntity, Long> {

}
