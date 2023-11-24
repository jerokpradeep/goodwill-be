package in.codifi.sso.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.sso.auth.entity.primary.VendorAppSubscriptionEntity;

public interface VendorSubscriptionRepository extends JpaRepository<VendorAppSubscriptionEntity, Long> {

}
