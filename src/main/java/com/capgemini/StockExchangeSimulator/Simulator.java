package com.capgemini.StockExchangeSimulator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeParseException;

import com.capgemini.model.BrokerageFirm;
import com.capgemini.model.Investor;
import com.capgemini.model.SimulationStatus;
import com.capgemini.model.StockExchange;
import com.capgemini.model.strategy.impl.RandomInvestingStrategy;
import com.capgemini.model.strategy.impl.SellExpensiveBuyCheapStrategy;
import com.capgemini.model.strategy.impl.TransactionsBasedOnPricesFromYesterdayStrategy;

public class Simulator {
	public static void main(String[] args) {

		String filepath = "C:\\Users\\kgalka\\Desktop\\ZadanieGielda\\ZadanieGielda\\dane.csv";
		StockExchange stockExchange = new StockExchange();
		BrokerageFirm brokerageFirm = new BrokerageFirm(stockExchange);
		Investor investor = new Investor(new RandomInvestingStrategy(),
		brokerageFirm, stockExchange);
//		Investor investor = new Investor(new SellExpensiveBuyCheapStrategy(),
//		brokerageFirm);
//		Investor investor = new Investor(
//				new TransactionsBasedOnPricesFromYesterdayStrategy(),
//				brokerageFirm);

		try {
			stockExchange.initialise(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DateTimeParseException e) {
			e.printStackTrace();
		}

		System.out.println("Simulation initialised");
		System.out.println("Investor\'s wallet value: "
				+ investor.getWallet().getTotalValue() + "PLN");
		System.out.println("Investor\'s money: "
				+ investor.getWallet().getMoney() + "PLN");
		System.out.println("\nStarting simulation\n");

		int i = 1;
		while (stockExchange
				.getSimulationStatus() == SimulationStatus.SIMULATION_NOT_FINISHED) {

			investor.invest();
			stockExchange.nextDay();

			System.out.println("Iteration no. " + i);
			System.out.println("Investor\'s wallet value: "
					+ investor.getWallet().getTotalValue() + "PLN");
			System.out.println("Investor\'s money: "
					+ investor.getWallet().getMoney() + "PLN");
			System.out.println();

			i++;
		}

		System.out.println("Simulation finished");
		System.out.println("Investor\'s wallet value: "
				+ investor.getWallet().getTotalValue() + "PLN");
		System.out.println("Investor\'s money: "
				+ investor.getWallet().getMoney() + "PLN");
	}
}
