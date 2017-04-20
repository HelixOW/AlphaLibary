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
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

public class UUIDFetcher {

    private static final Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create();
    private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
    private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/names";

    private static HashMap<UUID, String> names = new HashMap<>();
    private static HashMap<String, UUID> uuids = new HashMap<>();

    /**
     * Gets the UUID of a {@link Player}
     *
     * @param p the {@link Player} to get its UUID from
     * @return the {@link UUID} of the {@link Player}
     */
    public static UUID getUUID(Player p) {
        return getUUID(p.getName());
    }

    /**
     * Gets the UUID of a {@link OfflinePlayer}
     *
     * @param p the {@link OfflinePlayer} to get its UUID from
     * @return the {@link UUID} of the {@link OfflinePlayer}
     */
    public static UUID getUUID(OfflinePlayer p) {
        return getUUID(p.getName());
    }

    /**
     * Gets the {@link UUID} of a {@link String}
     *
     * @param name the name of the {@link Player}
     * @return the {@link UUID} of the {@link Player}
     */
    public static UUID getUUID(String name) {
        if (name == null) {
            return UUID.randomUUID();
        }
        name = name.toLowerCase();

        if (uuids.containsKey(name))
            return uuids.get(name);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(
                    String.format(UUID_URL, name, System.currentTimeMillis() / 1000)).openConnection();
            connection.setReadTimeout(5000);

            PlayerUUID player =
                    gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())),
                            PlayerUUID.class);

            if (player == null)
                return null;

            if (player.getId() == null)
                return null;

            uuids.put(name, player.getId());

            return player.getId();
        } catch (Exception e) {
            Bukkit.getConsoleSender()
                    .sendMessage("Your server has no connection to the mojang servers or is runnig slowly.");
            return null;
        }
    }

    /**
     * Gets the name of a {@link UUID}
     *
     * @param uuid the {@link UUID} of the {@link Player}
     * @return the name of the {@link Player}
     */
    public static String getName(UUID uuid) {
        if (names.containsKey(uuid))
            return names.get(uuid);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(
                    String.format(NAME_URL, UUIDTypeAdapter.fromUUID(uuid))).openConnection();
            connection.setReadTimeout(5000);

            PlayerUUID[] allUserNames = gson.fromJson(
                    new BufferedReader(new InputStreamReader(connection.getInputStream())), PlayerUUID[].class);
            PlayerUUID currentName = allUserNames[allUserNames.length - 1];

            if (currentName == null)
                return Bukkit.getOfflinePlayer(uuid).getName();

            if (currentName.getName() == null) {
                return Bukkit.getOfflinePlayer(uuid).getName();
            }

            names.put(uuid, currentName.getName());

            return currentName.getName();
        } catch (Exception e) {
            Bukkit.getConsoleSender()
                    .sendMessage("§cYour server has no connection to the mojang servers or is runnig slow.");
            names.put(uuid, Bukkit.getOfflinePlayer(uuid).getName());
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
