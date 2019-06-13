package io.github.alphahelixdev.alpary.utils;

import com.mojang.authlib.GameProfile;
import io.github.alphahelixdev.alpary.Alpary;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;

@EqualsAndHashCode(callSuper = false)
@ToString
public class SkullUtil {
	
	public ItemStack getCustomSkull(String url) {
		GameProfile profile = Utils.skins().changeSkin(url);
		
		ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
		ItemMeta headMeta = head.getItemMeta();
		Class<?> headMetaClass = headMeta.getClass();
		
		try {
			Field f = headMetaClass.getDeclaredField("profile");
			f.setAccessible(true);
			
			f.set(headMeta, profile);
		} catch(NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		head.setItemMeta(headMeta);
		return head;
	}
	
	public ItemStack getPlayerSkull(String name) {
		ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, 1);
		SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
		
		try {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(Alpary.getInstance().mojangFetcher().getUUID(name).getId()));
        } catch (IOException ignored) {
		}
		itemStack.setItemMeta(meta);
		return itemStack;
	}
	
	@Getter
	@ToString
	@RequiredArgsConstructor
	public enum Skulls implements Serializable {
		ARROW_LEFT("MHF_ArrowLeft"),
		ARROW_RIGHT("MHF_ArrowRight"),
		ARROW_UP("MHF_ArrowUp"),
		ARROW_DOWN("MHF_ArrowDown"),
		QUESTION("MHF_Question"),
		EXCLAMATION("MHF_Exclamation"),
		CAMERA("FHG_Cam"),
		
		ZOMBIE_PIGMAN("MHF_PigZombie"),
		PIG("MHF_Pig"),
		SHEEP("MHF_Sheep"),
		BLAZE("MHF_Blaze"),
		CHICKEN("MHF_Chicken"),
		COW("MHF_Cow"),
		SLIME("MHF_Slime"),
		SPIDER("MHF_Spider"),
		SQUID("MHF_Squid"),
		VILLAGER("MHF_Villager"),
		OCELOT("MHF_Ocelot"),
		HEROBRINE("MHF_Herobrine"),
		LAVA_SLIME("MHF_LavaSlime"),
		MOOSHROOM("MHF_MushroomCow"),
		GOLEM("MHF_Golem"),
		GHAST("MHF_Ghast"),
		ENDERMAN("MHF_Enderman"),
		CAVE_SPIDER("MHF_CaveSpider"),
		
		CACTUS("MHF_Cactus"),
		CAKE("MHF_Cake"),
		CHEST("MHF_Chest"),
		MELON("MHF_Melon"),
		LOG("MHF_OakLog"),
		PUMPKIN("MHF_Pumpkin"),
		TNT("MHF_TNT"),
		DYNAMITE("MHF_TNT2");
		
		private final String id;
		
		public ItemStack getSkull() {
			return Utils.skulls().getPlayerSkull(this.getId());
		}
	}
	
	public static class LetterSkulls {
		
		public static ItemStack getSkull(SkullUtil.LetterSkulls.LetterSkull skull) {
			ItemStack item = Utils.skulls().getCustomSkull(skull.url());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(skull.letter());
			item.setItemMeta(meta);
			return item;
		}
		
		public enum Wood implements SkullUtil.LetterSkulls.LetterSkull {
			A {
				@Override
				public String letter() {
					return "A";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/a67d813ae7ffe5be951a4f41f2aa619a5e3894e85ea5d4986f84949c63d7672e";
				}
			},
			B {
				@Override
				public String letter() {
					return "B";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/50c1b584f13987b466139285b2f3f28df6787123d0b32283d8794e3374e23";
				}
			},
			C {
				@Override
				public String letter() {
					return "C";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/abe983ec478024ec6fd046fcdfa4842676939551b47350447c77c13af18e6f";
				}
			},
			D {
				@Override
				public String letter() {
					return "D";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/3193dc0d4c5e80ff9a8a05d2fcfe269539cb3927190bac19da2fce61d71";
				}
			},
			E {
				@Override
				public String letter() {
					return "E";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/dbb2737ecbf910efe3b267db7d4b327f360abc732c77bd0e4eff1d510cdef";
				}
			},
			F {
				@Override
				public String letter() {
					return "F";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/b183bab50a3224024886f25251d24b6db93d73c2432559ff49e459b4cd6a";
				}
			},
			G {
				@Override
				public String letter() {
					return "G";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/1ca3f324beeefb6a0e2c5b3c46abc91ca91c14eba419fa4768ac3023dbb4b2";
				}
			},
			H {
				@Override
				public String letter() {
					return "H";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/31f3462a473549f1469f897f84a8d4119bc71d4a5d852e85c26b588a5c0c72f";
				}
			},
			I {
				@Override
				public String letter() {
					return "I";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/46178ad51fd52b19d0a3888710bd92068e933252aac6b13c76e7e6ea5d3226";
				}
			},
			J {
				@Override
				public String letter() {
					return "J";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/3a79db9923867e69c1dbf17151e6f4ad92ce681bcedd3977eebbc44c206f49";
				}
			},
			K {
				@Override
				public String letter() {
					return "K";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/9461b38c8e45782ada59d16132a4222c193778e7d70c4542c9536376f37be42";
				}
			},
			L {
				@Override
				public String letter() {
					return "L";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/319f50b432d868ae358e16f62ec26f35437aeb9492bce1356c9aa6bb19a386";
				}
			},
			M {
				@Override
				public String letter() {
					return "M";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/49c45a24aaabf49e217c15483204848a73582aba7fae10ee2c57bdb76482f";
				}
			},
			N {
				@Override
				public String letter() {
					return "N";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/35b8b3d8c77dfb8fbd2495c842eac94fffa6f593bf15a2574d854dff3928";
				}
			},
			O {
				@Override
				public String letter() {
					return "O";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/d11de1cadb2ade61149e5ded1bd885edf0df6259255b33b587a96f983b2a1";
				}
			},
			P {
				@Override
				public String letter() {
					return "P";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/a0a7989b5d6e621a121eedae6f476d35193c97c1a7cb8ecd43622a485dc2e912";
				}
			},
			Q {
				@Override
				public String letter() {
					return "Q";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/43609f1faf81ed49c5894ac14c94ba52989fda4e1d2a52fd945a55ed719ed4";
				}
			},
			R {
				@Override
				public String letter() {
					return "R";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/a5ced9931ace23afc351371379bf05c635ad186943bc136474e4e5156c4c37";
				}
			},
			S {
				@Override
				public String letter() {
					return "S";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/3e41c60572c533e93ca421228929e54d6c856529459249c25c32ba33a1b1517";
				}
			},
			T {
				@Override
				public String letter() {
					return "T";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/1562e8c1d66b21e459be9a24e5c027a34d269bdce4fbee2f7678d2d3ee4718";
				}
			},
			U {
				@Override
				public String letter() {
					return "U";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/607fbc339ff241ac3d6619bcb68253dfc3c98782baf3f1f4efdb954f9c26";
				}
			},
			V {
				@Override
				public String letter() {
					return "V";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/cc9a138638fedb534d79928876baba261c7a64ba79c424dcbafcc9bac7010b8";
				}
			},
			W {
				@Override
				public String letter() {
					return "W";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/269ad1a88ed2b074e1303a129f94e4b710cf3e5b4d995163567f68719c3d9792";
				}
			},
			X {
				@Override
				public String letter() {
					return "X";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/5a6787ba32564e7c2f3a0ce64498ecbb23b89845e5a66b5cec7736f729ed37";
				}
			},
			Y {
				@Override
				public String letter() {
					return "Y";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/c52fb388e33212a2478b5e15a96f27aca6c62ac719e1e5f87a1cf0de7b15e918";
				}
			},
			Z {
				@Override
				public String letter() {
					return "Z";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/90582b9b5d97974b11461d63eced85f438a3eef5dc3279f9c47e1e38ea54ae8d";
				}
			},
			SPACE {
				@Override
				public String letter() {
					return " ";
				}
				
				@Override
				public String url() {
					return "http://textures.minecraft.net/texture/5db532b5cced46b4b535ece16eced7bbc5cac55594d61e8b8f8eac4299c9fc";
				}
			}
		}
		
		public interface LetterSkull {
			String letter();
			
			String url();
		}
	}
	
}
