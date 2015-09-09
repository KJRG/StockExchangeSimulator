package com.capgemini.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BrokersOffice {

	private static final BigDecimal SUM_AFTER_CHARGING_BROKERAGE = new BigDecimal(
			"0.995");
	private StockExchange stockExchange;
	
	public BigDecimal getBrokerage() {
		return SUM_AFTER_CHARGING_BROKERAGE;
	}

	public BrokersOffice(StockExchange stockExchange) {
		this.stockExchange = stockExchange;
	}

	public Set<String> getCompaniesNames() {
		return stockExchange.getCompaniesNames();
	}

	public List<SharePrice> getSharesPrices() {
		return stockExchange.getSharesPrices();
	}

	public SharePrice getShareByCompanyName(String companyName) {
		return stockExchange.getShareByCompanyName(companyName);
	}

	public Map<SharePrice, Integer> buy(Map<String, Integer> shareQuantities,
			Wallet wallet) {

		if (shareQuantities == null || shareQuantities.isEmpty()
				|| wallet == null) {
			return Collections.emptyMap();
		}

		Map<SharePrice, Integer> boughtShares = new HashMap<>();
		BigDecimal totalPrice = BigDecimal.ZERO;

		for (String companyName : shareQuantities.keySet()) {
			SharePrice boughtShare = stockExchange
					.getShareByCompanyName(companyName);
			Integer quantity = shareQuantities.get(companyName);

			boughtShares.put(boughtShare, quantity);

			totalPrice = totalPrice.add(boughtShare.getPrice().multiply(new BigDecimal(quantity)));
		}

		if (totalPrice.compareTo(wallet.getMoney()) > 0) {
			return Collections.emptyMap();
		}
		wallet.takeMoney(totalPrice);

		return boughtShares;
	}

	public BigDecimal sell(Map<SharePrice, Integer> sharesQuantities) {
		
		BigDecimal sum = BigDecimal.ZERO;

		if (sharesQuantities == null) {
			return sum;
		}

		for (SharePrice share : sharesQuantities.keySet()) {
			sum = sum.add(share.getPrice().multiply(new BigDecimal(sharesQuantities.get(share))));
		}

		/*
		 * The broker's office charges brokerage.
		 */
		return sum.multiply(SUM_AFTER_CHARGING_BROKERAGE);
	}
}
