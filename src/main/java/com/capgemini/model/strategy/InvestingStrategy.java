package com.capgemini.model.strategy;

import java.util.Map;

import com.capgemini.model.BrokerageFirm;
import com.capgemini.model.Stock;
import com.capgemini.model.Wallet;

public interface InvestingStrategy {
	
	Map<String, Integer> chooseStocksToBuy(Wallet wallet, BrokerageFirm brokerageFirm);
	Map<Stock, Integer> chooseStocksToSell(Wallet wallet, BrokerageFirm brokerageFirm);
}
