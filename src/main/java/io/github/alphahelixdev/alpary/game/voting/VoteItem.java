package io.github.alphahelixdev.alpary.game.voting;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.inventory.ItemStack;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class VoteItem<T> {

    private final String name;
    private final ItemStack icon;
    private final T voteFor;

}
