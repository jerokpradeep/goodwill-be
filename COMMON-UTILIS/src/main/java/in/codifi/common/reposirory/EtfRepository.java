package in.codifi.common.reposirory;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.common.entity.EtfEntity;

public interface EtfRepository extends JpaRepository<EtfEntity, Long> {

}
