package com.capgemini.model.strategy.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.capgemini.model.BrokerageFirm;
import com.capgemini.model.Stock;
import com.capgemini.model.Wallet;
import com.capgemini.model.strategy.InvestingStrategy;

public class TransactionsBasedOnPricesFromYesterdayStrategy
		implements
			InvestingStrategy {

	private Set<Stock> stocksFromYesterday = new HashSet<>();

	@Override
	public Map<String, Integer> chooseStocksToBuy(Wallet wallet,
			BrokerageFirm brokerageFirm) {
		Map<String, Integer> stocksToBuy = new HashMap<>();
		Stock stockWithMaximumPriceDrop = null;
		BigDecimal maximumPriceDrop = BigDecimal.ZERO;

		if (stocksFromYesterday.isEmpty()) {
			/*
			 * If there is no data, don't buy any stocks and update data.
			 */
			updateStocks(brokerageFirm.getStocks());
			return Collections.emptyMap();
		}

		for (Stock stock : brokerageFirm.getStocks()) {
			BigDecimal priceFromYesterday = stocksFromYesterday.stream()
					.filter(s -> s.getCompanyName()
							.equals(stock.getCompanyName()))
					.findFirst().get().getPrice();

			if (priceFromYesterday == null) {
				continue;
			}

			BigDecimal priceDifference = priceFromYesterday
					.subtract(stock.getPrice());
			if (priceDifference.compareTo(BigDecimal.ZERO) > 0
					&& priceDifference.compareTo(maximumPriceDrop) > 0) {
				stockWithMaximumPriceDrop = stock;
			}
		}

		if (stockWithMaximumPriceDrop == null) {
			return Collections.emptyMap();
		}

		/*
		 * Invest at most 5% of investor's money.
		 */
		Integer quantity = 0;
		BigDecimal price = BigDecimal.ZERO;
		BigDecimal priceLimit = wallet.getMoney()
				.divide(new BigDecimal("20.0"));

		while (price.compareTo(priceLimit) <= 0) {
			quantity++;
			price = price.add(stockWithMaximumPriceDrop.getPrice());
		}

		stocksToBuy.put(stockWithMaximumPriceDrop.getCompanyName(), quantity);

		updateStocks(brokerageFirm.getStocks());
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

		List<Stock> currentStocks = brokerageFirm.getStocks();

		for (Stock stock : playersStocksQuantities.keySet()) {
			BigDecimal currentPrice = currentStocks.stream().filter(
					s -> s.getCompanyName().equals(stock.getCompanyName()))
					.findFirst().get().getPrice();

			BigDecimal priceFromYesterday = stock.getPrice();

			if (priceFromYesterday.compareTo(currentPrice) < 0) {
				Integer quantity = playersStocksQuantities.get(stock);
				/*
				 * Sell half of the stocks which are more expensive then they
				 * were yesterday.
				 */
				stocksToSell.put(stock, quantity / 2);
			}
		}

		return stocksToSell;
	}

	private void updateStocks(List<Stock> stocks) {
		if (stocksFromYesterday.isEmpty()) {
			for (Stock stock : stocks) {
				stocksFromYesterday.add(stock);
			}
		}

		Set<Stock> updatedStocks = new HashSet<>();
		for (final Stock stock : stocksFromYesterday) {
			Stock potentiallyUpdatedStock = stocks.stream().filter(
					s -> s.getCompanyName().equals(stock.getCompanyName()))
					.findFirst().get();
			if (potentiallyUpdatedStock == null) {
				continue;
			}

			LocalDate date = potentiallyUpdatedStock.getDate();
			if (date.compareTo(stock.getDate()) > 0) {
				updatedStocks.add(potentiallyUpdatedStock);
				continue;
			}
			updatedStocks.add(stock);
		}

		stocksFromYesterday = updatedStocks;
	}
}
