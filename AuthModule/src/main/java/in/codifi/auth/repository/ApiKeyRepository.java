package in.codifi.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.auth.entity.primary.ApiKeyEntity;

public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {

}
