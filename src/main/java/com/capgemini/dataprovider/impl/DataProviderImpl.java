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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.capgemini.dataprovider.DataProvider;
import com.capgemini.model.SharePrice;

public class DataProviderImpl implements DataProvider {

	private List<SharePrice> sharePrices;
	private Comparator<SharePrice> sharePriceDateComparator;

	public DataProviderImpl() {
		sharePrices = new ArrayList<>();
		
		sharePriceDateComparator = new Comparator<SharePrice>() {
			
			@Override
			public int compare(SharePrice o1, SharePrice o2) {
				return o1.getDate().compareTo(o2.getDate());
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
				String[] shareData = line.split(separator);
				
				companyName = shareData[0];
				
				try {
					date = LocalDate.parse(shareData[1], dateFormat);
				}
				catch(DateTimeParseException e) {
					throw e;
				}
				
				price = new BigDecimal(shareData[2]);
				
				sharePrices.add(new SharePrice(companyName, date, price));
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
		Set<String> companiesNames = new HashSet<>();
		
		for(SharePrice sp : sharePrices) {
			companiesNames.add(sp.getCompanyName());
		}
		
		return companiesNames;
	}

	@Override
	public LocalDate getEarliestDate() {
		return Collections.min(sharePrices, sharePriceDateComparator).getDate();
	}

	@Override
	public LocalDate getLatestDate() {
		return Collections.max(sharePrices, sharePriceDateComparator).getDate();
	}

	public List<SharePrice> getSharePrices() {
		return sharePrices;
	}

	@Override
	public List<SharePrice> getSharePricesByDate(LocalDate date) {
		return sharePrices.stream().filter(p -> p.getDate().equals(date)).collect(Collectors.toList());
	}

}
