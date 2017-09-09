/*
 * Copyright (C) <2017>  <AlphaHelixDev>
 *
 *       This program is free software: you can redistribute it under the
 *       terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.alphahelix.alphalibary.fakeapi;

import de.alphahelix.alphalibary.reflection.ReflectionUtil;

public enum FakeMobType {

    GUARDIAN("EntityGuardian", ReflectionUtil.getNmsClass("World")),
    SKELETON("EntitySkeleton", ReflectionUtil.getNmsClass("World")),
    ZOMBIE("EntityZombie", ReflectionUtil.getNmsClass("World")),
    HORSE("EntityHorse", ReflectionUtil.getNmsClass("World")),
    CREEPER("EntityCreeper", ReflectionUtil.getNmsClass("World")),
    SPIDER("EntitySpider", ReflectionUtil.getNmsClass("World")),
    GIANT("EntityGiantZombie", ReflectionUtil.getNmsClass("World")),
    SLIME("EntitySlime", ReflectionUtil.getNmsClass("World")),
    GHAST("EntityGhast", ReflectionUtil.getNmsClass("World")),
    PIGMAN("EntityPigZombie", ReflectionUtil.getNmsClass("World")),
    ENDERMAN("EntityEnderman", ReflectionUtil.getNmsClass("World")),
    CAVE_SPIDER("EntityCaveSpider", ReflectionUtil.getNmsClass("World")),
    SILVERFISH("EntitySilverfish", ReflectionUtil.getNmsClass("World")),
    BLAZE("EntityBlaze", ReflectionUtil.getNmsClass("World")),
    ENDER_DRAGON("EntityEnderDragon", ReflectionUtil.getNmsClass("World")),
    WITHER("EntityWither", ReflectionUtil.getNmsClass("World")),
    BAT("EntityBat", ReflectionUtil.getNmsClass("World")),
    WITCH("EntityWitch", ReflectionUtil.getNmsClass("World")),
    ENDERMITE("EntityEndermite", ReflectionUtil.getNmsClass("World")),
    PIG("EntityPig", ReflectionUtil.getNmsClass("World")),
    SHEEP("EntitySheep", ReflectionUtil.getNmsClass("World")),
    COW("EntityCow", ReflectionUtil.getNmsClass("World")),
    CHICKEN("EntityChicken", ReflectionUtil.getNmsClass("World")),
    SQUID("EntitySquid", ReflectionUtil.getNmsClass("World")),
    WOLF("EntityWolf", ReflectionUtil.getNmsClass("World")),
    MUSHROOM_COW("EntityMushroomCow", ReflectionUtil.getNmsClass("World")),
    SNOWMAN("EntitySnowman", ReflectionUtil.getNmsClass("World")),
    OCELOT("EntityOcelot", ReflectionUtil.getNmsClass("World")),
    IRON_GOLEM("EntityIronGolem", ReflectionUtil.getNmsClass("World")),
    RABBIT("EntityRabbit", ReflectionUtil.getNmsClass("World")),
    VILLAGER("EntityVillager", ReflectionUtil.getNmsClass("World")),
    LLAMA("EntityLlama", ReflectionUtil.getNmsClass("World")),
    ILLAGER("EntityIllagerWizard", ReflectionUtil.getNmsClass("World")),
    VINDICATOR("EntityVindicator", ReflectionUtil.getNmsClass("World")),
    EVOKER("EntityEvoker", ReflectionUtil.getNmsClass("World")),
    ILLUSIONER("EntityIllagerIllusioner", ReflectionUtil.getNmsClass("World")),
    PARROT("EntityParrot", ReflectionUtil.getNmsClass("World"));

    private String nmsClass;
    private Class<?>[] classes;

    FakeMobType(String nmsClass, Class<?>... classes) {
        this.nmsClass = nmsClass;
        this.classes = classes;
    }

    public String getNmsClass() {
        return nmsClass;
    }

    public ReflectionUtil.SaveConstructor getConstructor() {
        return ReflectionUtil.getDeclaredConstructor(nmsClass, classes);
    }

    @Override
    public String toString() {
        return "FakeMobType{" +
                "nmsClass='" + nmsClass + '\'' +
                '}';
    }
}
