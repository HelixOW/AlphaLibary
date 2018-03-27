package de.alphahelix.alphalibary.core.utils.abstracts;

import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.core.utilites.Utility;
import de.alphahelix.alphalibary.core.utils.implementations.ISkinChangeUtil;
import org.apache.commons.codec.binary.Base64;

@Utility(implementation = ISkinChangeUtil.class)
public abstract class AbstractSkinChangeUtil {
	
	private static final Base64 BASE64 = new Base64();
	public static AbstractSkinChangeUtil instance;
	
	public static Base64 getBASE64() {
		return BASE64;
	}
	
	public abstract GameProfile changeSkin(String url);
}
