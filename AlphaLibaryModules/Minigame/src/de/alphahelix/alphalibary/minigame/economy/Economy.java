package de.alphahelix.alphalibary.minigame.economy;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.UUID;


public class Economy implements Serializable {
	
	private final UUID owner;
	private long money;
	private boolean minus = false;
	
	public Economy(UUID owner) {
		this(owner, 0, false);
	}
	
	public Economy(UUID owner, long money, boolean minus) {
		this.owner = owner;
		this.money = money;
		this.minus = minus;
	}
	
	public Economy(UUID owner, long money) {
		this(owner, money, false);
	}
	
	public Economy addMoney(long money) {
		this.money += money;
		return this;
	}
	
	public Economy removeMoney(long money) {
		if(this.money - money < 0 && !isMinus())
			this.money = 0;
		else
			this.money -= money;
		return this;
	}
	
	public boolean isMinus() {
		return minus;
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
				", minus=" + minus +
				'}';
	}
	
	public UUID getOwner() {
		return owner;
	}
	
	public long getMoney() {
		return money;
	}
	
	public Economy setMoney(long money) {
		this.money = money;
		return this;
	}
}
