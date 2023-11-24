package in.codifi.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.codifi.admin.entity.VendorAppEntity;

public interface VendorRepository extends JpaRepository<VendorAppEntity, Long> {

	/**
	 * Method to get api from Vendor
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Query(value = "SELECT a FROM TBL_VENDOR_APP a WHERE a.client_id = :client_id")
	List<VendorAppEntity> getByClientId(@Param("client_id") String client_id);

	/**
	 * Method to Update authorization_status
	 * 
	 * @author LOKESH
	 * @return
	 */
	@Modifying
	@Query(value = "UPDATE TBL_VENDOR_APP set authorization_status = :authorization_status  where api_key = :api_key")
	int updateAuthorization(@Param("authorization_status") int authorization_status, @Param("api_key") String api_key);
}
