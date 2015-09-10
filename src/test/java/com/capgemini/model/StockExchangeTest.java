package com.capgemini.model;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.capgemini.dataprovider.DataProvider;

public class StockExchangeTest {

	@InjectMocks
	private StockExchange stockExchange;
	@Mock
	private DataProvider dataProvider;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void simulationStatusShouldBeSimulationUninitialised() {
		// given when
		StockExchange stockExchange = new StockExchange();
		// then
		assertEquals(SimulationStatus.SIMULATION_UNINITIALISED,
				stockExchange.getSimulationStatus());
	}

	@Test
	public void simulationShouldBeInitialised()
			throws DateTimeParseException, FileNotFoundException, IOException {
		// given
		Mockito.doNothing().when(dataProvider)
				.readDataFromFile(Mockito.anyString());
		Mockito.when(dataProvider.getEarliestDate())
				.thenReturn(LocalDate.parse("2013-01-02"));
		Mockito.when(dataProvider.getLatestDate())
				.thenReturn(LocalDate.parse("2013-12-30"));

		// when
		stockExchange.initialise("Test");

		// then
		Mockito.verify(dataProvider).readDataFromFile(Mockito.anyString());
		Mockito.verify(dataProvider).getCompaniesNames();
		Mockito.verify(dataProvider).getEarliestDate();
		Mockito.verify(dataProvider)
				.getStocksByDate(LocalDate.parse("2013-01-02"));
		Mockito.verify(dataProvider).getLatestDate();
		assertEquals(SimulationStatus.SIMULATION_NOT_FINISHED,
				stockExchange.getSimulationStatus());
	}

	@Test
	public void simulationStatusShouldBeSimulationFinishedAfterInitialisingForEqualDates()
			throws DateTimeParseException, FileNotFoundException, IOException {
		// given
		Mockito.doNothing().when(dataProvider)
				.readDataFromFile(Mockito.anyString());
		Mockito.when(dataProvider.getEarliestDate())
				.thenReturn(LocalDate.parse("2013-12-30"));
		Mockito.when(dataProvider.getLatestDate())
				.thenReturn(LocalDate.parse("2013-12-30"));

		// when
		stockExchange.initialise("Test");

		// then
		Mockito.verify(dataProvider).readDataFromFile(Mockito.anyString());
		assertEquals(SimulationStatus.SIMULATION_FINISHED,
				stockExchange.getSimulationStatus());
	}

	@Test
	public void simulationStatusShouldBeSimulationFinishedAfterInitialising()
			throws DateTimeParseException, FileNotFoundException, IOException {
		// given
		Mockito.doNothing().when(dataProvider)
				.readDataFromFile(Mockito.anyString());
		Mockito.when(dataProvider.getEarliestDate())
				.thenReturn(LocalDate.parse("2013-12-30"));
		Mockito.when(dataProvider.getLatestDate())
				.thenReturn(LocalDate.parse("2013-01-02"));

		// when
		stockExchange.initialise("Test");

		// then
		Mockito.verify(dataProvider).readDataFromFile(Mockito.anyString());
		assertEquals(SimulationStatus.SIMULATION_FINISHED,
				stockExchange.getSimulationStatus());
	}

	@Test
	public void nextDayShouldIncrementDateAndCallGetStockByDate()
			throws DateTimeParseException, FileNotFoundException, IOException {
		// given
		Mockito.doNothing().when(dataProvider)
				.readDataFromFile(Mockito.anyString());
		Mockito.when(dataProvider.getEarliestDate())
				.thenReturn(LocalDate.parse("2013-01-02"));
		Mockito.when(dataProvider.getLatestDate())
				.thenReturn(LocalDate.parse("2013-01-07"));
		Mockito.when(dataProvider.getStocksByDate(Mockito.any()))
				.thenReturn(Arrays
						.asList(new Stock("TPSA", LocalDate.parse("2013-01-03"),
								new BigDecimal("12.13"))));
		stockExchange.initialise("Test");

		// when
		stockExchange.nextDay();

		// then
		Mockito.verify(dataProvider)
				.getStocksByDate(LocalDate.parse("2013-01-03"));
		assertEquals(SimulationStatus.SIMULATION_NOT_FINISHED,
				stockExchange.getSimulationStatus());
	}

	@Test
	public void simulationStatusShouldBeSimulationFinishedAfter1Day()
			throws DateTimeParseException, FileNotFoundException, IOException {
		// given
		Mockito.doNothing().when(dataProvider)
				.readDataFromFile(Mockito.anyString());
		Mockito.when(dataProvider.getEarliestDate())
				.thenReturn(LocalDate.parse("2013-01-02"));
		Mockito.when(dataProvider.getLatestDate())
				.thenReturn(LocalDate.parse("2013-01-03"));
		stockExchange.initialise("Test");

		// when
		stockExchange.nextDay();

		// then
		assertEquals(SimulationStatus.SIMULATION_FINISHED,
				stockExchange.getSimulationStatus());
	}

	@Test
	public void simulationStatusShouldBeSimulationFinishedAfter7Days()
			throws DateTimeParseException, FileNotFoundException, IOException {
		// given
		Mockito.doNothing().when(dataProvider)
				.readDataFromFile(Mockito.anyString());
		Mockito.when(dataProvider.getEarliestDate())
				.thenReturn(LocalDate.parse("2013-01-02"));
		Mockito.when(dataProvider.getLatestDate())
				.thenReturn(LocalDate.parse("2013-01-09"));
		stockExchange.initialise("Test");

		// when
		for (int i = 0; i < 7; i++) {
			stockExchange.nextDay();
		}

		// then
		assertEquals(SimulationStatus.SIMULATION_FINISHED,
				stockExchange.getSimulationStatus());
	}
}
