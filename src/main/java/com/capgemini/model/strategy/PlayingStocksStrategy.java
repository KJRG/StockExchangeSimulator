package com.capgemini.model.strategy;

import java.math.BigDecimal;
import java.util.Map;

import com.capgemini.model.BrokersOffice;
import com.capgemini.model.SharePrice;
import com.capgemini.model.Wallet;

public interface PlayingStocksStrategy {
	Map<SharePrice, Integer> buyShares(Wallet wallet, BrokersOffice brokersOffice);
	BigDecimal sellShares(Wallet wallet, BrokersOffice brokersOffice);
}
