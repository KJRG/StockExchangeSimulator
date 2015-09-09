package com.capgemini.model;

import java.math.BigDecimal;
import java.util.Map;

import com.capgemini.model.strategy.PlayingStocksStrategy;

public class Player {

	private Wallet wallet;
	private PlayingStocksStrategy playingStrategy;
	private BrokersOffice brokersOffice;

	public Player(PlayingStocksStrategy playingStrategy,
			BrokersOffice brokersOffice) {
		this.wallet = new Wallet();
		this.playingStrategy = playingStrategy;
		this.brokersOffice = brokersOffice;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public PlayingStocksStrategy getPlayingStrategy() {
		return playingStrategy;
	}

	public void setPlayingStrategy(PlayingStocksStrategy playingStrategy) {
		this.playingStrategy = playingStrategy;
	}

	private void buyShares() {
		Map<SharePrice, Integer> boughtShares = brokersOffice.buy(
				playingStrategy.chooseSharesToBuy(wallet, brokersOffice),
				wallet);
		wallet.addShares(boughtShares);
	}

	private void sellShares() {
		Map<SharePrice, Integer> sharesToSell = playingStrategy
				.chooseSharesToSell(wallet, brokersOffice);
		wallet.removeShares(sharesToSell);
		BigDecimal profit = brokersOffice.sell(sharesToSell);
		wallet.setMoney(wallet.getMoney().add(profit));
	}

	public void play() {
		buyShares();
		sellShares();
		wallet.updateSharePrices(brokersOffice.getSharesPrices());
	}
}
