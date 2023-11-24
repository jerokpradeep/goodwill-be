package in.codifi.notify.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.notify.entity.ConsentEntity;

public interface ConsentRepository extends JpaRepository<ConsentEntity, Long> {

}
