package com.capgemini.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BrokerageFirm {

	private static final BigDecimal MONEY_AFTER_CHARGING_COMMISSION = new BigDecimal(
			"0.995");
	private StockExchange stockExchange;
	
	public BigDecimal getCommission() {
		return MONEY_AFTER_CHARGING_COMMISSION;
	}

	public BrokerageFirm(StockExchange stockExchange) {
		this.stockExchange = stockExchange;
	}

	public Set<String> getCompaniesNames() {
		return stockExchange.getCompaniesNames();
	}

	public List<Stock> getStocks() {
		return stockExchange.getStocks();
	}

	public Stock getStockByCompanyName(String companyName) {
		return stockExchange.getStockByCompanyName(companyName);
	}

	public Map<Stock, Integer> buy(Map<String, Integer> stocksQuantities,
			Wallet wallet) {

		if (stocksQuantities == null || stocksQuantities.isEmpty()
				|| wallet == null) {
			return Collections.emptyMap();
		}

		Map<Stock, Integer> boughtStocks = new HashMap<>();
		BigDecimal totalPrice = BigDecimal.ZERO;

		for (String companyName : stocksQuantities.keySet()) {
			Stock boughtStock = stockExchange
					.getStockByCompanyName(companyName);
			Integer quantity = stocksQuantities.get(companyName);

			boughtStocks.put(boughtStock, quantity);

			totalPrice = totalPrice.add(boughtStock.getPrice().multiply(new BigDecimal(quantity)));
		}

		if (totalPrice.compareTo(wallet.getMoney()) > 0) {
			return Collections.emptyMap();
		}
		wallet.takeMoney(totalPrice);

		return boughtStocks;
	}

	public BigDecimal sell(Map<Stock, Integer> stocksQuantities) {
		
		BigDecimal totalPrice = BigDecimal.ZERO;

		if (stocksQuantities == null) {
			return totalPrice;
		}

		for (Stock stock : stocksQuantities.keySet()) {
			totalPrice = totalPrice.add(stock.getPrice().multiply(new BigDecimal(stocksQuantities.get(stock))));
		}

		/*
		 * The brokerage firm charges commission.
		 */
		return totalPrice.multiply(MONEY_AFTER_CHARGING_COMMISSION);
	}
}
