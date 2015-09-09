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
	private List<SharePrice> sharePrices;
	private LocalDate currentDate;
	private Set<String> companiesNames;

	public StockExchange() {
		this.simulationStatus = SimulationStatus.SIMULATION_UNINITIALISED;
		this.dataProvider = new DataProviderImpl();
		sharePrices = null;
		currentDate = null;
		companiesNames = null;
	}

	public void initialise(String filepath)
			throws FileNotFoundException, IOException, DateTimeParseException {
		dataProvider.readDataFromFile(filepath);
		companiesNames = dataProvider.getCompaniesNames();
		currentDate = dataProvider.getEarliestDate();
		sharePrices = dataProvider.getSharePricesByDate(currentDate);

		if(currentDate.compareTo(dataProvider.getLatestDate()) >= 0) {
			simulationStatus = SimulationStatus.SIMULATION_FINISHED;
			return;
		}
		simulationStatus = SimulationStatus.SIMULATION_NOT_FINISHED;
	}
	
	public Set<String> getCompaniesNames() {
		return companiesNames;
	}
	
	public List<SharePrice> getSharesPrices() {
		return sharePrices;
	}
	
	public SimulationStatus getSimulationStatus() {
		return simulationStatus;
	}
	
	public SharePrice getShareByCompanyName(String companyName) {
		return sharePrices.stream().filter(sp -> sp.getCompanyName().equals(companyName)).findFirst().get();
	}
	
	public void nextDay() {
		if(simulationStatus == SimulationStatus.SIMULATION_NOT_FINISHED) {
			
			do {
				currentDate = currentDate.plusDays(1L);
				if(currentDate.compareTo(dataProvider.getLatestDate()) > 0) {
					simulationStatus = SimulationStatus.SIMULATION_FINISHED;
					break;
				}

				sharePrices = dataProvider.getSharePricesByDate(currentDate);
				
			} while (sharePrices == null || sharePrices.isEmpty() );
		}
	}
}
