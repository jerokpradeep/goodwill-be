package in.codifi.sso.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.sso.auth.entity.primary.VendorAppEntity;

public interface VendorRepository extends JpaRepository<VendorAppEntity, Long> {

	List<VendorAppEntity> findAllByClientIdAndActiveStatus(String clientId,int activeStatus);

	VendorAppEntity findByIdAndClientId(long appId, String userId);
}
