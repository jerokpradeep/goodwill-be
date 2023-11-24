package in.codifi.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import in.codifi.auth.entity.primary.VendorEntity;

public interface VendorRepository extends JpaRepository<VendorEntity, Long> {

	List<VendorEntity> findAllByApiKey(@Param("apiKey") String apiKey);

	List<VendorEntity> findAllByApiKeyAndAuthorizationStatusAndActiveStatus(String apiKey, int authorizationStatus,
			int activeStatus);

}
