package in.codifi.cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import in.codifi.admin.repository.ContractEntityManager;

@ApplicationScoped
public class CacheService {

	@Inject
	ContractEntityManager entityManager;

	public void loadTokenForPosition() {
		entityManager.loadTokenForPosition();

	}

	public void loadTokenForHoldings() {
		entityManager.loadTokenForHoldings();
	}
}
