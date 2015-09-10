package com.capgemini.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

import com.capgemini.dataprovider.DataProvider;
import com.capgemini.dataprovider.impl.DataProviderImpl;

public class StockExchange {

	private SimulationStatus simulationStatus;
	private DataProvider dataProvider;
	private List<Stock> stocks;
	private LocalDate currentDate;
	private Set<String> companiesNames;

	public StockExchange() {
		this.simulationStatus = SimulationStatus.SIMULATION_UNINITIALISED;
		this.dataProvider = new DataProviderImpl();
		stocks = null;
		currentDate = null;
		companiesNames = null;
	}

	public void initialise(String filepath)
			throws FileNotFoundException, IOException, DateTimeParseException {
		dataProvider.readDataFromFile(filepath);
		companiesNames = dataProvider.getCompaniesNames();
		currentDate = dataProvider.getEarliestDate();
		stocks = dataProvider.getStocksByDate(currentDate);

		if(currentDate.compareTo(dataProvider.getLatestDate()) >= 0) {
			simulationStatus = SimulationStatus.SIMULATION_FINISHED;
			return;
		}
		simulationStatus = SimulationStatus.SIMULATION_NOT_FINISHED;
	}
	
	public Set<String> getCompaniesNames() {
		return companiesNames;
	}
	
	public List<Stock> getStocks() {
		return stocks;
	}
	
	public SimulationStatus getSimulationStatus() {
		return simulationStatus;
	}
	
	public Stock getStockByCompanyName(String companyName) {
		return stocks.stream().filter(s -> s.getCompanyName().equals(companyName)).findFirst().get();
	}
	
	public void nextDay() {
		if(simulationStatus == SimulationStatus.SIMULATION_NOT_FINISHED) {
			
			do {
				currentDate = currentDate.plusDays(1L);
				if(currentDate.compareTo(dataProvider.getLatestDate()) > 0) {
					simulationStatus = SimulationStatus.SIMULATION_FINISHED;
					break;
				}

				stocks = dataProvider.getStocksByDate(currentDate);
				
			} while (stocks == null || stocks.isEmpty() );
		}
	}
}
