package com.capgemini.StockExchangeSimulator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import com.capgemini.model.BrokersOffice;
import com.capgemini.model.Player;
import com.capgemini.model.SimulationStatus;
import com.capgemini.model.StockExchange;
import com.capgemini.model.strategy.impl.RandomPlayingStrategy;

public class App {
	public static void main(String[] args) {
		
		String filepath = "C:\\Users\\kgalka\\Desktop\\ZadanieGielda\\ZadanieGielda\\dane.csv";
		StockExchange stockExchange = new StockExchange();
		BrokersOffice brokersOffice = new BrokersOffice(stockExchange);
		Player player = new Player(new RandomPlayingStrategy(), brokersOffice);
		
		try {
			stockExchange.initialise(filepath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int i = 0;
		while (stockExchange
				.getSimulationStatus() == SimulationStatus.SIMULATION_NOT_FINISHED) {
			
			System.out.println("Iteration no. " + i);
			System.out.println("Player\'s wallet value: " + player.getWallet().getTotalValue() + "PLN");
			System.out.println("Player\'s money: " + player.getWallet().getMoney());
			System.out.println();

			player.play();
			stockExchange.nextDay();
			i++;
		}
		
		System.out.println("Player\'s wallet value: " + player.getWallet().getTotalValue() + "PLN");
		System.out.println("Player\'s money: " + player.getWallet().getMoney());
	}
}
