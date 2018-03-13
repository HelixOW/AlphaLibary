/*
 *
 * Copyright (C) <2017>  <AlphaHelixDev>
 *
 *       This program is free software: you can redistribute it under the
 *       terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.alphahelix.alphalibary.minigame.voting;

import java.util.WeakHashMap;
import java.util.concurrent.ThreadLocalRandom;


public class VoteManager {
	
	private final WeakHashMap<VoteItem, Integer> votes = new WeakHashMap<>();
	
	public void addVote(VoteItem item) {
		if(votes.containsKey(item)) {
			votes.put(item, votes.get(item) + 1);
		} else {
			votes.put(item, 1);
		}
	}
	
	public void removeItem(VoteItem item) {
		if(votes.containsKey(item)) {
			if(votes.get(item) == 1) {
				votes.remove(item);
			} else {
				votes.put(item, votes.get(item) - 1);
			}
		}
	}
	
	public int getVotes(VoteItem item) {
		if(votes.containsKey(item)) {
			return votes.get(item);
		}
		return 0;
	}
	
	public VoteItem getHighestVotedItem() {
		VoteItem winner = null;
		
		for(VoteItem items : votes.keySet()) {
			if(winner == null) {
				winner = items;
			} else {
				if(votes.get(items) > votes.get(winner)) winner = items;
			}
		}
		
		return winner;
	}
	
	public VoteItem getRandomItem() {
		return votes.keySet().toArray(new VoteItem[votes.keySet().size()])[ThreadLocalRandom.current().nextInt(votes.keySet().size())];
	}
}
