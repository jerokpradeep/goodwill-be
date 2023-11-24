package in.codifi.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.resteasy.reactive.RestResponse;

import in.codifi.cache.model.ProductMasterModel;
import in.codifi.common.config.HazelcastConfig;
import in.codifi.common.entity.ProductMasterEntity;
import in.codifi.common.model.response.GenericResponse;
import in.codifi.common.reposirory.ProductMasterRepository;
import in.codifi.common.service.spec.ProductMasterServiceSpec;
import in.codifi.common.utility.AppConstants;
import in.codifi.common.utility.PrepareResponse;
import in.codifi.common.utility.StringUtil;
import io.quarkus.logging.Log;

@ApplicationScoped
public class ProductMasterService implements ProductMasterServiceSpec {

	@Inject
	PrepareResponse prepareResponse;

	@Inject
	ProductMasterRepository repository;

	/**
	 * Method to load product master into cache
	 * 
	 * @author DINESH KUMAR
	 *
	 * @return
	 */
	@Override
	public RestResponse<GenericResponse> loadProductMaster() {
		try {

			List<String> tags = repository.findDistinctByTag();
			if (StringUtil.isListNullOrEmpty(tags))
				return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
			for (String tag : tags) {
				List<ProductMasterEntity> productMasterEntities = repository.findAllByTag(tag);
				if (StringUtil.isListNullOrEmpty(productMasterEntities))
					return prepareResponse.prepareFailedResponse(AppConstants.NO_RECORDS_FOUND);
				List<ProductMasterModel> masterModels = new ArrayList<>();
				for (ProductMasterEntity masterEntity : productMasterEntities) {
					ProductMasterModel model = new ProductMasterModel();
					model.setKeyVariable(masterEntity.getKeyVariable());
					model.setValue(masterEntity.getValue());
					masterModels.add(model);
				}
				switch (tag) {
				case AppConstants.PRODUCT_TYPE:
					HazelcastConfig.getInstance().getProductTypes().clear();
					HazelcastConfig.getInstance().getProductTypes().put(AppConstants.PRODUCT_TYPE, masterModels);
					break;
				case AppConstants.ORDER_TYPE:
					HazelcastConfig.getInstance().getOrderTypes().clear();
					HazelcastConfig.getInstance().getOrderTypes().put(AppConstants.ORDER_TYPE, masterModels);
					break;
				case AppConstants.PRICE_TYPE:
					HazelcastConfig.getInstance().getPriceTypes().clear();
					HazelcastConfig.getInstance().getPriceTypes().put(AppConstants.PRICE_TYPE, masterModels);
					break;
				default:
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}
		return prepareResponse.prepareFailedResponse(AppConstants.FAILED_STATUS);
	}

}
