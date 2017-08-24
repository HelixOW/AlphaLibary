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
package de.alphahelix.alphalibary.uuid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.util.UUIDTypeAdapter;
import de.alphahelix.alphalibary.AlphaLibary;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class UUIDFetcher {

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/names";

    private static ConcurrentHashMap<UUID, String> names = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, UUID> uuids = new ConcurrentHashMap<>();

    /**
     * Gets the {@link UUID} of a {@link String}
     *
     * @param p        the {@link Player}
     * @param callback the {@link UUIDCallback} with the parsed {@link UUID}
     */
    public static void getUUID(Player p, UUIDCallback callback) {
        getUUID(p.getName(), callback);
    }

    /**
     * Gets the {@link UUID} of a {@link String}
     *
     * @param p        the {@link Player}
     * @param callback the {@link UUIDCallback} with the parsed {@link UUID}
     */
    public static void getUUID(OfflinePlayer p, UUIDCallback callback) {
        getUUID(p.getName(), callback);
    }

    /**
     * Gets the {@link UUID} of a {@link String}
     *
     * @param name     the name of the {@link Player}
     * @param callback the {@link UUIDCallback} with the parsed {@link UUID}
     */
    public static void getUUID(String name, UUIDCallback callback) {
        if (name == null) {
            callback.done(null);
            return;
        }
        name = name.toLowerCase();

        if (uuids.containsKey(name)) {
            callback.done(uuids.get(name));
            return;
        }

        String finalName = name;
        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(
                        String.format(UUID_URL, finalName, System.currentTimeMillis() / 1000)).openConnection();
                connection.setReadTimeout(5000);

                PlayerUUID player =
                        GSON.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())),
                                PlayerUUID.class);

                if (player == null) {
                    callback.done(Bukkit.getOfflinePlayer(finalName).getUniqueId());
                    return;
                }

                if (player.getId() == null) {
                    callback.done(Bukkit.getOfflinePlayer(finalName).getUniqueId());
                    return;
                }

                uuids.put(finalName, player.getId());

                callback.done(player.getId());
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "Your server has no connection to the mojang servers or is runnig slowly. Using offline UUID now");
                callback.done(Bukkit.getOfflinePlayer(finalName).getUniqueId());
                callback.done(Bukkit.getOfflinePlayer(finalName).getUniqueId());
            }
        });
    }

    /**
     * Gets the name of a {@link UUID}
     *
     * @param uuid     the {@link UUID} of the {@link Player}
     * @param callback the {@link NameCallback} with the parsed name
     */
    public static void getName(UUID uuid, NameCallback callback) {
        if (names.containsKey(uuid)) {
            callback.done(names.get(uuid));
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(AlphaLibary.getInstance(), () -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(
                        String.format(NAME_URL, UUIDTypeAdapter.fromUUID(uuid))).openConnection();
                connection.setReadTimeout(5000);

                PlayerUUID[] allUserNames = GSON.fromJson(
                        new BufferedReader(new InputStreamReader(connection.getInputStream())), PlayerUUID[].class);
                PlayerUUID currentName = allUserNames[allUserNames.length - 1];

                if (currentName == null) {
                    callback.done(Bukkit.getOfflinePlayer(uuid).getName());
                    return;
                }

                if (currentName.getName() == null) {
                    callback.done(Bukkit.getOfflinePlayer(uuid).getName());
                    return;
                }

                names.put(uuid, currentName.getName());

                callback.done(currentName.getName());
            } catch (Exception e) {
                Bukkit.getLogger().log(Level.SEVERE, "Your server has no connection to the mojang servers or is runnig slowly. Using offline UUID now");
                callback.done(Bukkit.getOfflinePlayer(uuid).getName());
            }
        });
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
