package com.capgemini.model.strategy;

import java.util.Map;

import com.capgemini.model.BrokersOffice;
import com.capgemini.model.Stock;
import com.capgemini.model.Wallet;

public interface InvestingStrategy {
	
	Map<String, Integer> chooseStocksToBuy(Wallet wallet, BrokersOffice brokersOffice);
	Map<Stock, Integer> chooseStocksToSell(Wallet wallet, BrokersOffice brokersOffice);
}
