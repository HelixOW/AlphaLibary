package de.alphahelix.alphalibary.textimages;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageMessage {
    private static String[] lines;

    public ImageMessage(BufferedImage image, int height, char imgChar) {
        ChatColor[][] chatColors = toChatColorArray(image, height);
        lines = toImgMessage(chatColors, imgChar);
    }

    public ImageMessage(ChatColor[][] chatColors, char imgChar) {
        lines = toImgMessage(chatColors, imgChar);
    }

    public ImageMessage(String... imgLines) {
        lines = imgLines;
    }

    private static String[] toImgMessage(ChatColor[][] colors, char imgchar) {
        String[] lines = new String[colors[0].length];
        for (int y = 0; y < colors[0].length; y++) {
            StringBuilder line = new StringBuilder();
            for (int x = 0; x < colors.length; x++) {
                ChatColor color = colors[x][y];
                line.append(color != null ? colors[x][y].toString() + imgchar : Character.valueOf(' '));
            }
            lines[y] = (line.toString() + ChatColor.RESET);
        }
        return lines;
    }

    public static String[] getLines() {
        return lines;
    }

    public ImageMessage appendText(String... text) {
        for (int y = 0; y < lines.length; y++) {
            if (text.length > y) {
                String[] line = lines;
                line[y] = (line[y] + " " + text[y]);
            }
        }
        return this;
    }

    public ImageMessage appendCenteredText(String... text) {
        for (int y = 0; y < lines.length; y++) {
            if (text.length > y) {
                int len = 65 - lines[y].length();
                lines[y] = (lines[y] + center(text[y], len));
            } else {
                return this;
            }
        }
        return this;
    }

    private ChatColor[][] toChatColorArray(BufferedImage image, int height) {
        double ratio = image.getHeight() / image.getWidth();
        int width = (int) (height / ratio);
        if (width > 10) {
            width = 10;
        }
        BufferedImage resized = resizeImage(image, width, height);

        ChatColor[][] chatImg = new ChatColor[resized.getWidth()][resized.getHeight()];
        for (int x = 0; x < resized.getWidth(); x++) {
            for (int y = 0; y < resized.getHeight(); y++) {
                int rgb = resized.getRGB(x, y);
                ChatColor closest = getClosestChatColor(new Color(rgb));
                chatImg[x][y] = closest;
            }
        }
        return chatImg;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage img = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.SCALE_FAST);
        Graphics2D g = img.createGraphics();
        g.scale(width, height);
        g.drawImage(originalImage, null, 0, 0);
        g.dispose();
        return originalImage;
    }

    private double getDistance(Color c1, Color c2) {
        double rmean = (c1.getRed() + c2.getRed()) / 2.0D;
        double r = c1.getRed() - c2.getRed();
        double g = c1.getGreen() - c2.getGreen();
        int b = c1.getBlue() - c2.getBlue();
        double weightR = 2.0D + rmean / 256.0D;
        double weightG = 4.0D;
        double weightB = 2.0D + (255.0D - rmean) / 256.0D;
        return weightR * r * r + weightG * g * g + weightB * b * b;
    }

    private ChatColor getClosestChatColor(Color color) {
        return ColorUtil.fromRGB(color.getRed(), color.getGreen(), color.getBlue());
    }

    private String center(String s, int length) {
        if (s.length() > length) {
            return s.substring(0, length);
        }
        if (s.length() == length) {
            return s;
        }
        int leftPadding = (length - s.length()) / 2;
        StringBuilder leftBuilder = new StringBuilder();
        for (int i = 0; i < leftPadding; i++) {
            leftBuilder.append(" ");
        }
        return leftBuilder.toString() + s;
    }

    public void sendToPlayer(Player player) {
        String[] arrayOfString;
        int j = (arrayOfString = lines).length;
        for (int i = 0; i < j; i++) {
            String line = arrayOfString[i];
            player.sendMessage(line);
        }
    }
}
