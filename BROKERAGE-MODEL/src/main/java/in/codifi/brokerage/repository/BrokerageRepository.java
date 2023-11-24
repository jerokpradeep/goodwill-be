package in.codifi.brokerage.repository;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import in.codifi.brokerage.config.HazelcastConfig;
import in.codifi.brokerage.entity.BrokerageEquityEntity;
import in.codifi.brokerage.model.request.BrokerageReqModel;
import in.codifi.brokerage.utility.StringUtil;

@ApplicationScoped
public class BrokerageRepository {
	@Inject
	EntityManager entityManager;

	@SuppressWarnings("unchecked")
	public BrokerageEquityEntity values(BrokerageReqModel brokerageReqModel) {
		List<Object[]> result = null;
		BrokerageEquityEntity brokerage = new BrokerageEquityEntity();
		StringBuffer queryString = new StringBuffer();
		try {
			String subQuery = "SELECT plan,segment,base,instrument_type,description,type,lot,turnover,compare_cost,securities_transaction_tax_buy,securities_transaction_tax_sell,transaction_charges_nse_buy,transaction_charges_nse_sell,"
					+ "sebi_charges_buy,sebi_charges_sell,stamp_charges_buy,stamp_charges_sell,clearing_member,ipft,gst FROM tbl_brokerage";
			queryString.append(subQuery);
			List<String> conditions = getConditon(brokerageReqModel);
			if (!conditions.isEmpty()) {
				queryString.append(" where " + StringUtil.convertConditionsListToString(conditions));
			}
			Query query = entityManager.createNativeQuery(queryString.toString());
			result = query.getResultList();
			for (Object[] values : result) {
				brokerage = new BrokerageEquityEntity();
				if (values[0] != null) {
					brokerage.setPlan(values[0].toString());
				} else if (values[1] != null) {
					brokerage.setSegment(values[1].toString());
				} else if (values[2] != null) {
					brokerage.setBase(values[2].toString());
				} else if (values[3] != null) {
					brokerage.setInstrumentType(values[3].toString());
				} else if (values[4] != null) {
					brokerage.setDescription(values[4].toString());
				} else if (values[5] != null) {
					brokerage.setType(values[5].toString());
				} else if (values[6] != null) {
					brokerage.setLot(values[6].toString());
				} else if (values[7] != null) {
					brokerage.setTurnOver(values[7].toString());
				} else if (values[8] != null) {
					brokerage.setCompareCost(values[8].toString());
				} else if (values[9] != null) {
					brokerage.setSecuritiesTransactionTaxBuy(values[9].toString());
				} else if (values[10] != null) {
					brokerage.setSecuritiesTransactionTaxSell(values[10].toString());
				} else if (values[11] != null) {
					brokerage.setTransactionChargesNseBuy(values[11].toString());
				} else if (values[12] != null) {
					brokerage.setTransactionChargesNseSell(values[12].toString());
				} else if (values[13] != null) {
					brokerage.setSebiChargesBuy(values[13].toString());
				} else if (values[14] != null) {
					brokerage.setSebiChargesSell(values[14].toString());
				} else if (values[15] != null) {
					brokerage.setStampChargesBuy(values[15].toString());
				} else if (values[16] != null) {
					brokerage.setStampChargesSell(values[16].toString());
				} else if (values[17] != null) {
					brokerage.setClearing_member(values[17].toString());
				} else if (values[18] != null) {
					brokerage.setIpft(values[18].toString());
				} else if (values[19] != null) {
					brokerage.setGst(values[19].toString());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return brokerage;
	}

	/**
	 * Method to
	 *
	 * @author Admin
	 *
	 * @param brokerageReqModel
	 * @return
	 */
	private List<String> getConditon(BrokerageReqModel brokerageReqModel) {
		List<String> conditions = new ArrayList<String>();
		if (brokerageReqModel != null) {
			if (StringUtil.isNotNullOrEmpty(brokerageReqModel.getPlan())) {
				conditions.add(" plan = '" + brokerageReqModel.getPlan() + "' ");
			}
			if (StringUtil.isNotNullOrEmpty(brokerageReqModel.getSegment())) {
				conditions.add(" segment = '" + brokerageReqModel.getSegment() + "' ");
			}
			if (StringUtil.isNotNullOrEmpty(brokerageReqModel.getInstrumentType())) {
				conditions.add(" instrument_type = '" + brokerageReqModel.getInstrumentType() + "' ");
			}
			if (StringUtil.isNotNullOrEmpty(brokerageReqModel.getType())) {
				conditions.add(" type = '" + brokerageReqModel.getType() + "' ");
			}
		}
		return conditions;
	}

	/**
	 * 
	 * Method to load brokerage
	 *
	 * @author SOWMIYA
	 *
	 */
	@SuppressWarnings("unchecked")
	public void loadBrokerage() {
		List<Object[]> result = null;
		try {
			Query query = entityManager.createNativeQuery(
					"SELECT plan,segment,base,instrument_type,description,type,lot,turnover,compare_cost,securities_transaction_tax_buy,securities_transaction_tax_sell,transaction_charges_nse_buy,transaction_charges_nse_sell,"
							+ "sebi_charges_buy,sebi_charges_sell,stamp_charges_buy,stamp_charges_sell,clearing_member,gst FROM tbl_brokerage");

			result = query.getResultList();
			for (Object[] values : result) {
				BrokerageEquityEntity brokerage = new BrokerageEquityEntity();
				if (values[0] != null) {
					brokerage.setPlan(values[0].toString());
				}
				if (values[1] != null) {
					brokerage.setSegment(values[1].toString());
				}
				if (values[2] != null) {
					brokerage.setBase(values[2].toString());
				}
				if (values[3] != null) {
					brokerage.setInstrumentType(values[3].toString());
				}
				if (values[4] != null) {
					brokerage.setDescription(values[4].toString());
				}
				if (values[5] != null) {
					brokerage.setType(values[5].toString());
				}
				if (values[6] != null) {
					brokerage.setLot(values[6].toString());
				}
				if (values[7] != null) {
					brokerage.setTurnOver(values[7].toString());
				}
				if (values[8] != null) {
					brokerage.setCompareCost(values[8].toString());
				}
				if (values[9] != null) {
					brokerage.setSecuritiesTransactionTaxBuy(values[9].toString());
				}
				if (values[10] != null) {
					brokerage.setSecuritiesTransactionTaxSell(values[10].toString());
				}
				if (values[11] != null) {
					brokerage.setTransactionChargesNseBuy(values[11].toString());
				}
				if (values[12] != null) {
					brokerage.setTransactionChargesNseSell(values[12].toString());
				}
				if (values[13] != null) {
					brokerage.setSebiChargesBuy(values[13].toString());
				}
				if (values[14] != null) {
					brokerage.setSebiChargesSell(values[14].toString());
				}
				if (values[15] != null) {
					brokerage.setStampChargesBuy(values[15].toString());
				}
				if (values[16] != null) {
					brokerage.setStampChargesSell(values[16].toString());
				}
				if (values[17] != null) {
					brokerage.setClearing_member(values[17].toString());
				}
				if (values[18] != null) {
					brokerage.setGst(values[18].toString());
					HazelcastConfig.getInstance().getBrokerageCalc()
							.put(brokerage.getPlan().toUpperCase() + "_" + brokerage.getSegment().toUpperCase() + "_"
									+ brokerage.getInstrumentType().toUpperCase() + "_"
									+ brokerage.getType().toUpperCase(), brokerage);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
