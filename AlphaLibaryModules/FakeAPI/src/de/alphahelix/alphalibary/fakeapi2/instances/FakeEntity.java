package de.alphahelix.alphalibary.fakeapi2.instances;

import com.google.gson.annotations.Expose;
import de.alphahelix.alphalibary.core.utilites.players.PlayerMap;
import de.alphahelix.alphalibary.reflection.ReflectionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;
import java.util.UUID;

public class FakeEntity {
	
	private final String name;
	private final UUID id;
	private final Location start;
	
	@Expose
	private final Object nmsEntity;
	@Expose
	private final int entityID;
	@Expose
	private final PlayerMap<BukkitTask> followMap = new PlayerMap<>();
	@Expose
	private final PlayerMap<BukkitTask> splitMap = new PlayerMap<>();
	@Expose
	private Location current;
	
	public FakeEntity(String name, Location start, Object nmsEntity) {
		this.name = name;
		this.id = UUID.randomUUID();
		this.start = start;
		this.current = start;
		this.nmsEntity = nmsEntity;
		this.entityID = ReflectionUtil.getEntityID(nmsEntity);
	}
	
	public final String getName() {
		return name;
	}
	
	public final UUID getId() {
		return id;
	}
	
	public final Location getStart() {
		return start;
	}
	
	public final Object getNmsEntity() {
		return nmsEntity;
	}
	
	public final int getEntityID() {
		return entityID;
	}
	
	public final Location getCurrent() {
		return current;
	}
	
	public final FakeEntity setCurrent(Location current) {
		this.current = current;
		return this;
	}
	
	public PlayerMap<BukkitTask> getFollow() {
		return followMap;
	}
	
	public PlayerMap<BukkitTask> getSplit() {
		return splitMap;
	}
	
	public void cancelAllSplittedTasks(Player p) {
		if(splitMap.containsKey(p)) {
			splitMap.get(p).cancel();
			splitMap.remove(p);
		}
	}
	
	public void unFollow(Player p) {
		if(followMap.containsKey(p)) {
			followMap.get(p).cancel();
			followMap.remove(p);
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name, id, start, nmsEntity, entityID, current, followMap, splitMap);
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		FakeEntity entity = (FakeEntity) o;
		return entityID == entity.entityID &&
				Objects.equals(name, entity.name) &&
				Objects.equals(id, entity.id) &&
				Objects.equals(start, entity.start) &&
				Objects.equals(nmsEntity, entity.nmsEntity) &&
				Objects.equals(current, entity.current) &&
				Objects.equals(followMap, entity.followMap) &&
				Objects.equals(splitMap, entity.splitMap);
	}
	
	@Override
	public String toString() {
		return "FakeEntity{" +
				"name='" + name + '\'' +
				", id=" + id +
				", start=" + start +
				", nmsEntity=" + nmsEntity +
				", entityID=" + entityID +
				", current=" + current +
				", followMap=" + followMap +
				", splitMap=" + followMap +
				'}';
	}
}
