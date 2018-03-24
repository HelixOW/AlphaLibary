package de.alphahelix.alphalibary.annotations.random;

import de.alphahelix.alphalibary.annotations.Accessor;
import de.alphahelix.alphalibary.core.utils.StringUtil;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Used to apply the random values
 *
 * @author AlphaHelix
 * @version 1.0
 * @see StringRandom
 * @see IntegerRandom
 * @see DoubleRandom
 * @see BooleanRandom
 * @see UUIDRandom
 * @since 1.9.2.1
 */
public class AnnotatedRandom {
	
	private final Object randomClazz;
	private final Field randomField;
	
	private final int length;
	private final Object strIntDouble;
	
	public AnnotatedRandom(Object randomClazz, Field randomField, StringRandom random) {
		this.randomClazz = randomClazz;
		this.randomField = randomField;
		
		this.length = random.length();
		this.strIntDouble = "";
	}
	
	public AnnotatedRandom(Object randomClazz, Field randomField, IntegerRandom random) {
		this.randomClazz = randomClazz;
		this.randomField = randomField;
		
		this.length = random.length();
		this.strIntDouble = -1;
	}
	
	public AnnotatedRandom(Object randomClazz, Field randomField, DoubleRandom random) {
		this.randomClazz = randomClazz;
		this.randomField = randomField;
		
		this.length = random.length();
		this.strIntDouble = -1.0;
	}
	
	public AnnotatedRandom(Object randomClazz, Field randomField, BooleanRandom random) {
		this.randomClazz = randomClazz;
		this.randomField = randomField;
		
		this.length = 1;
		this.strIntDouble = true;
	}
	
	public AnnotatedRandom(Object randomClazz, Field randomField, UUIDRandom random) {
		this.randomClazz = randomClazz;
		this.randomField = randomField;
		
		this.length = 1;
		this.strIntDouble = UUID.randomUUID();
	}
	
	final AnnotatedRandom apply() {
		try {
			if(strIntDouble instanceof String)
				Accessor.access(randomField).set(randomClazz, StringUtil.generateRandomString(length));
			else if(strIntDouble instanceof Integer)
				Accessor.access(randomField).set(randomClazz, ThreadLocalRandom.current().nextInt(length));
			else if(strIntDouble instanceof Double)
				Accessor.access(randomField).set(randomClazz, ThreadLocalRandom.current().nextDouble(length));
			else if(strIntDouble instanceof Boolean)
				Accessor.access(randomField).set(randomClazz, ThreadLocalRandom.current().nextBoolean());
			else if(strIntDouble instanceof UUID)
				Accessor.access(randomField).set(randomClazz, UUID.randomUUID());
		} catch(ReflectiveOperationException e) {
			e.printStackTrace();
		}
		
		return this;
	}
}
