package in.codifi.common.reposirory;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.common.entity.FutureEntity;

public interface FutureRepository extends JpaRepository<FutureEntity, Long> {

}
