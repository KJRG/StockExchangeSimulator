package com.capgemini.model.strategy.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.capgemini.model.BrokersOffice;
import com.capgemini.model.Stock;
import com.capgemini.model.Wallet;
import com.capgemini.model.strategy.InvestingStrategy;

public class RandomInvestingStrategy implements InvestingStrategy {

	private static final Integer MAX_NUMBER_OF_STOCKS_TO_BUY = 100;

	private Random randomGenerator = new Random();

	@Override
	public Map<String, Integer> chooseStocksToBuy(Wallet wallet,
			BrokersOffice brokersOffice) {
		
		Map<String, Integer> stocksToBuy = new HashMap<>();

		for (String companyName : brokersOffice.getCompaniesNames()) {
			BigDecimal totalPrice = BigDecimal.ZERO;

			if (randomGenerator.nextBoolean()) {
				Integer quantity = 0;
				BigDecimal price = brokersOffice
						.getStockByCompanyName(companyName).getPrice();

				for (int i = 0; i < randomGenerator
						.nextInt(MAX_NUMBER_OF_STOCKS_TO_BUY); i++) {
					
					if (totalPrice.compareTo(wallet.getMoney()) > 0) {
						break;
					}

					quantity++;
					totalPrice = totalPrice.add(price);
				}

				stocksToBuy.put(companyName, quantity);
			}
		}

		return stocksToBuy;
	}

	@Override
	public Map<Stock, Integer> chooseStocksToSell(Wallet wallet,
			BrokersOffice brokersOffice) {

		Map<Stock, Integer> playersStocksQuantities = wallet
				.getStocksQuantities();

		if (playersStocksQuantities == null
				|| playersStocksQuantities.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Stock, Integer> stocksToSell = new HashMap<>();

		for (Stock stock : playersStocksQuantities.keySet()) {
			if (randomGenerator.nextBoolean()) {

				Integer numberOfOwnedStocks = playersStocksQuantities
						.get(stock);
				if (numberOfOwnedStocks == 0) {
					continue;
				}

				Integer quantity = randomGenerator.nextInt(numberOfOwnedStocks)
						+ 1;
				stocksToSell.put(stock, quantity);
			}
		}

		return stocksToSell;
	}
}
