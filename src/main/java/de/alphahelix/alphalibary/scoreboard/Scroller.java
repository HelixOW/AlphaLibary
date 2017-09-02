package de.alphahelix.alphalibary.scoreboard;

import com.google.common.base.Objects;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Scroller {
    private static final char COLOUR_CHAR = '?';
    private int position;
    private List<String> list;
    private ChatColor colour = ChatColor.RESET;

    /**
     * @param message      The {@link String} to scroll
     * @param width        The width of the window to scroll across
     * @param spaceBetween The amount of spaces between each repetition
     * @param colourChar   The colour code character you're using
     */
    public Scroller(String message, int width, int spaceBetween, char colourChar) {
        list = new ArrayList<>();

        if (message.length() < width) {
            StringBuilder sb = new StringBuilder(message);
            while (sb.length() < width)
                sb.append(" ");
            message = sb.toString();
        }

        width -= 2;

        if (width < 1)
            width = 1;
        if (spaceBetween < 0)
            spaceBetween = 0;

        if (colourChar != '?')
            message = ChatColor.translateAlternateColorCodes(colourChar, message);

        for (int i = 0; i < message.length() - width; i++)
            list.add(message.substring(i, i + width));

        StringBuilder space = new StringBuilder();
        for (int i = 0; i < spaceBetween; ++i) {
            list.add(message.substring(message.length() - width + (i > width ? width : i), message.length()) + space);
            if (space.length() < width)
                space.append(" ");
        }

        for (int i = 0; i < width - spaceBetween; ++i)
            list.add(message.substring(message.length() - width + spaceBetween + i, message.length()) + space
                    + message.substring(0, i));

        for (int i = 0; i < spaceBetween; i++) {
            if (i > space.length())
                break;
            list.add(space.substring(0, space.length() - i)
                    + message.substring(0, width - (spaceBetween > width ? width : spaceBetween) + i));
        }
    }

    /**
     * @return Gets the next {@link String} to display
     */
    public String next() {
        StringBuilder sb = getNext();
        if (sb.charAt(sb.length() - 1) == COLOUR_CHAR)
            sb.setCharAt(sb.length() - 1, ' ');

        if (sb.charAt(0) == COLOUR_CHAR) {
            ChatColor c = ChatColor.getByChar(sb.charAt(1));
            if (c != null) {
                colour = c;
                sb = getNext();
                if (sb.charAt(0) != ' ')
                    sb.setCharAt(0, ' ');
            }
        }

        return sb.toString();

    }

    private StringBuilder getNext() {
        return new StringBuilder(list.get(position++ % list.size()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scroller scroller = (Scroller) o;
        return position == scroller.position &&
                Objects.equal(list, scroller.list) &&
                colour == scroller.colour;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(position, list, colour);
    }

    @Override
    public String toString() {
        return "Scroller{" +
                "position=" + position +
                ", list=" + list +
                ", colour=" + colour +
                '}';
    }
}
