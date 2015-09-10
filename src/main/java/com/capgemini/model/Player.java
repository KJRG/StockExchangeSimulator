package com.capgemini.model;

import java.math.BigDecimal;
import java.util.Map;

import com.capgemini.model.strategy.InvestingStrategy;

public class Player {

	private Wallet wallet;
	private InvestingStrategy investingStrategy;
	private BrokersOffice brokersOffice;

	public Player(InvestingStrategy investingStrategy,
			BrokersOffice brokersOffice) {
		this.wallet = new Wallet();
		this.investingStrategy = investingStrategy;
		this.brokersOffice = brokersOffice;
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
				.chooseStocksToBuy(wallet, brokersOffice);
		Map<Stock, Integer> boughtStocks = brokersOffice.buy(
				stocksToBuy,
				wallet);
		wallet.addStocks(boughtStocks);
	}

	private void sellStocks() {
		Map<Stock, Integer> stocksToSell = investingStrategy
				.chooseStocksToSell(wallet, brokersOffice);
		wallet.removeStocks(stocksToSell);
		BigDecimal profit = brokersOffice.sell(stocksToSell);
		wallet.addMoney(profit);
	}

	public void play() {
		buyStocks();
		sellStocks();
		wallet.updateStocks(brokersOffice.getStocks());
	}
}
