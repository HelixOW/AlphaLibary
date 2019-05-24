package io.github.alphahelixdev.alpary.utilities;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.ChatColor;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class ProgressBar {
	
	private final int maximum, total;
	private final char barSymbol;
	private final ChatColor completed, uncompleted;
	private final BarItem progress;
	
	public ProgressBar(int maximum, int total, char barSymbol, BarItem progress) {
		this(maximum, total, barSymbol, ChatColor.GREEN, ChatColor.GRAY, progress);
	}
	
	public String display() {
		float percent = getProgress().current() / getMaximum();
		int progress = (int) (getTotal() * percent);
		
		return Strings.repeat("" + getCompleted() + getBarSymbol(), progress)
				+ Strings.repeat("" + getUncompleted() + getBarSymbol(), getTotal() - progress);
	}
}
