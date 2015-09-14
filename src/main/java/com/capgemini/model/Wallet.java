package com.capgemini.model;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class Wallet implements Observer {

	private static final BigDecimal MONEY_IN_THE_BEGINNING = new BigDecimal(
			"10000.0");

	private BigDecimal money;
	private Map<Stock, Integer> stocksQuantities;

	public Wallet(StockExchange stockExchange) {
		this.money = MONEY_IN_THE_BEGINNING;
		this.stocksQuantities = new HashMap<Stock, Integer>();
		stockExchange.addObserver(this);
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Map<Stock, Integer> getStocksQuantities() {
		return stocksQuantities;
	}

	public void setStocksQuantities(Map<Stock, Integer> stocksQuantities) {
		this.stocksQuantities = stocksQuantities;
	}

	public void addMoney(BigDecimal money) {

		if (money == null) {
			return;
		}

		this.money = this.money.add(money);
	}

	public BigDecimal takeMoney(BigDecimal money) {

		if (money == null || this.money.compareTo(money) < 0) {
			return BigDecimal.ZERO;
		}

		this.money = this.money.subtract(money);
		return money;
	}

	public void addStocks(Map<Stock, Integer> stocks) {

		if (stocks == null || stocks.isEmpty()) {
			return;
		}

		for (Stock stock : stocks.keySet()) {

			Integer quantityBeforeAdding = stocksQuantities.get(stock);
			if (quantityBeforeAdding == null) {
				quantityBeforeAdding = 0;
			}

			Integer addedStockQuantity = stocks.get(stock);
			stocksQuantities.put(stock,
					quantityBeforeAdding + addedStockQuantity);
		}
	}

	public void removeStocks(Map<Stock, Integer> stocks) {

		if (stocks == null || stocks.isEmpty()) {
			return;
		}

		for (Stock stock : stocks.keySet()) {

			Integer quantityBeforeRemoving = stocksQuantities.get(stock);
			if (quantityBeforeRemoving == null) {
				return;
			}

			Integer removedStockQuantity = stocks.get(stock);
			stocksQuantities.put(stock,
					quantityBeforeRemoving - removedStockQuantity);
		}
	}

	public void updateStocks(List<Stock> currentStocks) {

		if (currentStocks == null || currentStocks.isEmpty()) {
			return;
		}

		Map<Stock, Integer> updatedStocksQuantities = new HashMap<>();

		for (Stock stock : stocksQuantities.keySet()) {

			Stock current = currentStocks.stream().filter(
					s -> s.getCompanyName().equals(stock.getCompanyName()))
					.findFirst().get();
			if (current == null) {
				return;
			}

			Integer quantity = stocksQuantities.get(current);
			updatedStocksQuantities.put(current, quantity);
		}
		
		stocksQuantities = updatedStocksQuantities;
	}

	public BigDecimal getTotalValue() {

		BigDecimal totalValue = money;

		for (Stock stock : stocksQuantities.keySet()) {
			BigDecimal price = stock.getPrice();
			Integer quantity = stocksQuantities.get(stock);
			totalValue = totalValue
					.add(price.multiply(new BigDecimal(quantity)));
		}

		return totalValue;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o == null || o.getClass() != StockExchange.class) {
			return;
		}

		List<Stock> updatedStocks = (List<Stock>) arg;
		updateStocks(updatedStocks);
	}
}
