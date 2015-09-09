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

public class BrokersOfficeTest {

	@InjectMocks
	private BrokersOffice brokersOffice;
	@Mock
	private StockExchange stockExchange;

	@Before
	public void setUp() {
		stockExchange = new StockExchange();
		MockitoAnnotations.initMocks(this);
		brokersOffice = new BrokersOffice(stockExchange);
	}

	@Test
	public void feeShouldBe0point5Percent() {
		// given when
		// 100% - 0.5% = 99.5%
		// 1 * 99.5% = 0.995
		BigDecimal expectedResult = new BigDecimal("0.995");
		// then
		assertEquals(expectedResult, brokersOffice.getBrokerage());
	}

	@Test
	public void brokersOfficeShouldCallGetCompaniesNamesFromClassStockExchange() {
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
		brokersOffice.getCompaniesNames();

		// then
		Mockito.verify(stockExchange).getCompaniesNames();
	}

	@Test
	public void brokersOfficeShouldCallGetSharesPricesFromClassStockExchange() {
		// given
		Mockito.when(stockExchange.getSharesPrices())
				.then(new Answer<List<SharePrice>>() {

					@Override
					public List<SharePrice> answer(InvocationOnMock invocation)
							throws Throwable {

						return Arrays.asList(new SharePrice("TPSA",
								LocalDate.parse("2013-07-04"),
								new BigDecimal("7.74")),
								new SharePrice("PKOBP",
										LocalDate.parse("2013-07-04"),
										new BigDecimal("34.88")),
								new SharePrice("PGNIG",
										LocalDate.parse("2013-07-04"),
										new BigDecimal("6.11")),
								new SharePrice("KGHM",
										LocalDate.parse("2013-07-04"),
										new BigDecimal("130.0")),
								new SharePrice("JSW",
										LocalDate.parse("2013-07-04"),
										new BigDecimal("66.32")));
					}

				});

		// when
		brokersOffice.getSharesPrices();

		// then
		Mockito.verify(stockExchange).getSharesPrices();
	}

	@Test
	public void brokersOfficeShouldCallGetShareByCompanyNameFromClassStockExchange() {
		// given
		Mockito.when(stockExchange.getShareByCompanyName(Mockito.anyString()))
				.then(new Answer<SharePrice>() {

					@Override
					public SharePrice answer(InvocationOnMock invocation)
							throws Throwable {
						Object[] args = invocation.getArguments();
						return new SharePrice((String) args[0],
								LocalDate.parse("2015-01-02"),
								new BigDecimal("17.0"));
					}

				});
		String companyName = "PKOBP";

		// when
		SharePrice result = brokersOffice.getShareByCompanyName(companyName);

		// then
		ArgumentCaptor<String> capture = ArgumentCaptor.forClass(String.class);
		Mockito.verify(stockExchange).getShareByCompanyName(capture.capture());
		assertEquals(companyName, result.getCompanyName());
		assertEquals(LocalDate.parse("2015-01-02"), result.getDate());
		assertEquals(new BigDecimal("17.0").compareTo(result.getPrice()), 0);
	}

	@Test
	public void buyShouldReturnEmptyMapForNullShareQuantities() {
		// given
		Wallet wallet = new Wallet();
		// when
		Map<SharePrice, Integer> result = brokersOffice.buy(null, wallet);
		// then
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void buyShouldReturnEmptyMapForNullWallet() {
		// given
		Map<String, Integer> sharesToBuy = new HashMap<>();
		sharesToBuy.put("KGHM", 17);
		// when
		Map<SharePrice, Integer> result = brokersOffice.buy(sharesToBuy, null);
		// then
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void buyShouldReturnEmptyMapForEmptyShareQuantities() {
		// given
		Map<String, Integer> sharesToBuy = Collections.emptyMap();
		Wallet wallet = new Wallet();
		// when
		Map<SharePrice, Integer> result = brokersOffice.buy(sharesToBuy,
				wallet);
		// then
		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	public void buyShouldReturnBoughtShares() {
		// given
		Map<String, Integer> sharesToBuy = new HashMap<>();
		Wallet wallet = new Wallet();

		sharesToBuy.put("KGHM", 1);
		sharesToBuy.put("TPSA", 7);
		sharesToBuy.put("JSW", 2);
		sharesToBuy.put("PKOBP", 8);
		sharesToBuy.put("PGNIG", 12);
		
		Mockito.when(stockExchange.getShareByCompanyName(Mockito.anyString()))
		.then(new Answer<SharePrice>() {
			
			@Override
			public SharePrice answer(InvocationOnMock invocation)
					throws Throwable {
				Object[] args = invocation.getArguments();
				return new SharePrice((String) args[0],
						LocalDate.parse("2015-01-02"),
						new BigDecimal("17.0"));
			}
			
		});
		
		// when
		Map<SharePrice, Integer> result = brokersOffice.buy(sharesToBuy,
				wallet);
		
		// then
		assertNotNull(result);
		assertFalse(result.isEmpty());
		assertEquals(result.size(), 5);
		Integer numberOfBoughtShares = result.values().stream().reduce(0, (a, b) -> a + b);
		assertEquals((int) numberOfBoughtShares, 30);
	}

	@Test
	public void sellShouldReturn0ForNullArgument() {
		// given
		BigDecimal result = null;
		// when
		result = brokersOffice.sell(null);
		// then
		assertEquals(BigDecimal.ZERO.compareTo(result), 0);
	}

	@Test
	public void sellShouldReturn0ForEmptyMap() {
		// given
		BigDecimal result = null;
		Map<SharePrice, Integer> sharesToSell = Collections.emptyMap();
		// when
		result = brokersOffice.sell(sharesToSell);
		// then
		assertEquals(BigDecimal.ZERO.compareTo(result), 0);
	}

	@Test
	public void sellShouldReturn253MinusFee() {
		// given
		BigDecimal result = null;
		Map<SharePrice, Integer> sharesToSell = new HashMap<>();
		sharesToSell.put(new SharePrice("KGHM", LocalDate.parse("2013-07-03"),
				new BigDecimal("126.5")), 2);
		// when
		result = brokersOffice.sell(sharesToSell);
		// then
		BigDecimal expectedResult = new BigDecimal("253.0")
				.multiply(brokersOffice.getBrokerage());
		assertEquals(expectedResult.compareTo(result), 0);
	}
}
