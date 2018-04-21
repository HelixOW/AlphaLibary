package de.alphahelix.alphalibary.minigame.economy;

import java.util.Objects;

public class EcoNumber {
	
	private double value;
	private boolean onlyPositive;
	
	public EcoNumber(Number value) {
		this(value, true);
	}
	
	public EcoNumber(Number value, boolean onlyPositive) {
		this.value = value.doubleValue();
		this.onlyPositive = onlyPositive;
	}
	
	public EcoNumber add(Number n) {
		value += n.doubleValue();
		return this;
	}
	
	public EcoNumber subtract(Number n) {
		if(this.value - n.doubleValue() < 0 && onlyPositive)
			this.value = 0.0;
		else
			this.value -= n.doubleValue();
		return this;
	}
	
	public double getValue() {
		return value;
	}
	
	public EcoNumber setValue(double value) {
		this.value = value;
		return this;
	}
	
	public boolean isOnlyPositive() {
		return onlyPositive;
	}
	
	public EcoNumber setOnlyPositive(boolean onlyPositive) {
		this.onlyPositive = onlyPositive;
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(value, onlyPositive);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		EcoNumber ecoNumber = (EcoNumber) o;
		return Double.compare(ecoNumber.value, value) == 0 &&
				onlyPositive == ecoNumber.onlyPositive;
	}
	
	@Override
	public String toString() {
		return "EcoNumber{" +
				"value=" + value +
				", onlyPositive=" + onlyPositive +
				'}';
	}
}
