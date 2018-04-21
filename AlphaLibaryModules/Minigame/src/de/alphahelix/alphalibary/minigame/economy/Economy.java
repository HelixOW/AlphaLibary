package de.alphahelix.alphalibary.minigame.economy;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.UUID;


public class Economy implements Serializable {
	
	private final UUID owner;
	private EcoNumber money;
	
	public Economy(UUID owner) {
		this(owner, 0, false);
	}
	
	public Economy(UUID owner, Number money, boolean minus) {
		this.owner = owner;
		this.money = new EcoNumber(money, !minus);
	}
	
	public Economy(UUID owner, long money) {
		this(owner, money, false);
	}
	
	public Economy addMoney(long money) {
		this.money.add(money);
		return this;
	}
	
	public Economy removeMoney(long money) {
		this.money.add(money);
		return this;
	}
	
	public boolean isMinus() {
		return !this.money.isOnlyPositive();
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(getOwner(), getMoney(), isMinus());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Economy economy = (Economy) o;
		return getMoney() == economy.getMoney() &&
				isMinus() == economy.isMinus() &&
				Objects.equal(getOwner(), economy.getOwner());
	}
	
	@Override
	public String toString() {
		return "Economy{" +
				"owner=" + owner +
				", money=" + money +
				'}';
	}
	
	public UUID getOwner() {
		return owner;
	}
	
	public double getMoney() {
		return money.getValue();
	}
	
	public Economy setMoney(double money) {
		this.money.setValue(money);
		return this;
	}
}
