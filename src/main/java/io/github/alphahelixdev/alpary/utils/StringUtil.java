package io.github.alphahelixdev.alpary.utils;

import com.google.common.base.Strings;
import org.bukkit.ChatColor;

public class StringUtil extends io.github.alphahelixdev.helius.utils.StringUtil {

    public String getProgessBar(float current, int maximum, int total, char symbol, ChatColor completed, ChatColor uncompleted) {
        float percent = current / maximum;
        int progress = (int) (total * percent);

        return Strings.repeat("" + completed + symbol, progress)
                + Strings.repeat("" + uncompleted + symbol, total - progress);
    }

    public String getFirstColors(String input) {
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
