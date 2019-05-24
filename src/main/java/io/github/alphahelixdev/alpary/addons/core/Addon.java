package io.github.alphahelixdev.alpary.addons.core;

import lombok.*;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public abstract class Addon {
	
	private final String name, description, author, version;
	private File dataFolder;
	private ClassLoader loader;
	private boolean loaded = false;
	
	public Addon(AddonInfo info) {
		this.name = info.name();
		this.description = info.description();
		this.author = info.author();
		this.version = info.version();
	}
	
	final void init(AddonClassLoader classLoader, File dataFolder) {
		this.dataFolder = dataFolder;
		this.loader = classLoader;
		this.onEnable();
		this.loaded = true;
	}
	
	public abstract void onEnable();
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface AddonInfo {
		String name();
		
		String description() default "";
		
		String author();
		
		String version() default "1.0";
	}
}
