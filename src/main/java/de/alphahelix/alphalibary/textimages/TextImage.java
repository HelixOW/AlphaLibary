package de.alphahelix.alphalibary.textimages;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class TextImage {

    public static void sendHead(Player p) {
        sendHead(p, p);
    }

    public static void sendHead(Player p, OfflinePlayer head) {
        try {
            sendImage(p, new URL("https://minotar.net/avatar/" + head.getName() + "/10.png"));
        } catch (MalformedURLException ignored) {
        }
    }

    public static void sendHeadText(Player p, OfflinePlayer head, String... text) {
        try {
            sendImageText(p, new URL("https://minotar.net/avatar/" + head.getName() + "/10.png"), text);
        } catch (MalformedURLException ignored) {
        }
    }

    public static void sendImage(Player p, URL url) {
        urlToMessage(url).sendToPlayer(p);
    }

    public static void sendImageText(Player p, URL url, String... text) {
        ImageMessage im = urlToMessage(url);

        im.appendText(text);

        im.sendToPlayer(p);
    }

    public static ImageMessage toMessage(File file) {
        BufferedImage imageToSend = null;
        try {
            imageToSend = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ImageMessage(imageToSend, 8, ImageChar.MEDIUM_SHADE.getChar());
    }

    public static ImageMessage urlToMessage(URL url) {
        BufferedImage imageToSend = null;
        try {
            imageToSend = ImageIO.read(url);
        } catch (IOException e) {
        }
        if (imageToSend.getWidth() != imageToSend.getHeight()) {
            System.out.println("§eThe image at §a" + url + " §eis not square!");
            System.out.println("§eAttempting to resize...");
            long time = System.currentTimeMillis();
            imageToSend = createThumbnail(imageToSend);
            time = System.currentTimeMillis() - time;
            System.out.println("§eThe image was resized in §a" + time + "ms");
        }
        return new ImageMessage(imageToSend, 8, ImageChar.MEDIUM_SHADE.getChar());
    }

    public static BufferedImage createThumbnail(BufferedImage image) {
        BufferedImage scaledImage = new BufferedImage(800, 800, 2);

        Graphics2D graphics2D = scaledImage.createGraphics();

        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, 800, 800, null);
        graphics2D.dispose();
        return scaledImage;
    }
}
