package in.codifi.scrips.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import in.codifi.scrips.entity.primary.StockReturnEntity;

public interface StockReturnRepository extends JpaRepository<StockReturnEntity, Long> {

	List<StockReturnEntity> findByExchAndToken(@Param("exch") String exch, @Param("token") String token);

}
