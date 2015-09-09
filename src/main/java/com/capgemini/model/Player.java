package com.capgemini.model;

import com.capgemini.model.strategy.PlayingStocksStrategy;

public class Player {

	private Wallet wallet;
	private PlayingStocksStrategy playingStrategy;
	private BrokersOffice brokersOffice;

	public Player(PlayingStocksStrategy playingStrategy, BrokersOffice brokersOffice) {
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
	
	public void play() {
		wallet.addShares(playingStrategy.buyShares(wallet, brokersOffice));
		wallet.setMoney(wallet.getMoney().add(playingStrategy.sellShares(wallet, brokersOffice)));
		wallet.updateSharePrices(brokersOffice.getSharesPrices());
	}
}
