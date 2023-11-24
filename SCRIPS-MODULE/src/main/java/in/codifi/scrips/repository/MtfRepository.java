package in.codifi.scrips.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.scrips.entity.primary.MTFEntity;

public interface MtfRepository extends JpaRepository<MTFEntity, Integer> {

}
