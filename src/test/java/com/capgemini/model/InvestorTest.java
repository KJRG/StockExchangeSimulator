package com.capgemini.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.reflection.Whitebox;

import com.capgemini.model.strategy.InvestingStrategy;

public class InvestorTest {

	@InjectMocks
	private Investor investor;
	@Mock
	private Wallet wallet;
	@Mock
	private InvestingStrategy investingStrategy;
	@Mock
	private BrokerageFirm brokerageFirm;
	@Mock
	private StockExchange stockExchange;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		Whitebox.setInternalState(investor, "wallet", wallet);
	}

	@Test
	public void investShouldCallMethodsFromOtherClasses() {
		// given when
		investor.invest();

		// then
		Mockito.verify(investingStrategy).chooseStocksToBuy(Mockito.any(), Mockito.any());
		Mockito.verify(brokerageFirm).buy(Mockito.any(), Mockito.any());
		Mockito.verify(wallet).addStocks(Mockito.any());

		Mockito.verify(investingStrategy).chooseStocksToSell(Mockito.any(), Mockito.any());
		Mockito.verify(wallet).removeStocks(Mockito.any());
		Mockito.verify(brokerageFirm).sell(Mockito.any());
		Mockito.verify(wallet).addMoney(Mockito.any());
		
		Mockito.verify(brokerageFirm).getStocks();
		Mockito.verify(wallet).updateStocks(Mockito.any());
	}

}
