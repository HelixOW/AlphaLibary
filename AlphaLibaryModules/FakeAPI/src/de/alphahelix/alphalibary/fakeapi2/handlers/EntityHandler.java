package de.alphahelix.alphalibary.fakeapi2.handlers;

import de.alphahelix.alphalibary.core.utilites.players.PlayerMap;
import de.alphahelix.alphalibary.core.utils.LocationUtil;
import de.alphahelix.alphalibary.fakeapi2.exceptions.NoSuchFakeEntityException;
import de.alphahelix.alphalibary.fakeapi2.instances.FakeEntity;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EntityHandler {
	
	private final PlayerMap<List<FakeEntity>> entities = new PlayerMap<>();
	
	public void addFakeEntity(Player p, FakeEntity entity) {
		List<FakeEntity> list = getFakeEntites(p);
		
		list.add(entity);
		
		entities.put(p, list);
	}
	
	public List<FakeEntity> getFakeEntites(Player p) {
		return entities.containsKey(p) ? entities.get(p) : new ArrayList<>();
	}
	
	public void removeFakeEntity(Player p, FakeEntity fake) {
		List<FakeEntity> list = getFakeEntites(p);
		
		list.remove(fake);
		
		entities.put(p, list);
	}
	
	public boolean isFakeEntityInRange(Player p, int range) {
		if(seesNoEntities(p)) return false;
		
		for(Block b : p.getLineOfSight(null, range)) {
			for(FakeEntity fakeEntity : getFakeEntites(p)) {
				if((LocationUtil.isSameLocation(b.getLocation(), fakeEntity.getCurrent()) || (b.getX() == fakeEntity.getCurrent().getBlockX()
						&& b.getY() == (fakeEntity.getCurrent().getBlockY() + 1)
						&& b.getZ() == fakeEntity.getCurrent().getBlockZ())))
					return true;
			}
		}
		return false;
	}
	
	public boolean seesNoEntities(Player p) {
		return !entities.containsKey(p);
	}
	
	public FakeEntity getLookedAtFakeEntity(Player p, int range) throws NoSuchFakeEntityException {
		if(seesNoEntities(p)) throw new NoSuchFakeEntityException();
		
		for(Block b : p.getLineOfSight(null, range)) {
			for(FakeEntity fakeEntity : getFakeEntites(p)) {
				if((LocationUtil.isSameLocation(b.getLocation(), fakeEntity.getCurrent()) || (b.getX() == fakeEntity.getCurrent().getBlockX()
						&& b.getY() == (fakeEntity.getCurrent().getBlockY() + 1)
						&& b.getZ() == fakeEntity.getCurrent().getBlockZ())))
					return fakeEntity;
			}
		}
		throw new NoSuchFakeEntityException();
	}
	
	public List<FakeEntity> getFakeEntitiesInRadius(Player p, double radius) {
		if(seesNoEntities(p))
			return new ArrayList<>();
		
		List<FakeEntity> list = new ArrayList<>();
		
		for(FakeEntity fakeEntity : getFakeEntites(p)) {
			if(fakeEntity.getCurrent().distanceSquared(p.getLocation()) <= radius * radius)
				list.add(fakeEntity);
		}
		return list;
	}
	
	public FakeEntity getFakeEntityByObject(Player p, Object fake) throws NoSuchFakeEntityException {
		if(seesNoEntities(p))
			throw new NoSuchFakeEntityException();
		
		for(FakeEntity fakeEntity : getFakeEntites(p))
			if(fakeEntity.getNmsEntity() == fake) return fakeEntity;
		
		throw new NoSuchFakeEntityException();
	}
	
	public FakeEntity getFakeEntityByName(Player p, String name) throws NoSuchFakeEntityException {
		if(seesNoEntities(p))
			throw new NoSuchFakeEntityException();
		
		for(FakeEntity fakeEntity : getFakeEntites(p))
			if(ChatColor.stripColor(fakeEntity.getName()).equals(ChatColor.stripColor(name)))
				return fakeEntity;
		
		throw new NoSuchFakeEntityException();
	}
	
	public FakeEntity getFakeEntityByID(Player p, int entityID) throws NoSuchFakeEntityException {
		if(seesNoEntities(p))
			throw new NoSuchFakeEntityException();
		
		for(FakeEntity fakeEntity : getFakeEntites(p))
			if(fakeEntity.getEntityID() == entityID)
				return fakeEntity;
		
		throw new NoSuchFakeEntityException();
	}
}
