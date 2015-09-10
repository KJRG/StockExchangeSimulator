package com.capgemini.dataprovider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

import com.capgemini.model.Stock;

public interface DataProvider {

	void readDataFromFile(String filepath)
			throws FileNotFoundException, IOException, DateTimeParseException;
	Set<String> getCompaniesNames();
	LocalDate getEarliestDate();
	LocalDate getLatestDate();
	List<Stock> getStocks();
	List<Stock> getStocksByDate(LocalDate date);
}
