package com.capgemini.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class WalletTest {
	
	@InjectMocks
	private Wallet wallet;
	@Mock
	private StockExchange stockExchange;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void walletShouldContain10000PLN() {
		// given when
		Wallet wallet = new Wallet(stockExchange);
		// then
		assertEquals(new BigDecimal("10000.0"), wallet.getMoney());
	}

	@Test
	public void walletShouldContainNoStocks() {
		// given when
		Wallet wallet = new Wallet(stockExchange);
		// then
		assertNotNull(wallet.getStocksQuantities());
		assertEquals(0, wallet.getStocksQuantities().keySet().size());
	}

	@Test
	public void walletsTotalValueShouldBe10000() {
		// given when
		Wallet wallet = new Wallet(stockExchange);
		// then
		assertEquals(new BigDecimal("10000.0"), wallet.getTotalValue());
	}

	@Test
	public void walletShouldNotAddMoneyForNullMoneyArgument() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		// when
		wallet.addMoney(null);
		// then
		assertEquals(0, wallet.getMoney().compareTo(new BigDecimal("10000.0")));
	}

	@Test
	public void walletShouldAddMoney() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		// when
		wallet.addMoney(new BigDecimal("1000.0"));
		// then
		assertEquals(0, wallet.getMoney().compareTo(new BigDecimal("11000.0")));
	}

	@Test
	public void walletShouldNotSubtractMoneyForNullMoneyArgument() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		// when
		wallet.takeMoney(null);
		// then
		assertEquals(0, wallet.getMoney().compareTo(new BigDecimal("10000.0")));
	}

	@Test
	public void walletShouldSubtractMoney() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		// when
		wallet.takeMoney(new BigDecimal("1000.0"));
		// then
		assertEquals(0, wallet.getMoney().compareTo(new BigDecimal("9000.0")));
	}

	@Test
	public void walletShouldNotSubtractMoney() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		// when
		wallet.takeMoney(new BigDecimal("11000.0"));
		// then
		assertEquals(0, wallet.getMoney().compareTo(new BigDecimal("10000.0")));
	}

	@Test
	public void walletShouldNotAddStocksForNullStocksArgument() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		// when
		wallet.addStocks(null);
		// then
		Integer numberOfStocksInWallet = wallet.getStocksQuantities().values()
				.stream().reduce(0, (a, b) -> a + b);
		assertEquals(0, (int) numberOfStocksInWallet);
	}

	@Test
	public void walletShouldNotAddStocksForEmptyStocksArgument() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		Map<Stock, Integer> stocks = Collections.emptyMap();
		// when
		wallet.addStocks(stocks);
		// then
		Integer numberOfStocksInWallet = wallet.getStocksQuantities().values()
				.stream().reduce(0, (a, b) -> a + b);
		assertEquals(0, (int) numberOfStocksInWallet);
	}

	@Test
	public void walletShouldAddStocks() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		Map<Stock, Integer> stocks = new HashMap<>();
		stocks.put(new Stock("PKOBP", LocalDate.parse("2013-01-07"),
				new BigDecimal("36.13")), 5);
		stocks.put(new Stock("TPSA", LocalDate.parse("2013-01-07"),
				new BigDecimal("12.87")), 5);
		// when
		wallet.addStocks(stocks);
		// then
		Integer numberOfStocksInWallet = wallet.getStocksQuantities().values()
				.stream().reduce(0, (a, b) -> a + b);
		assertEquals(10, (int) numberOfStocksInWallet);
	}

	@Test
	public void walletShouldNotRemoveStocksForNullStocksArgument() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		Map<Stock, Integer> stocks = new HashMap<>();
		stocks.put(new Stock("PKOBP", LocalDate.parse("2013-01-07"),
				new BigDecimal("36.13")), 5);
		stocks.put(new Stock("TPSA", LocalDate.parse("2013-01-07"),
				new BigDecimal("12.87")), 5);
		wallet.setStocksQuantities(stocks);
		// when
		wallet.removeStocks(null);
		// then
		Integer numberOfStocksInWallet = wallet.getStocksQuantities().values()
				.stream().reduce(0, (a, b) -> a + b);
		assertEquals(10, (int) numberOfStocksInWallet);
	}

	@Test
	public void walletShouldNotRemoveStocksForEmptyStocksArgument() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		Map<Stock, Integer> stocks = new HashMap<>();
		stocks.put(new Stock("PKOBP", LocalDate.parse("2013-01-07"),
				new BigDecimal("36.13")), 5);
		stocks.put(new Stock("TPSA", LocalDate.parse("2013-01-07"),
				new BigDecimal("12.87")), 5);
		wallet.setStocksQuantities(stocks);

		Map<Stock, Integer> stocksToRemove = Collections.emptyMap();

		// when
		wallet.removeStocks(stocksToRemove);

		// then
		Integer numberOfStocksInWallet = wallet.getStocksQuantities().values()
				.stream().reduce(0, (a, b) -> a + b);
		assertEquals(10, (int) numberOfStocksInWallet);
	}

	@Test
	public void walletShouldRemoveStocks() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		Map<Stock, Integer> stocks = new HashMap<>();
		stocks.put(new Stock("PKOBP", LocalDate.parse("2013-01-07"),
				new BigDecimal("36.13")), 5);
		stocks.put(new Stock("TPSA", LocalDate.parse("2013-01-07"),
				new BigDecimal("12.87")), 5);
		wallet.setStocksQuantities(stocks);

		Map<Stock, Integer> stocksToRemove = new HashMap<>();
		stocksToRemove.put(new Stock("TPSA", LocalDate.parse("2013-01-07"),
				new BigDecimal("12.87")), 3);

		// when
		wallet.removeStocks(stocksToRemove);

		// then
		Integer numberOfStocksInWallet = wallet.getStocksQuantities().values()
				.stream().reduce(0, (a, b) -> a + b);
		Integer numberOfStocksOfTheSameCompanyAsRemovedStocks = wallet
				.getStocksQuantities()
				.get(new Stock("TPSA", LocalDate.parse("2013-01-07"),
						new BigDecimal("12.87")));
		assertEquals(7, (int) numberOfStocksInWallet);
		assertEquals(2, (int) numberOfStocksOfTheSameCompanyAsRemovedStocks);
	}

	@Test
	public void walletShouldRemoveAllStocks() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		Map<Stock, Integer> stocks = new HashMap<>();
		stocks.put(new Stock("KGHM", LocalDate.parse("2013-01-07"),
				new BigDecimal("193.0")), 5);
		wallet.setStocksQuantities(stocks);

		Map<Stock, Integer> stocksToRemove = new HashMap<>();
		stocksToRemove.put(new Stock("KGHM", LocalDate.parse("2013-01-07"),
				new BigDecimal("193.0")), 5);

		// when
		wallet.removeStocks(stocksToRemove);

		// then
		Integer numberOfStocksInWallet = wallet.getStocksQuantities().values()
				.stream().reduce(0, (a, b) -> a + b);
		Integer numberOfStocksOfTheSameCompanyAsRemovedStocks = wallet
				.getStocksQuantities()
				.get(new Stock("TPSA", LocalDate.parse("2013-01-07"),
						new BigDecimal("12.87")));
		assertEquals(0, (int) numberOfStocksInWallet);
		assertNull(numberOfStocksOfTheSameCompanyAsRemovedStocks);
	}

	@Test
	public void walletShouldNotUpdateStocksForNullCurrentStocksArgument() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		Map<Stock, Integer> stocksQuantities = new HashMap<>();
		stocksQuantities.put(
				new Stock("TPSA", LocalDate.parse("2013-01-02"),
						new BigDecimal("12.16")),
				5);
		wallet.setStocksQuantities(stocksQuantities);
		
		// when
		wallet.updateStocks(null);
		
		// then
		Stock stock = wallet.getStocksQuantities().keySet().stream()
				.filter(s -> s.getCompanyName().equals("TPSA")).findFirst()
				.get();
		assertNotNull(stock);
		assertEquals(0, stock.getPrice().compareTo(new BigDecimal("12.16")));
	}

	@Test
	public void walletShouldNotUpdateStocksForEmptyCurrentStocksArgument() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		Map<Stock, Integer> stocksQuantities = new HashMap<>();
		stocksQuantities.put(
				new Stock("TPSA", LocalDate.parse("2013-01-02"),
						new BigDecimal("12.16")),
				5);
		wallet.setStocksQuantities(stocksQuantities);
		
		List<Stock> currentStocks = Collections.emptyList();
		
		// when
		wallet.updateStocks(currentStocks);
		
		// then
		Stock stock = wallet.getStocksQuantities().keySet().stream()
				.filter(s -> s.getCompanyName().equals("TPSA")).findFirst()
				.get();
		assertNotNull(stock);
		assertEquals(0, stock.getPrice().compareTo(new BigDecimal("12.16")));
	}

	@Test
	public void walletShouldUpdateStocks() {
		// given
		Wallet wallet = new Wallet(stockExchange);
		Map<Stock, Integer> stocksQuantities = new HashMap<>();

		stocksQuantities.put(
				new Stock("TPSA", LocalDate.parse("2013-01-02"),
						new BigDecimal("12.16")),
				3);
		stocksQuantities.put(
				new Stock("PKOBP", LocalDate.parse("2013-01-02"),
						new BigDecimal("37.35")),
				7);

		List<Stock> stocksForCurrentDate = Arrays.asList(
				new Stock("TPSA", LocalDate.parse("2013-07-04"),
						new BigDecimal("7.74")),
				new Stock("PKOBP", LocalDate.parse("2013-07-04"),
						new BigDecimal("34.88")),
				new Stock("PGNIG", LocalDate.parse("2013-07-04"),
						new BigDecimal("6.11")),
				new Stock("KGHM", LocalDate.parse("2013-07-04"),
						new BigDecimal("130.0")),
				new Stock("JSW", LocalDate.parse("2013-07-04"),
						new BigDecimal("66.3")));

		wallet.setMoney(BigDecimal.ZERO);
		wallet.setStocksQuantities(stocksQuantities);

		// when
		wallet.updateStocks(stocksForCurrentDate);

		// then
		// Correct result is: 3 * 7.74 + 7 * 34.88
		BigDecimal expectedResult = new BigDecimal("3.0")
				.multiply(new BigDecimal("7.74"));
		expectedResult = expectedResult
				.add(new BigDecimal("7.0").multiply(new BigDecimal("34.88")));
		assertEquals(0, expectedResult.compareTo(wallet.getTotalValue()));
	}

}
