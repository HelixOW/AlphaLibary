package io.github.alphahelixdev.alpary.fake;

import io.github.alphahelixdev.alpary.fake.exceptions.NoSuchFakeEntityException;
import io.github.alphahelixdev.alpary.utils.Utils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@EqualsAndHashCode
@ToString
public class EntityHandler {
	
	private final Map<UUID, List<FakeEntity>> entities = new HashMap<>();
	
	public void addFakeEntity(Player p, FakeEntity entity) {
		List<FakeEntity> list = getFakeEntites(p);
		
		list.add(entity);
		
		this.getEntities().put(p.getUniqueId(), list);
	}
	
	public List<FakeEntity> getFakeEntites(Player p) {
		return this.getEntities().containsKey(p.getUniqueId()) ? this.getEntities().get(p.getUniqueId()) : new ArrayList<>();
	}
	
	public void removeFakeEntity(Player p, FakeEntity fake) {
		List<FakeEntity> list = getFakeEntites(p);
		
		list.remove(fake);
		
		this.getEntities().put(p.getUniqueId(), list);
	}
	
	public boolean isFakeEntityInRange(Player p, int range) {
		if (this.seesNoEntities(p)) return false;
		
		for (Block b : p.getLineOfSight(null, range)) {
			for (FakeEntity fakeEntity : this.getFakeEntites(p)) {
				if ((Utils.locations().isSameBlockLocation(b.getLocation(), fakeEntity.getCurrent()) || (b.getX() == fakeEntity.getCurrent().getBlockX()
						&& b.getY() == (fakeEntity.getCurrent().getBlockY() + 1)
						&& b.getZ() == fakeEntity.getCurrent().getBlockZ())))
					return true;
			}
		}
		return false;
	}
	
	public boolean seesNoEntities(Player p) {
		return !this.getEntities().containsKey(p.getUniqueId());
	}
	
	public FakeEntity getLookedAtFakeEntity(Player p, int range) throws NoSuchFakeEntityException {
		if (this.seesNoEntities(p)) throw new NoSuchFakeEntityException();
		
		for (Block b : p.getLineOfSight(null, range)) {
			for (FakeEntity fakeEntity : this.getFakeEntites(p)) {
				if ((Utils.locations().isSameBlockLocation(b.getLocation(), fakeEntity.getCurrent()) || (b.getX() == fakeEntity.getCurrent().getBlockX()
						&& b.getY() == (fakeEntity.getCurrent().getBlockY() + 1)
						&& b.getZ() == fakeEntity.getCurrent().getBlockZ())))
					return fakeEntity;
			}
		}
		throw new NoSuchFakeEntityException();
	}
	
	public List<FakeEntity> getFakeEntitiesInRadius(Player p, double radius) {
		if (this.seesNoEntities(p))
			return new ArrayList<>();
		
		List<FakeEntity> list = new ArrayList<>();
		
		for (FakeEntity fakeEntity : this.getFakeEntites(p)) {
			if (fakeEntity.getCurrent().distanceSquared(p.getLocation()) <= radius * radius)
				list.add(fakeEntity);
		}
		return list;
	}
	
	public FakeEntity getFakeEntityByObject(Player p, Object fake) throws NoSuchFakeEntityException {
		if (seesNoEntities(p))
			throw new NoSuchFakeEntityException();
		
		for (FakeEntity fakeEntity : this.getFakeEntites(p))
			if (fakeEntity.getNmsEntity() == fake) return fakeEntity;
		
		throw new NoSuchFakeEntityException();
	}
	
	public FakeEntity getFakeEntityByName(Player p, String name) throws NoSuchFakeEntityException {
		if (seesNoEntities(p))
			throw new NoSuchFakeEntityException();
		
		for (FakeEntity fakeEntity : this.getFakeEntites(p))
			if (ChatColor.stripColor(fakeEntity.getName()).equals(ChatColor.stripColor(name)))
				return fakeEntity;
		
		throw new NoSuchFakeEntityException();
	}
	
	public FakeEntity getFakeEntityByID(Player p, int entityID) throws NoSuchFakeEntityException {
		if(seesNoEntities(p))
			throw new NoSuchFakeEntityException();
		
		for(FakeEntity fakeEntity : this.getFakeEntites(p))
			if(fakeEntity.getEntityID() == entityID)
				return fakeEntity;
		
		throw new NoSuchFakeEntityException();
	}
}
