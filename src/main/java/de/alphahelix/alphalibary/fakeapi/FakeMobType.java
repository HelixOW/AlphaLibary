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

public enum FakeMobType {

    GUARDIAN("EntityGuardian"),
    SKELETON("EntitySkeleton"),
    ZOMBIE("EntityZombie"),
    HORSE("EntityHorse"),
    CREEPER("EntityCreeper"),
    SPIDER("EntitySpider"),
    GIANT("EntityGiantZombie"),
    SLIME("EntitySlime"),
    GHAST("EntityGhast"),
    PIGMAN("EntityPigZombie"),
    ENDERMAN("EntityEnderman"),
    CAVE_SPIDER("EntityCaveSpider"),
    SILVERFISH("EntitySilverfish"),
    BLAZE("EntityBlaze"),
    ENDER_DRAGON("EntityEnderDragon"),
    WITHER("EntityWither"),
    BAT("EntityBat"),
    WITCH("EntityWitch"),
    ENDERMITE("EntityEndermite"),
    PIG("EntityPig"),
    SHEEP("EntitySheep"),
    COW("EntityCow"),
    CHICKEN("EntityChicken"),
    SQUID("EntitySquid"),
    WOLF("EntityWolf"),
    MUSHROOM_COW("EntityMushroomCow"),
    SNOWMAN("EntitySnowman"),
    OCELOT("EntityOcelot"),
    IRON_GOLEM("EntityIronGolem"),
    RABBIT("EntityRabbit"),
    VILLAGER("EntityVillager"),
    LLAMA("EntityLlama"),
    ILLAGER("EntityIllagerWizard"),
    VINDICATOR("EntityVindicator"),
    EVOKER("EntityEvoker"),
    ILLUSIONER("EntityIllagerIllusioner");

    private String nmsClass;

    FakeMobType(String nmsClass) {
        this.nmsClass = nmsClass;
    }

    public String getNmsClass() {
        return nmsClass;
    }

    @Override
    public String toString() {
        return "FakeMobType{" +
                "nmsClass='" + nmsClass + '\'' +
                '}';
    }
}
