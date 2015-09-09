package com.capgemini.model.strategy.impl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.capgemini.model.BrokersOffice;
import com.capgemini.model.SharePrice;
import com.capgemini.model.Wallet;
import com.capgemini.model.strategy.PlayingStocksStrategy;

public class RandomPlayingStrategy implements PlayingStocksStrategy {

	private static final Integer MAX_NUMBER_OF_SHARES_TO_BUY = 100;

	private Random randomGenerator = new Random();

	@Override
	public Map<String, Integer> chooseSharesToBuy(Wallet wallet,
			BrokersOffice brokersOffice) {
		Map<String, Integer> sharesToBuy = new HashMap<>();

		for (String companyName : brokersOffice.getCompaniesNames()) {
			BigDecimal totalPrice = BigDecimal.ZERO;

			if (randomGenerator.nextBoolean()) {
				Integer quantity = 0;
				BigDecimal price = brokersOffice
						.getShareByCompanyName(companyName).getPrice();

				for (int i = 0; i < randomGenerator
						.nextInt(MAX_NUMBER_OF_SHARES_TO_BUY); i++) {
					
					if (totalPrice.compareTo(wallet.getMoney()) > 0) {
						break;
					}

					quantity++;
					totalPrice = totalPrice.add(price);
				}

				sharesToBuy.put(companyName, quantity);
			}
		}

		return sharesToBuy;
	}

	@Override
	public Map<SharePrice, Integer> chooseSharesToSell(Wallet wallet,
			BrokersOffice brokersOffice) {

		Map<SharePrice, Integer> playersSharesQuantities = wallet
				.getSharesQuantities();

		if (playersSharesQuantities == null
				|| playersSharesQuantities.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<SharePrice, Integer> sharesToSell = new HashMap<>();

		for (SharePrice sharePrice : playersSharesQuantities.keySet()) {
			if (randomGenerator.nextBoolean()) {

				Integer numberOfOwnedShares = playersSharesQuantities
						.get(sharePrice);
				if (numberOfOwnedShares == 0) {
					continue;
				}

				Integer quantity = randomGenerator.nextInt(numberOfOwnedShares)
						+ 1;
				sharesToSell.put(sharePrice, quantity);
			}
		}

		return sharesToSell;
		
//		wallet.removeShares(sharesToSell);
//		return brokersOffice.sell(sharesToSell);
	}

}
