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
package de.alphahelix.alphalibary.utils;

import de.alphahelix.alphalibary.AlphaLibary;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Util {

    /**
     * Rounds a {@link Double} up
     *
     * @param value     the {@link Double} to round
     * @param precision the precision to round up to
     * @return the rounded up {@link Double}
     */
    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    /**
     * Creates a cooldown
     *
     * @param length       the lenght of the cooldown in ticks
     * @param key          the key to add a cooldown for
     * @param cooldownList the {@link List} where the key is in
     */
    public static <T> void cooldown(int length, final T key, final List<T> cooldownList) {
        cooldownList.add(key);
        new BukkitRunnable() {
            public void run() {
                cooldownList.remove(key);
            }
        }.runTaskLaterAsynchronously(AlphaLibary.getInstance(), length);
    }

    /**
     * @return [] out of a ... array
     */
    @SafeVarargs
    public static <T> T[] makeArray(T... types) {
        return types;
    }

    public static Player[] makePlayerArray(Player... types) {
        return types;
    }

    public static Player[] makePlayerArray(List<String> types) {
        ArrayList<Player> playerArrayList = new ArrayList<>();

        for (String type : types) {
            if (Bukkit.getPlayer(type) == null) continue;
            playerArrayList.add(Bukkit.getPlayer(type));
        }

        return playerArrayList.toArray(new Player[playerArrayList.size()]);
    }

    public static Player[] makePlayerArray(Set<String> types) {
        ArrayList<Player> playerArrayList = new ArrayList<>();

        for (String type : types) {
            if (Bukkit.getPlayer(type) == null) continue;
            playerArrayList.add(Bukkit.getPlayer(type));
        }

        return playerArrayList.toArray(new Player[playerArrayList.size()]);
    }

    public static void runLater(long ticks, boolean async, Runnable timer) {
        if (async)
            new BukkitRunnable() {
                public void run() {
                    timer.run();
                }
            }.runTaskLaterAsynchronously(AlphaLibary.getInstance(), ticks);
        else
            new BukkitRunnable() {
                public void run() {
                    timer.run();
                }
            }.runTaskLater(AlphaLibary.getInstance(), ticks);
    }

    public static void runTimer(long wait, long ticks, boolean async, Runnable timer) {
        if (async)
            new BukkitRunnable() {
                public void run() {
                    timer.run();
                }
            }.runTaskTimerAsynchronously(AlphaLibary.getInstance(), wait, ticks);
        else
            new BukkitRunnable() {
                public void run() {
                    timer.run();
                }
            }.runTaskTimer(AlphaLibary.getInstance(), wait, ticks);
    }

    public static boolean isSame(ItemStack a, ItemStack b) {
        if (a == null || b == null) return false;

        boolean type = a.getType() == b.getType();
        boolean amount = a.getAmount() == b.getAmount();
        boolean dura = a.getDurability() == b.getDurability();
        boolean itemMeta = a.hasItemMeta() == b.hasItemMeta();

        return (type) &&
                (amount) &&
                (dura) &&
                (itemMeta) && isSameMeta(a.getItemMeta(), b.getItemMeta());
    }

    private static boolean isSameMeta(ItemMeta a, ItemMeta b) {
        if (a == null || b == null) return false;

        boolean dn = a.hasDisplayName() == b.hasDisplayName();
        boolean l = a.hasLore() == b.hasLore();
        boolean hdn = a.hasDisplayName();
        boolean hl = a.hasLore();

        if (dn) {
            if (l) {
                if (hdn) {
                    if (hl) {
                        return a.getDisplayName().equals(b.getDisplayName()) && a.getLore().equals(b.getLore());
                    } else { //only name
                        return a.getDisplayName().equals(b.getDisplayName());
                    }
                } else { //maybe lore
                    return !hl || a.getLore().equals(b.getLore());
                }
            } else { //maybe name
                return !hdn || a.getDisplayName().equals(b.getDisplayName());
            }
        } else if (l) {
            return !hl || a.getLore().equals(b.getLore());
        }
        return false;
    }

    public static String generateRandomString(int size) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            char c = chars[ThreadLocalRandom.current().nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static int toMultipleOfNine(int val) {
        return ((val / 9) + 1) * 9;
    }

    public static void unzip(String zipPath, String outputFolder) {
        try {
            ZipFile zipFile = new ZipFile(zipPath);
            Enumeration<?> enu = zipFile.entries();

            File folder = new File(outputFolder);
            if (!folder.exists()) {
                folder.mkdir();
            }

            while (enu.hasMoreElements()) {
                ZipEntry zipEntry = (ZipEntry) enu.nextElement();

                String name = zipEntry.getName();

                File file = new File(outputFolder + File.separator + name);
                if (name.endsWith("/")) {
                    file.mkdirs();
                    continue;
                }

                File parent = file.getParentFile();
                if (parent != null) {
                    parent.mkdirs();
                }

                InputStream is = zipFile.getInputStream(zipEntry);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] bytes = new byte[1024];
                int length;
                while ((length = is.read(bytes)) >= 0) {
                    fos.write(bytes, 0, length);
                }

                is.close();
                fos.close();
            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> getTypesOf(Class<T> clazzType, List<Object> inList) {
        List<T> types = new ArrayList<>();
        for (Object e : inList)
            if (e.getClass().isInstance(clazzType))
                types.add((T) e);
        return types;
    }
}
