package com.capgemini.model.strategy.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.capgemini.model.BrokerageFirm;
import com.capgemini.model.Stock;
import com.capgemini.model.Wallet;
import com.capgemini.model.strategy.InvestingStrategy;
 
public class SellExpensiveBuyCheapStrategy implements InvestingStrategy {

	private Comparator<Stock> stockPriceComparator = new Comparator<Stock>() {

		@Override
		public int compare(Stock s1, Stock s2) {
			return s1.getPrice().compareTo(s2.getPrice());
		}
	};

	@Override
	public Map<String, Integer> chooseStocksToBuy(Wallet wallet,
			BrokerageFirm brokerageFirm) {
		Map<String, Integer> stocksToBuy = new HashMap<>();

		Stock cheapestStocks = brokerageFirm.getStocks().stream()
				.min(stockPriceComparator).get();
		/*
		 * Invest at most 10% of investor's money.
		 */
		Integer quantity = 0;
		BigDecimal price = BigDecimal.ZERO;
		BigDecimal priceLimit = wallet.getMoney()
				.divide(new BigDecimal("10.0"));
		while (price.compareTo(priceLimit) <= 0) {
			quantity++;
			price = price.add(cheapestStocks.getPrice());
		}
		stocksToBuy.put(cheapestStocks.getCompanyName(), quantity);

		return stocksToBuy;
	}

	@Override
	public Map<Stock, Integer> chooseStocksToSell(Wallet wallet,
			BrokerageFirm brokerageFirm) {

		Map<Stock, Integer> playersStocksQuantities = wallet
				.getStocksQuantities();

		if (playersStocksQuantities == null
				|| playersStocksQuantities.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Stock, Integer> stocksToSell = new HashMap<>();

		Stock mostExpensiveStocks = playersStocksQuantities.keySet().stream()
				.max(stockPriceComparator).get();
		/*
		 * Sell 10% of investor's most expensive stocks.
		 */
		Integer quantity = playersStocksQuantities.get(mostExpensiveStocks);
		if (quantity == null || quantity == 0) {
			return Collections.emptyMap();
		}
		quantity /= 10;
		stocksToSell.put(mostExpensiveStocks, quantity);

		return stocksToSell;
	}

}
