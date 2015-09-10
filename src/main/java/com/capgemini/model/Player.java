package com.capgemini.model;

import java.math.BigDecimal;
import java.util.Map;

import com.capgemini.model.strategy.InvestingStrategy;

public class Player {

	private Wallet wallet;
	private InvestingStrategy investingStrategy;
	private BrokerageFirm brokerageFirm;

	public Player(InvestingStrategy investingStrategy,
			BrokerageFirm brokerageFirm) {
		this.wallet = new Wallet();
		this.investingStrategy = investingStrategy;
		this.brokerageFirm = brokerageFirm;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public InvestingStrategy getInvestingStrategy() {
		return investingStrategy;
	}

	public void setInvestingStrategy(InvestingStrategy investingStrategy) {
		this.investingStrategy = investingStrategy;
	}

	private void buyStocks() {
		Map<String, Integer> stocksToBuy = investingStrategy
				.chooseStocksToBuy(wallet, brokerageFirm);
		Map<Stock, Integer> boughtStocks = brokerageFirm.buy(
				stocksToBuy,
				wallet);
		wallet.addStocks(boughtStocks);
	}

	private void sellStocks() {
		Map<Stock, Integer> stocksToSell = investingStrategy
				.chooseStocksToSell(wallet, brokerageFirm);
		wallet.removeStocks(stocksToSell);
		BigDecimal profit = brokerageFirm.sell(stocksToSell);
		wallet.addMoney(profit);
	}

	public void play() {
		buyStocks();
		sellStocks();
		wallet.updateStocks(brokerageFirm.getStocks());
	}
}
