/*
 *     Copyright (C) <2016>  <AlphaHelixDev>
 *
 *     This program is free software: you can redistribute it under the
 *     terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.alphahelix.alphalibary.scoreboard;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import de.alphahelix.alphalibary.AlphaLibary;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleScoreboard {

    private static final List<ChatColor> colors = Arrays.asList(ChatColor.values());
    private final List<BoardLine> boardLines = new ArrayList<>();
    private Scoreboard scoreboard = null;
    private Objective objective = null;
    private AlphaLibary api;

    /**
     * Creates a new {@link SimpleScoreboard} out of the {@link Scoreboard} of the {@link Player}
     *
     * @param p                      the {@link Player} who has a {@link Scoreboard}
     * @param scoreboardRegisterName the name of the {@link Objective} to register on the {@link Scoreboard}
     */
    public SimpleScoreboard(Player p, String scoreboardRegisterName) {
        this.api = AlphaLibary.getInstance();
        scoreboard = p.getScoreboard();
        objective = scoreboard.getObjective(scoreboardRegisterName);

        for (int i = 0; i < colors.size(); i++) {
            final ChatColor color = colors.get(i);
            final Team team = scoreboard.getTeam("line" + i);
            boardLines.add(new BoardLine(color, i, team));
        }
    }

    /**
     * Creates a new {@link SimpleScoreboard}
     *
     * @param scoreboardRegisterName the name of the {@link Objective} to register on the {@link Scoreboard}
     * @param displayName            the title of the {@link Scoreboard}
     * @param iden                   the {@link String} where the {@link Scoreboard} is split into left and right (should only be used once per line)
     * @param lines                  the amount of lines the {@link Scoreboard} should have (Highest = 16)
     */
    public SimpleScoreboard(String scoreboardRegisterName, String displayName, String iden, String... lines) {
        Validate.isTrue(lines.length < colors.size(), "Too many lines!");

        this.api = AlphaLibary.getInstance();

        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective(scoreboardRegisterName, "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(displayName);

        for (int i = 0; i < colors.size(); i++) {
            final ChatColor color = colors.get(i);
            final Team team = scoreboard.registerNewTeam("line" + i);

            team.addEntry(color.toString());
            boardLines.add(new BoardLine(color, i, team));
        }

        for (int i = 0; i < lines.length; i++)
            setValue(i, lines[i], iden);
    }

    private BoardLine getBoardLine(final int line) {
        for (BoardLine lines : boardLines) {
            if (lines.getLine() == line) {
                return lines;
            }
        }
        return null;
    }

    /**
     * Add a new line to the {@link Scoreboard}
     *
     * @param line  the number of the line
     * @param value the text at this line
     * @param iden  the {@link String} where the {@link Scoreboard} is split into left and right (should only be used once per line)
     */
    public void setValue(int line, String value, final String iden) {
        if (!value.contains(iden)) {
            Iterable<String> res = Splitter.fixedLength(value.length() / 2).split(value);
            String[] text = Iterables.toArray(res, String.class);
            BoardLine bl = getBoardLine(line);
            String cc = ChatColor.getLastColors(text[0]);

            assert bl != null;
            objective.getScore(bl.getColor().toString()).setScore(line);

            bl.getTeam().setPrefix(text[0]);
            bl.getTeam().setSuffix(text[1].startsWith("?") ? text[1] : cc + text[1]);
        } else {
            String[] text = value.split(iden);
            BoardLine bl = getBoardLine(line);

            String firstLeftColors = getFirstColors(text[0]);
            String lastRightColors = ChatColor.getLastColors(text[1]);

            String leftText = text[0].replace(firstLeftColors, "");
            String rightText = text[1].replace(lastRightColors, "");

            if (objective == null) return;
            assert bl != null;
            if (objective.getScore(bl.getColor().toString()) == null) return;
            objective.getScore(bl.getColor().toString()).setScore(line);

            if (leftText.length() > 12) {
                if (rightText.length() > 12) {

                    Scroller left = new Scroller(leftText, 10, 3, '?');
                    Scroller right = new Scroller(rightText, 8, 3, '?');

                    if ((leftText.length() % 2) == 0) {
                        left = new Scroller(leftText, 10, 2, '?');
                    }

                    if ((rightText.length() % 2) == 0) {
                        right = new Scroller(rightText, 8, 2, '?');
                    }

                    assert bl != null;
                    bl.getTeam().setPrefix(firstLeftColors + left.next());
                    bl.getTeam().setSuffix(iden + lastRightColors + right.next());

                    Scroller finalLeft = left;
                    Scroller finalRight = right;

                    new BukkitRunnable() {
                        public void run() {
                            bl.getTeam().setPrefix(firstLeftColors + finalLeft.next());
                            bl.getTeam().setSuffix(iden + lastRightColors + finalRight.next());
                        }
                    }.runTaskTimerAsynchronously(getApi(), 0, 10);
                }

                // When only left is too long
                else {
                    Scroller left = new Scroller(leftText, 10, 3, '?');

                    if ((leftText.length() % 2) == 0) {
                        left = new Scroller(leftText, 10, 2, '?');
                    }

                    assert bl != null;
                    bl.getTeam().setPrefix(firstLeftColors + left.next());
                    bl.getTeam().setSuffix(iden + lastRightColors + rightText);

                    Scroller finalLeft = left;

                    new BukkitRunnable() {
                        public void run() {
                            bl.getTeam().setPrefix(firstLeftColors + finalLeft.next());
                        }
                    }.runTaskTimerAsynchronously(getApi(), 0, 10);
                }
            }

            // When left is correct
            else {
                if (rightText.length() > 12) {
                    Scroller right = new Scroller(rightText, 8, 3, '?');

                    if ((rightText.length() % 2) == 0) {
                        right = new Scroller(rightText, 8, 2, '?');
                    }

                    assert bl != null;
                    bl.getTeam().setPrefix(firstLeftColors + leftText);
                    bl.getTeam().setSuffix(iden + lastRightColors + right.next());

                    Scroller finalRight = right;

                    new BukkitRunnable() {
                        public void run() {
                            bl.getTeam().setSuffix(iden + lastRightColors + finalRight.next());
                        }
                    }.runTaskTimerAsynchronously(getApi(), 0, 10);
                }

                // When everything is perfect
                else {
                    assert bl != null;
                    bl.getTeam().setPrefix(firstLeftColors + text[0]);
                    bl.getTeam().setSuffix(iden + lastRightColors + text[1]);
                }
            }
        }
    }

    /**
     * Updates a line inside the {@link Scoreboard}
     *
     * @param line       the number of the line to update
     * @param value      the new text of this line
     * @param identifier the {@link String} where the {@link Scoreboard} is split into left and right (should only be used once per line)
     */
    public void updateValue(final int line, final String value, final String identifier) {
        if (!value.contains(identifier))
            return;

        String[] text = value.split(identifier);
        BoardLine bl = getBoardLine(line);

        String firstLeftColors = getFirstColors(text[0]);
        String lastRightColors = ChatColor.getLastColors(text[1]);

        String leftText = text[0].replace(firstLeftColors, "");
        String rightText = text[1].replace(lastRightColors, "");

        if (leftText.length() > 12) {
            if (rightText.length() > 12) {

                Scroller left = new Scroller(leftText, 10, 3, '?');
                Scroller right = new Scroller(rightText, 8, 3, '?');

                if ((leftText.length() % 2) == 0) {
                    left = new Scroller(leftText, 10, 2, '?');
                }

                if ((rightText.length() % 2) == 0) {
                    right = new Scroller(rightText, 8, 2, '?');
                }

                assert bl != null;
                bl.getTeam().setPrefix(firstLeftColors + left.next());
                bl.getTeam().setSuffix(identifier + lastRightColors + right.next());

                Scroller finalLeft = left;
                Scroller finalRight = right;

                new BukkitRunnable() {
                    public void run() {
                        bl.getTeam().setPrefix(firstLeftColors + finalLeft.next());
                        bl.getTeam().setSuffix(identifier + lastRightColors + finalRight.next());
                    }
                }.runTaskTimerAsynchronously(getApi(), 0, 10);
            }

            // When only left is too long
            else {
                Scroller left = new Scroller(leftText, 10, 3, '?');

                if ((leftText.length() % 2) == 0) {
                    left = new Scroller(leftText, 10, 2, '?');
                }

                assert bl != null;
                bl.getTeam().setPrefix(firstLeftColors + left.next());
                bl.getTeam().setSuffix(identifier + lastRightColors + rightText);

                Scroller finalLeft = left;

                new BukkitRunnable() {
                    public void run() {
                        bl.getTeam().setPrefix(firstLeftColors + finalLeft.next());
                    }
                }.runTaskTimerAsynchronously(getApi(), 0, 10);
            }
        }

        // When left is correct
        else {
            if (rightText.length() > 12) {
                Scroller right = new Scroller(rightText, 8, 3, '?');

                if ((rightText.length() % 2) == 0) {
                    right = new Scroller(rightText, 8, 2, '?');
                }

                assert bl != null;
                bl.getTeam().setPrefix(firstLeftColors + leftText);
                bl.getTeam().setSuffix(identifier + lastRightColors + right.next());

                Scroller finalRight = right;

                new BukkitRunnable() {
                    public void run() {
                        bl.getTeam().setSuffix(identifier + lastRightColors + finalRight.next());
                    }
                }.runTaskTimerAsynchronously(getApi(), 0, 10);
            }

            // When everything is perfect
            else {
                assert bl != null;
                bl.getTeam().setPrefix(firstLeftColors + text[0]);
                bl.getTeam().setSuffix(identifier + lastRightColors + text[1]);
            }
        }
    }

    public void removeLine(int line) {
        final BoardLine boardLine = getBoardLine(line);
        Validate.notNull(boardLine, "Unable to find BoardLine with index of " + line + ".");
        scoreboard.resetScores(boardLine.getColor().toString());
    }

    public Scoreboard buildScoreboard() {
        return scoreboard;
    }

    public AlphaLibary getApi() {
        return api;
    }

    private static String getFirstColors(String input) {
        StringBuilder result = new StringBuilder();
        int length = input.length();

        for (int index = 0; index < length; index++) {
            char section = input.charAt(index);

            if (section == ChatColor.COLOR_CHAR && index < length - 1) {
                char c = input.charAt(index + 1);
                ChatColor color = ChatColor.getByChar(c);

                if (color != null) {
                    result.insert(0, color.toString());

                    if (color.isColor() || color.equals(ChatColor.RESET)) {
                        break;
                    }
                }
            }
        }

        return result.toString();
    }
}

class BoardLine {

    private final ChatColor color;
    private final int line;
    private final Team team;

    public BoardLine(ChatColor color, int line, Team team) {
        this.color = color;
        this.line = line;
        this.team = team;
    }

    public ChatColor getColor() {
        return color;
    }

    public int getLine() {
        return line;
    }

    public Team getTeam() {
        return team;
    }

}
