package io.github.alphahelixdev.alpary.game;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class GameCurrency {
	
	private final UUID owner;
	private float amount;
	private boolean minus;
	
	public GameCurrency(UUID owner, float amount) {
		this(owner, amount, false);
	}
	
	public GameCurrency add(float amount) {
		this.amount += amount;
		return this;
	}
	
	public GameCurrency remove(float amount) {
		if (this.amount - amount < 0 && !minus)
			this.amount = 0.0f;
		else
			this.amount -= amount;
		return this;
	}
}
