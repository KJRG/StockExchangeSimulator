package com.capgemini.model.strategy;

import java.util.Map;

import com.capgemini.model.BrokersOffice;
import com.capgemini.model.SharePrice;
import com.capgemini.model.Wallet;

public interface PlayingStocksStrategy {
	
	Map<String, Integer> chooseSharesToBuy(Wallet wallet, BrokersOffice brokersOffice);
	Map<SharePrice, Integer> chooseSharesToSell(Wallet wallet, BrokersOffice brokersOffice);
}
