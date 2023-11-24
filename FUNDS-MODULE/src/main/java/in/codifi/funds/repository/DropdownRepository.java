package in.codifi.funds.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.codifi.funds.entity.primary.DropdownEntity;

public interface DropdownRepository extends JpaRepository<DropdownEntity, Long> {

	@Query("SELECT e.value FROM TBL_DROPDOWNS e WHERE e.tag = 'Funds' AND e.key = 'Payout Reasons'")
	List<String> getPayoutReasons();
}
