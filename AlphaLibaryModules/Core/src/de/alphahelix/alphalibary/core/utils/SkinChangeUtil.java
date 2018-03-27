package de.alphahelix.alphalibary.core.utils;

import com.mojang.authlib.GameProfile;
import de.alphahelix.alphalibary.core.utils.abstracts.AbstractSkinChangeUtil;

public interface SkinChangeUtil {
	
	static GameProfile changeSkin(String url) {
		return AbstractSkinChangeUtil.instance.changeSkin(url);
	}
}
