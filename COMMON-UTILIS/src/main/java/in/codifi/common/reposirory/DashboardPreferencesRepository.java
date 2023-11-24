package in.codifi.common.reposirory;

import org.springframework.data.jpa.repository.JpaRepository;

import in.codifi.common.entity.DashboardPreferencesEntity;

public interface DashboardPreferencesRepository extends JpaRepository<DashboardPreferencesEntity, Long> {

}
