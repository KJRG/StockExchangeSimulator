package com.capgemini.StockExchangeSimulator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeParseException;

import com.capgemini.model.BrokersOffice;
import com.capgemini.model.Player;
import com.capgemini.model.SimulationStatus;
import com.capgemini.model.StockExchange;
import com.capgemini.model.strategy.impl.RandomInvestingStrategy;

public class Simulator {
	public static void main(String[] args) {
		
		String filepath = "C:\\Users\\kgalka\\Desktop\\ZadanieGielda\\ZadanieGielda\\dane.csv";
		StockExchange stockExchange = new StockExchange();
		BrokersOffice brokersOffice = new BrokersOffice(stockExchange);
		Player player = new Player(new RandomInvestingStrategy(), brokersOffice);
		
		try {
			stockExchange.initialise(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(DateTimeParseException e) {
			e.printStackTrace();
		}
		
		System.out.println("Simulation initialised");
		System.out.println("Player\'s wallet value: " + player.getWallet().getTotalValue() + "PLN");
		System.out.println("Player\'s money: " + player.getWallet().getMoney() + "PLN");
		System.out.println("\nStarting simulation\n");
		
		int i = 1;
		while (stockExchange
				.getSimulationStatus() == SimulationStatus.SIMULATION_NOT_FINISHED) {
			

			player.play();
			stockExchange.nextDay();

			System.out.println("Iteration no. " + i);
			System.out.println("Player\'s wallet value: " + player.getWallet().getTotalValue() + "PLN");
			System.out.println("Player\'s money: " + player.getWallet().getMoney() + "PLN");
			System.out.println();
			
			i++;
		}
		
		System.out.println("Simulation finished");
		System.out.println("Player\'s wallet value: " + player.getWallet().getTotalValue() + "PLN");
		System.out.println("Player\'s money: " + player.getWallet().getMoney() + "PLN");
	}
}
