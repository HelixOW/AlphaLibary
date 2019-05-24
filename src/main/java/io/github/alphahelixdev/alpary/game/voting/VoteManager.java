package io.github.alphahelixdev.alpary.game.voting;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
@ToString
public class VoteManager {
	
	private final Map<VoteItem<?>, Integer> votes = new HashMap<>();
	
	public VoteManager addVote(VoteItem item) {
		votes.put(item, votes.getOrDefault(item, 0) + 1);
		return this;
	}
	
	public VoteManager removeVote(VoteItem item) {
		votes.put(item, votes.getOrDefault(item, 0) - 1);
		return this;
	}
	
	public int getVotes(VoteItem item) {
		return votes.getOrDefault(item, 0);
	}
	
	public VoteItem<?> mostVoted() {
		return votes.entrySet().stream().max((o1, o2) -> o2.getValue() - o1.getValue()).orElse(new Map.Entry<VoteItem<?>, Integer>() {
			@Override
			public VoteItem<?> getKey() {
				return random();
			}
			
			@Override
			public Integer getValue() {
				return 0;
			}
			
			@Override
			public Integer setValue(Integer value) {
				return 0;
			}
		}).getKey();
	}
	
	public VoteItem<?> random() {
		return votes.keySet().stream().findAny().orElseThrow(IllegalStateException::new);
	}
}
