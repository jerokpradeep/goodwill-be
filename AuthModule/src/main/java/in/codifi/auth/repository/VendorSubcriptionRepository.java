package in.codifi.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import in.codifi.auth.entity.primary.VendorSubcriptionEntity;

public interface VendorSubcriptionRepository extends JpaRepository<VendorSubcriptionEntity, Long> {

	List<VendorSubcriptionEntity> findAllByUserIdAndAppIdAndActiveStatus(@Param("userId") String userId,
			@Param("appId") long appId, @Param("activeStatus") int activeStatus);

}
