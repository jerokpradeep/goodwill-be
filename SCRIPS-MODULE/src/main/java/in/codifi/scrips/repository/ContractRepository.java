package in.codifi.scrips.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.scrips.entity.primary.ContractEntity;

public interface ContractRepository extends JpaRepository<ContractEntity, Long> {

}
