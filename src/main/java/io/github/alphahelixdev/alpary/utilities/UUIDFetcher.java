package io.github.alphahelixdev.alpary.utilities;

import com.mojang.util.UUIDTypeAdapter;
import io.github.alphahelixdev.alpary.Alpary;
import io.github.alphahelixdev.helius.Cache;
import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.sql.SQLColumn;
import io.github.alphahelixdev.helius.sql.SQLConstraint;
import io.github.alphahelixdev.helius.sql.SQLTableHandler;
import io.github.alphahelixdev.helius.sql.sqlite.SQLiteConnector;
import io.github.alphahelixdev.helius.sql.sqlite.SQLiteDataType;
import io.github.alphahelixdev.helius.web.WebConsumer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UUIDFetcher {
	
	private static final String UUID_URL = "https://api.mojang.com/users/profiles/minecraft/%s?at=%d";
	private static final String NAME_URL = "https://api.mojang.com/user/profiles/%s/NAMES";
	
	private UUIDCache cache;
	
	public UUIDFetcher() {
		Alpary.getInstance().gsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter());
		this.cache = new UUIDCache();
		Helius.addCache(this.cache);
	}
	
	public void getUUID(Player p, WebConsumer<UUID> callback) {
		this.getUUID(p.getName(), callback);
	}
	
	public void getUUID(String name, WebConsumer<UUID> callback) {
		Bukkit.getScheduler().runTaskAsynchronously(Alpary.getInstance(), () -> {
			UUID id = null;
			try {
				id = this.getUUID(name);
			} catch(UUIDNotFoundException e) {
				callback.fail("Unable to find UUID for " + name);
			}
			
			if(id == null)
				callback.fail("Unable to find UUID for " + name);
			else
				callback.success(id);
		});
	}
	
	public UUID getUUID(String name) throws UUIDNotFoundException {
		if(name == null)
			return null;
		
		name = name.toLowerCase();
		
		if(this.getCache().getUuid().containsKey(name))
			return this.getCache().getUuid().get(name);
		
		String finalName = name;
		
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(
					String.format(this.getUUIDUrl(), finalName, System.currentTimeMillis() / 1000)).openConnection();
			connection.setReadTimeout(5000);
			
			PlayerUUID player = Alpary.getInstance().gson().fromJson(
					new BufferedReader(new InputStreamReader(connection.getInputStream())), PlayerUUID.class);
			
			if(player == null)
				return null;
			
			if(player.getId() == null)
				return null;
			
			this.getCache().getUuid().put(finalName, player.getId());
			
			return player.getId();
		} catch(Exception e) {
			throw new UUIDNotFoundException(name);
		}
	}
	
	public UUIDCache getCache() {
		return this.cache;
	}
	
	public String getUUIDUrl() {
		return UUIDFetcher.UUID_URL;
	}
	
	public void getUUID(OfflinePlayer p, WebConsumer<UUID> callback) {
		this.getUUID(p.getName(), callback);
	}
	
	public void getName(UUID uuid, WebConsumer<String> callback) {
		Bukkit.getScheduler().runTaskAsynchronously(Alpary.getInstance(), () -> {
			String name = null;
			try {
				name = getName(uuid);
			} catch(NameNotFoundException e) {
				callback.fail("Unable to find a name for " + uuid);
			}
			
			if(name.equals(""))
				callback.fail("Unable to find a name for " + uuid);
			else
				callback.success(name);
		});
	}
	
	public String getName(UUID uuid) throws NameNotFoundException {
		if(uuid == null)
			return "";
		
		if(this.getCache().getName().containsKey(uuid))
			return this.getCache().getName().get(uuid);
		
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(
					String.format(this.getNameUrl(), UUIDTypeAdapter.fromUUID(uuid))).openConnection();
			connection.setReadTimeout(5000);
			
			PlayerUUID[] allUserNames = Alpary.getInstance().gson().fromJson(
					new BufferedReader(new InputStreamReader(connection.getInputStream())), PlayerUUID[].class);
			PlayerUUID currentName = allUserNames[allUserNames.length - 1];
			
			if(currentName == null)
				return "";
			
			if(currentName.getName() == null)
				return "";
			
			this.getCache().getName().put(uuid, currentName.getName());
			
			return currentName.getName();
		} catch(Exception e) {
			throw new NameNotFoundException(uuid.toString());
		}
	}
	
	public String getNameUrl() {
		return UUIDFetcher.NAME_URL;
	}
	
	public UUID getUUID(Player p) throws UUIDNotFoundException {
		return this.getUUID(p.getName());
	}
	
	public UUID getUUID(OfflinePlayer p) throws UUIDNotFoundException {
		return this.getUUID(p.getName());
	}
	
	public class UUIDCache implements Cache {
		private final Map<UUID, String> name = new ConcurrentHashMap<>();
		private final Map<String, UUID> uuid = new ConcurrentHashMap<>();
		
		private final SQLTableHandler nameTable;
		private final SQLTableHandler uuidTable;
		
		public UUIDCache() {
			this.nameTable = new SQLTableHandler(new SQLiteConnector(()
					-> Alpary.getInstance().getDataFolder().getAbsolutePath() + "/uuidNameCache.db"));
			this.uuidTable = new SQLTableHandler(new SQLiteConnector(()
					-> Alpary.getInstance().getDataFolder().getAbsolutePath() + "/uuidUUIDCache.db"));
			
			this.getNameTable().create(new SQLColumn("name", SQLiteDataType.TEXT, new SQLConstraint(SQLConstraint.NOT_NULL, "")),
					new SQLColumn("uuid", SQLiteDataType.TEXT, new SQLConstraint(SQLConstraint.PRIMARY_KEY, "")));
			this.getUuidTable().create(new SQLColumn("name", SQLiteDataType.TEXT, new SQLConstraint(SQLConstraint.PRIMARY_KEY, "")),
					new SQLColumn("uuid", SQLiteDataType.TEXT, new SQLConstraint(SQLConstraint.NOT_NULL, "")));
		}
		
		public SQLTableHandler getNameTable() {
			return this.nameTable;
		}
		
		public SQLTableHandler getUuidTable() {
			return this.uuidTable;
		}
		
		@Override
		public void clear() {
			this.getName().clear();
			this.getUuid().clear();
		}
		
		public Map<UUID, String> getName() {
			return this.name;
		}
		
		public Map<String, UUID> getUuid() {
			return this.uuid;
		}
		
		@Override
		public String clearMessage() {
			return "UUID Cache cleared";
		}
		
		@Override
		public void save() {
			this.getNameTable().empty();
			this.getUuidTable().empty();
			
			for(Map.Entry<UUID, String> nameEntry : this.getName().entrySet())
				this.getNameTable().insert(nameEntry.getKey().toString(), nameEntry.getValue());
			
			for(Map.Entry<String, UUID> uuidEntry : this.getUuid().entrySet())
				this.getUuidTable().insert(uuidEntry.getKey(), uuidEntry.getValue().toString());
		}
	}
	
	public class UUIDNotFoundException extends Exception {
		public UUIDNotFoundException(String name) {
			super("Unable to find UUID for " + name);
		}
	}
	
	public class NameNotFoundException extends Exception {
		public NameNotFoundException(String uuid) {
			super("Unable to find Name for " + uuid);
		}
	}
	
	@Override
	public String toString() {
		return "UUIDFetcher{" +
				"cache=" + cache +
				'}';
	}
}

class PlayerUUID {
	private String name;
	private UUID id;
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getName(), this.getId());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		PlayerUUID that = (PlayerUUID) o;
		return Objects.equals(this.getName(), that.getName()) &&
				Objects.equals(this.getId(), that.getId());
	}
	
	public String getName() {
		return this.name;
	}
	
	public UUID getId() {
		return this.id;
	}
	
	
	@Override
	public String toString() {
		return "PlayerUUID{" +
				"name='" + this.name + '\'' +
				",id=" + this.id +
				'}';
	}
}
