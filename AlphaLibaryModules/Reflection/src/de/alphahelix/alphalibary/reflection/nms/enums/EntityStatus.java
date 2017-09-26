package de.alphahelix.alphalibary.reflection.nms.enums;

public enum EntityStatus {

    PLAY_GUARDIAN_SOUND(21),
    PLAY_TOTEM_OF_UNDYING(35),
    SNOWBALL_POOF(3),
    EGG_CRACK(3),
    HOOK_PULL(31),
    TIPPED_ARROW_PARTICLES(0),
    FIREWORK_TRIGGER(17),
    LIVING_HURT(2),
    LIVING_DEATH(3),
    LIVING_SHIELD_BLOCK(29),
    LIVING_SHIELD_BREAK(30),
    LIVING_THORNS(33),
    PLAYER_ITEM_FINISHED(9),
    PLAYER_ENABLE_REDUCED_DEBUG(22),
    PLAYER_DISABLE_REDUCED_DEBUG(23),
    PLAYER_OP_PERM_0(24),
    PLAYER_OP_PERM_1(25),
    PLAYER_OP_PERM_2(26),
    PLAYER_OP_PERM_3(27),
    PLAYER_OP_PERM_4(28),
    ARMORSTAND_HIT(32),
    INSENTIENT_EXPLOSION(20),
    SQUID_RESET(19),
    ANIMAL_LOVE(18),
    RABBIT_JUMP(1),
    SHEEP_EAT(10),
    TAIM_FAIL(6),
    TAIM_SUCCESS(7),
    WOLF_SHAKING(8),
    VILLAGER_MATING(12),
    VILLAGER_ANGRY(13),
    VILLAGER_HAPPY(14),
    IRON_GOLEM_ATTACK(4),
    IRON_GOLEM_SHOW_PUPPY(11),
    IRON_GOLEM_HIDE_PUPPY(34),
    EVOCATION_FANGS_ATTACK(4),
    WITCH_PARTICLES(15),
    ZOMBIE_VILLAGER_CURE(16),
    SPAWNER_RESET_DELAY(1);

    private final byte id;

    EntityStatus(byte id) {
        this.id = id;
    }

    EntityStatus(int i) {
        this((byte) i);
    }

    public byte getId() {
        return id;
    }

    @Override
    public String toString() {
        return "EntityStatus{" +
                "id=" + id +
                '}';
    }
}
