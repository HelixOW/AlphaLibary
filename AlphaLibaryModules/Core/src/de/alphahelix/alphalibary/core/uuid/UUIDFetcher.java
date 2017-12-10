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
package de.alphahelix.alphalibary.core.uuid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.util.UUIDTypeAdapter;
import de.alphahelix.alphalibary.core.AlphaLibary;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;

public class UUIDFetcher {

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/NAMES";

    private static final Map<UUID, String> NAMES = new ConcurrentHashMap<>();
    private static final Map<String, UUID> UUIDS = new ConcurrentHashMap<>();

    /**
     * Gets the {@link UUID} of a {@link String} async
     *
     * @param p        the {@link Player}
     * @param callback the {@link Consumer<UUID>} with the parsed {@link UUID}
     */
    public static void getUUID(Player p, Consumer<UUID> callback) {
        getUUID(p.getName(), callback);
    }

    /**
     * Gets the {@link UUID} of a {@link String} async
     *
     * @param p        the {@link Player}
     * @param callback the {@link Consumer<UUID>} with the parsed {@link UUID}
     */
    public static void getUUID(OfflinePlayer p, Consumer<UUID> callback) {
        getUUID(p.getName(), callback);
    }

    /**
     * Gets the {@link UUID} of a {@link String} async
     *
     * @param name     the name of the {@link Player}
     * @param callback the {@link Consumer<UUID>} with the parsed {@link UUID}
     */
    public static void getUUID(String name, Consumer<UUID> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> callback.accept(getUUID(name)));
    }

    /**
     * Gets the name of a {@link UUID} async
     *
     * @param uuid     the {@link UUID} of the {@link Player}
     * @param callback the {@link Consumer<String>} with the parsed name
     */
    public static void getName(UUID uuid, Consumer<String> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> callback.accept(getName(uuid)));
    }

    /**
     * Gets the {@link UUID} of a {@link String} sync
     *
     * @param p the {@link Player}
     * @see UUIDFetcher#getUUID(Player, Consumer<UUID>)
     */
    public static UUID getUUID(Player p) {
        return getUUID(p.getName());
    }

    /**
     * Gets the {@link UUID} of a {@link String} sync
     *
     * @param p the {@link OfflinePlayer}
     * @see UUIDFetcher#getUUID(OfflinePlayer, Consumer<UUID>)
     */
    public static UUID getUUID(OfflinePlayer p) {
        return getUUID(p.getName());
    }

    /**
     * Gets the {@link UUID} of a {@link String} sync
     *
     * @param name the name of the {@link Player}
     * @see UUIDFetcher#getName(UUID, Consumer<String>)
     */
    public static UUID getUUID(String name) {
        if (name == null)
            return null;

        name = name.toLowerCase();

        if (UUIDS.containsKey(name))
            return UUIDS.get(name);

        String finalName = name;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(
                    String.format(UUID_URL, finalName, System.currentTimeMillis() / 1000)).openConnection();
            connection.setReadTimeout(5000);

            PlayerUUID player =
                    GSON.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())),
                            PlayerUUID.class);

            if (player == null)
                return Bukkit.getOfflinePlayer(finalName).getUniqueId();


            if (player.getId() == null)
                return Bukkit.getOfflinePlayer(finalName).getUniqueId();


            UUIDS.put(finalName, player.getId());

            return player.getId();
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Your server has no connection to the mojang servers or is runnig slowly. Using offline UUID now");

            return Bukkit.getOfflinePlayer(finalName).getUniqueId();
        }
    }

    /**
     * Gets the name of a {@link UUID} sync
     *
     * @param uuid the {@link UUID} of the {@link Player}
     * @see UUIDFetcher#getName(UUID, Consumer<String>)
     */
    public static String getName(UUID uuid) {
        if (NAMES.containsKey(uuid))
            return NAMES.get(uuid);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(
                    String.format(NAME_URL, UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            connection.setReadTimeout(5000);

            PlayerUUID[] allUserNames = GSON.fromJson(
                    new BufferedReader(new InputStreamReader(connection.getInputStream())), PlayerUUID[].class);
            PlayerUUID currentName = allUserNames[allUserNames.length - 1];

            if (currentName == null)
                return Bukkit.getOfflinePlayer(uuid).getName();

            if (currentName.getName() == null)
                return Bukkit.getOfflinePlayer(uuid).getName();

            NAMES.put(uuid, currentName.getName());

            return currentName.getName();
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Your server has no connection to the mojang servers or is runnig slowly. Using offline UUID now");
            return Bukkit.getOfflinePlayer(uuid).getName();
        }
    }
}

class PlayerUUID {

    private String name;
    private UUID id;

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

}
