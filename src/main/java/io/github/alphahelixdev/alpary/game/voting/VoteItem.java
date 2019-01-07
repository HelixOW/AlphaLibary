package io.github.alphahelixdev.alpary.game.voting;

import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class VoteItem<T> {

    private final String name;
    private final ItemStack icon;
    private final T voteFor;

    public VoteItem(String name, ItemStack icon, T voteFor) {
        this.name = name;
        this.icon = icon;
        this.voteFor = voteFor;
    }

    public String getName() {
        return name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public T getVoteFor() {
        return voteFor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteItem<?> voteItem = (VoteItem<?>) o;
        return Objects.equals(name, voteItem.name) &&
                Objects.equals(icon, voteItem.icon) &&
                Objects.equals(voteFor, voteItem.voteFor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, icon, voteFor);
    }

    @Override
    public String toString() {
        return "VoteItem{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", voteFor=" + voteFor +
                '}';
    }
}
