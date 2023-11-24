package in.codifi.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.api.entity.primary.PredefinedMwEntity;

public interface PredefinedMwRepo extends JpaRepository<PredefinedMwEntity, Long> {

	PredefinedMwEntity findAllByMwNameAndMwId(String mwName, int mwId);
}
