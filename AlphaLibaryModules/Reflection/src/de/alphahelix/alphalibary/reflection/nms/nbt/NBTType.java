package de.alphahelix.alphalibary.reflection.nms.nbt;

public enum NBTType {
	END(0),
	BYTE(1),
	SHORT(2),
	INT(3),
	LONG(4),
	FLOAT(5),
	DOUBLE(6),
	BYTE_ARRAY(7),
	STRING(8),
	LIST(9),
	COMPOUND(10),
	INT_ARRAY(11);
	
	
	private final int id;
	
	NBTType(int id) {
		this.id = id;
	}
	
	public static NBTType valueOf(int id) {
		for(NBTType type : values())
			if(type.id == id)
				return type;
		return END;
	}
	
	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		return "NBTType{" +
				"id=" + id +
				'}';
	}
}
