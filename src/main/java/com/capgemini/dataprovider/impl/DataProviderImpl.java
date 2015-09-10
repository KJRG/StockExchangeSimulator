package com.capgemini.dataprovider.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.capgemini.dataprovider.DataProvider;
import com.capgemini.model.Stock;

public class DataProviderImpl implements DataProvider {

	private List<Stock> stocks;
	private Comparator<Stock> stockDateComparator;

	public DataProviderImpl() {
		stocks = new ArrayList<>();

		stockDateComparator = new Comparator<Stock>() {

			@Override
			public int compare(Stock s1, Stock s2) {
				return s1.getDate().compareTo(s2.getDate());
			}
		};
	}

	@Override
	public void readDataFromFile(String filepath)
			throws FileNotFoundException, IOException, DateTimeParseException {

		BufferedReader reader = null;
		String separator = ",";
		String line = "";
		String companyName = "";
		BigDecimal price = null;
		LocalDate date = null;
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd");

		try {
			reader = new BufferedReader(new FileReader(filepath));

			while ((line = reader.readLine()) != null) {
				String[] stockData = line.split(separator);

				companyName = stockData[0];

				try {
					date = LocalDate.parse(stockData[1], dateFormat);
				} catch (DateTimeParseException e) {
					throw e;
				}

				price = new BigDecimal(stockData[2]);

				stocks.add(new Stock(companyName, date, price));
			}
		} catch (FileNotFoundException e) {
			throw e;
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw e;
				}
			}
		}
	}

	@Override
	public Set<String> getCompaniesNames() {
		return stocks.stream().map(Stock::getCompanyName)
				.collect(Collectors.toSet());
	}

	@Override
	public LocalDate getEarliestDate() {
		return Collections.min(stocks, stockDateComparator).getDate();
	}

	@Override
	public LocalDate getLatestDate() {
		return Collections.max(stocks, stockDateComparator).getDate();
	}

	public List<Stock> getStocks() {
		return stocks;
	}

	@Override
	public List<Stock> getStocksByDate(LocalDate date) {
		return stocks.stream().filter(s -> s.getDate().equals(date))
				.collect(Collectors.toList());
	}
}
