package com.capgemini.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class WalletTest {

	@Test
	public void walletShouldContain10000PLN() {
		// given
		Wallet wallet = new Wallet();
		// when then
		assertEquals(new BigDecimal("10000.0"), wallet.getMoney());
	}

	@Test
	public void walletShouldContainNoStocks() {
		// given
		Wallet wallet = new Wallet();
		// when then
		assertNotNull(wallet.getStocksQuantities());
		assertEquals(0, wallet.getStocksQuantities().keySet().size());
	}

	@Test
	public void walletsTotalValueShouldBe10000() {
		// given
		Wallet wallet = new Wallet();
		// when then
		assertEquals(new BigDecimal("10000.0"), wallet.getTotalValue());
	}

	@Test
	public void walletShouldUpdateStocks() {
		// given
		Wallet wallet = new Wallet();
		Map<Stock, Integer> stocksQuantities = new HashMap<>();

		stocksQuantities.put(
				new Stock("TPSA", LocalDate.parse("2013-01-02"), new BigDecimal("12.16")),
				3);
		stocksQuantities.put(
				new Stock("PKOBP", LocalDate.parse("2013-01-02"), new BigDecimal("37.35")),
				7);

		List<Stock> stocksForCurrentDate = Arrays.asList(
				new Stock("TPSA", LocalDate.parse("2013-07-04"), new BigDecimal("7.74")),
				new Stock("PKOBP", LocalDate.parse("2013-07-04"), new BigDecimal("34.88")),
				new Stock("PGNIG", LocalDate.parse("2013-07-04"), new BigDecimal("6.11")),
				new Stock("KGHM", LocalDate.parse("2013-07-04"), new BigDecimal("130.0")),
				new Stock("JSW", LocalDate.parse("2013-07-04"), new BigDecimal("66.3")));
		
		// when
		wallet.setMoney(BigDecimal.ZERO);
		wallet.setStocksQuantities(stocksQuantities);
		wallet.updateStocks(stocksForCurrentDate);
		
		// then
		// Correct result is: 3 * 7.74 + 7 * 34.88
		BigDecimal expectedResult = new BigDecimal("3.0").multiply(new BigDecimal("7.74"));
		expectedResult = expectedResult.add(new BigDecimal("7.0").multiply(new BigDecimal("34.88")));
		assertEquals(expectedResult.compareTo(wallet.getTotalValue()), 0);
	}

}
