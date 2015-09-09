package com.capgemini.model;

import java.util.Map;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class Wallet {

	private static final BigDecimal MONEY_IN_THE_BEGINNING = new BigDecimal("10000.0");

	private BigDecimal money;
	private Map<SharePrice, Integer> sharesQuantities;

	public Wallet() {
		this.money = MONEY_IN_THE_BEGINNING;
		this.sharesQuantities = new HashMap<SharePrice, Integer>();
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Map<SharePrice, Integer> getSharesQuantities() {
		return sharesQuantities;
	}

	public void setSharesQuantities(Map<SharePrice, Integer> sharesQuantities) {
		this.sharesQuantities = sharesQuantities;
	}
	
	public void addMoney(BigDecimal money) {
		this.money = this.money.add(money);
	}
	
	public BigDecimal takeMoney(BigDecimal money) {
		this.money = this.money.subtract(money);
		return money;
	}
	
	public void addShares(Map<SharePrice, Integer> shares) {
			for(SharePrice sp : shares.keySet()) {
			
			Integer quantityBeforeAdding = sharesQuantities.get(sp);
			if(quantityBeforeAdding == null) {
				quantityBeforeAdding = 0;
			}

			Integer addedShareQuantity = shares.get(sp);
			sharesQuantities.put(sp, quantityBeforeAdding + addedShareQuantity);
		}
	}
	
	public void removeShares(Map<SharePrice, Integer> shares) {
		for(SharePrice sp : shares.keySet()) {
			
			Integer quantityBeforeRemoving = sharesQuantities.get(sp);
			if(quantityBeforeRemoving == null) {
				return;
			}
			
			Integer removedShareQuantity = shares.get(sp);
			sharesQuantities.put(sp, quantityBeforeRemoving - removedShareQuantity);
		}
	}
	
	public void updateSharePrices(List<SharePrice> currentSharePrices) {
		
		for (SharePrice sharePrice : sharesQuantities.keySet()) {
			
			BigDecimal price = currentSharePrices.stream()
					.filter(sp -> sp.getCompanyName()
							.equals(sharePrice.getCompanyName()))
					.findFirst().get().getPrice();
			
			sharePrice.setPrice(price);
		}
	}

	public BigDecimal getTotalValue() {

		BigDecimal totalValue = money;

		for (SharePrice sharePrice : sharesQuantities.keySet()) {
			BigDecimal price = sharePrice.getPrice();
			Integer quantity = sharesQuantities.get(sharePrice);
			totalValue = totalValue.add(price.multiply(new BigDecimal(quantity)));
		}

		return totalValue;
	}
}
