package com.capgemini.model;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class BrokerageFirmTest {

	@InjectMocks
	private BrokerageFirm brokerageFirm;
	@Mock
	private StockExchange stockExchange;

	@Before
	public void setUp() {
		stockExchange = new StockExchange();
		MockitoAnnotations.initMocks(this);
		brokerageFirm = new BrokerageFirm(stockExchange);
	}

	@Test
	public void commissionShouldBe0point5Percent() {
		// given when
		// 100% - 0.5% = 99.5%
		// 1 * 99.5% = 0.995
		BigDecimal expectedResult = new BigDecimal("0.995");
		// then
		assertEquals(expectedResult, brokerageFirm.getCommission());
	}

	@Test
	public void brokerageFirmShouldCallGetCompaniesNamesFromClassStockExchange() {
		// given
		Mockito.when(stockExchange.getCompaniesNames())
				.then(new Answer<Set<String>>() {

					@Override
					public Set<String> answer(InvocationOnMock invocation)
							throws Throwable {
						return new HashSet<String>(Arrays.asList("TPSA",
								"PKOBP", "PGNIG", "KGHM", "JSW"));
					}

				});

		// when
		brokerageFirm.getCompaniesNames();

		// then
		Mockito.verify(stockExchange).getCompaniesNames();
	}

	@Test
	public void brokerageFirmShouldCallGetStocksPricesFromClassStockExchange() {
		// given
		Mockito.when(stockExchange.getStocks())
				.then(new Answer<List<Stock>>() {

					@Override
					public List<Stock> answer(InvocationOnMock invocation)
							throws Throwable {

						return Arrays.asList(new Stock("TPSA",
								LocalDate.parse("2013-07-04"),
								new BigDecimal("7.74")),
								new Stock("PKOBP",
										LocalDate.parse("2013-07-04"),
										new BigDecimal("34.88")),
								new Stock("PGNIG",
										LocalDate.parse("2013-07-04"),
										new BigDecimal("6.11")),
								new Stock("KGHM",
										LocalDate.parse("2013-07-04"),
										new BigDecimal("130.0")),
								new Stock("JSW",
										LocalDate.parse("2013-07-04"),
										new BigDecimal("66.32")));
					}

				});

		// when
		brokerageFirm.getStocks();

		// then
		Mockito.verify(stockExchange).getStocks();
	}

	@Test
	public void brokerageFirmShouldCallGetStockByCompanyNameFromClassStockExchange() {
		// given
		Mockito.when(stockExchange.getStockByCompanyName(Mockito.anyString()))
				.then(new Answer<Stock>() {

					@Override
					public Stock answer(InvocationOnMock invocation)
							throws Throwable {
						Object[] args = invocation.getArguments();
						return new Stock((String) args[0],
								LocalDate.parse("2015-01-02"),
								new BigDecimal("17.0"));
					}

				});
		String companyName = "PKOBP";

		// when
		Stock result = brokerageFirm.getStockByCompanyName(companyName);

		// then
		ArgumentCaptor<String> capture = ArgumentCaptor.forClass(String.class);
		Mockito.verify(stockExchange).getStockByCompanyName(capture.capture());
		assertEquals(companyName, result.getCompanyName());
		assertEquals(LocalDate.parse("2015-01-02"), result.getDate());
		assertEquals(new BigDecimal("17.0").compareTo(result.getPrice()), 0);
	}

	@Test
	public void buyShouldReturnEmptyMapForNullStockQuantities() {
		// given
		Wallet wallet = new Wallet();
		// when
		Map<Stock, Integer> result = brokerageFirm.buy(null, wallet);
		// then
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void buyShouldReturnEmptyMapForNullWallet() {
		// given
		Map<String, Integer> stocksToBuy = new HashMap<>();
		stocksToBuy.put("KGHM", 17);
		// when
		Map<Stock, Integer> result = brokerageFirm.buy(stocksToBuy, null);
		// then
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void buyShouldReturnEmptyMapForEmptyStockQuantities() {
		// given
		Map<String, Integer> stocksToBuy = Collections.emptyMap();
		Wallet wallet = new Wallet();
		// when
		Map<Stock, Integer> result = brokerageFirm.buy(stocksToBuy,
				wallet);
		// then
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void buyShouldReturnBoughtStocks() {
		// given
		Map<String, Integer> stocksToBuy = new HashMap<>();
		Wallet wallet = new Wallet();

		stocksToBuy.put("KGHM", 1);
		stocksToBuy.put("TPSA", 7);
		stocksToBuy.put("JSW", 2);
		stocksToBuy.put("PKOBP", 8);
		stocksToBuy.put("PGNIG", 12);
		
		Mockito.when(stockExchange.getStockByCompanyName(Mockito.anyString()))
		.then(new Answer<Stock>() {
			
			@Override
			public Stock answer(InvocationOnMock invocation)
					throws Throwable {
				Object[] args = invocation.getArguments();
				return new Stock((String) args[0],
						LocalDate.parse("2015-01-02"),
						new BigDecimal("17.0"));
			}
			
		});
		
		// when
		Map<Stock, Integer> result = brokerageFirm.buy(stocksToBuy,
				wallet);
		
		// then
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(result.size(), 5);
		Integer numberOfBoughtStocks = result.values().stream().reduce(0, (a, b) -> a + b);
		assertEquals((int) numberOfBoughtStocks, 30);
	}

	@Test
	public void sellShouldReturn0ForNullArgument() {
		// given
		BigDecimal result = null;
		// when
		result = brokerageFirm.sell(null);
		// then
		assertEquals(BigDecimal.ZERO.compareTo(result), 0);
	}

	@Test
	public void sellShouldReturn0ForEmptyMap() {
		// given
		BigDecimal result = null;
		Map<Stock, Integer> stocksToSell = Collections.emptyMap();
		// when
		result = brokerageFirm.sell(stocksToSell);
		// then
		assertEquals(BigDecimal.ZERO.compareTo(result), 0);
	}

	@Test
	public void sellShouldReturn253MinusCommission() {
		// given
		BigDecimal result = null;
		Map<Stock, Integer> stocksToSell = new HashMap<>();
		stocksToSell.put(new Stock("KGHM", LocalDate.parse("2013-07-03"),
				new BigDecimal("126.5")), 2);
		// when
		result = brokerageFirm.sell(stocksToSell);
		// then
		BigDecimal expectedResult = new BigDecimal("253.0")
				.multiply(brokerageFirm.getCommission());
		assertEquals(expectedResult.compareTo(result), 0);
	}
}
