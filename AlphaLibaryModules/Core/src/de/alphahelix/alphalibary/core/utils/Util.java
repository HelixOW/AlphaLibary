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
package de.alphahelix.alphalibary.core.utils;

import com.google.common.base.Strings;
import de.alphahelix.alphalibary.core.AlphaLibary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
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

    /**
     * Wraps the floor(Abrunden) method from the net.minecraft.server MathHelper
     *
     * @param var0 the double to floor
     * @return the floored int
     */
    public static int floor(double var0) {
        int var2 = (int) var0;
        return var0 < (double) var2 ? var2 - 1 : var2;
    }

    /**
     * Converts a float into a angle
     *
     * @param v the float to convert
     * @return the converted angle as a byte
     */
    public static byte toAngle(float v) {
        return (byte) ((int) (v * 256.0F / 360.0F));
    }

    /**
     * Converts a double into a delta
     *
     * @param v the double to convert
     * @return the converted delta as a double
     */
    public static double toDelta(double v) {
        return ((v * 32) * 128);
    }

    public static String getProgessBar(int current, int maximum, int total, char symbol, ChatColor completed, ChatColor uncompleted) {
        float percent = current / maximum;
        int progress = (int) (total * percent);

        return Strings.repeat("" + completed + symbol, progress)
                + Strings.repeat("" + uncompleted + symbol, total - progress);
    }

    public static String[] replaceInArray(String pattern, String replace, String... array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replace(pattern, replace);
        }
        return array;
    }

    public static boolean isLong(String s) {
        Scanner sc = new Scanner(s.trim());

        if (!sc.hasNextLong()) return false;

        sc.nextLong();
        return !sc.hasNext();
    }

    public static boolean isDouble(String s) {
        Scanner sc = new Scanner(s.trim());

        if (!sc.hasNextDouble()) return false;

        sc.nextDouble();
        return !sc.hasNext();
    }

    public static String getFirstColors(String input) {
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

    public static String repeat(String string, int count) {
        if (count <= 1) {
            return count == 0 ? "" : string;
        } else {
            int len = string.length();
            long longSize = (long) len * (long) count;
            int size = (int) longSize;
            if ((long) size != longSize) {
                throw new ArrayIndexOutOfBoundsException("Required array size too large: " + longSize);
            } else {
                char[] array = new char[size];
                string.getChars(0, len, array, 0);
                int n;
                for (n = len; n < size - n; n <<= 1) {
                    System.arraycopy(array, 0, array, n, n);
                }
                System.arraycopy(array, 0, array, n, size - n);
                return new String(array);
            }
        }
    }

    public static Vector blockFaceToVector(BlockFace face, double length) {
        return new Vector(face.getModX(), face.getModY(), face.getModZ()).multiply(length);
    }

    public static Vector rotatePitch(Vector toRotate, double pitch) {
        return rotate(toRotate, 0, pitch);
    }

    public static Vector rotateYaw(Vector toRotate, double yaw) {
        return rotate(toRotate, yaw, 0);
    }

    public static Vector rotate(Vector toRotate, double yaw, double pitch) {
        return rotate(toRotate, yaw, pitch, 0);
    }

    public static Vector rotate(Vector toRotate, double yaw, double pitch, double roll) {
        Vector temp1 = rotate(toRotate, new Vector(0, 1, 0), yaw);
        Vector temp2 = rotate(temp1, new Vector(1, 0, 0), pitch);
        return rotate(temp2, new Vector(0, 0, 1), roll);
    }

    public static Vector rotate(Vector toRotate, Vector around, double angle) {
        if (angle == 0)
            return toRotate;

        /*
        v = around;

        x1 = x * ((vx * vx) * (1 - cos a) + cos a) + y * ((vx * vy) * (1 - cos a) - vz * sin a) + ((vx * vz) * (1 - cos a) + vy * sin a)
        x2 = x * (())

         */

        double vx = around.getX(), vy = around.getY(), vz = around.getZ();
        double x = toRotate.getX(), y = toRotate.getY(), z = toRotate.getZ();
        double sinA = Math.sin(Math.toRadians(angle)), cosA = Math.cos(Math.toRadians(angle));

        double x1 = x * ((vx * vx) * (1 - cosA) + cosA) + y * ((vx * vy) * (1 - cosA) - vz * sinA) + z * ((vx * vz) * (1 - cosA) + vy * sinA);
        double y1 = x * ((vy * vx) * (1 - cosA) + vz * sinA) + y * ((vy * vy) * (1 - cosA) + cosA) + z * ((vy * vz) * (1 - cosA) - vx * sinA);
        double z1 = x * ((vz * vx) * (1 - cosA) - vy * sinA) + y * ((vz * vy) * (1 - cosA) + vx * sinA) + z * ((vz * vz) * (1 - cosA) + cosA);

        return new Vector(x1, y1, z1);
    }

    public static String replaceLast(String string, String toReplace, String replacement) {
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1) {
            return string.substring(0, pos)
                    + replacement
                    + string.substring(pos + toReplace.length(), string.length());
        } else {
            return string;
        }
    }

    public static List<String> upperEverything(List<String> list) {
        List<String> nL = new LinkedList<>();
        for (String str : list)
            nL.add(str.toUpperCase());

        return nL;
    }
}
