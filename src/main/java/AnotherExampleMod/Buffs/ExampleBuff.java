package AnotherExampleMod.Buffs;

import AnotherExampleMod.ExampleSettings;
import AnotherExampleMod.Particles.ExampleParticle;
import necesse.engine.util.GameRandom;
import necesse.entity.manager.EntityManager;
import necesse.entity.mobs.Mob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.BuffEventSubscriber;
import necesse.entity.mobs.buffs.BuffModifiers;
import necesse.entity.mobs.buffs.staticBuffs.Buff;
import necesse.entity.particle.Particle;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTexture.GameTextureSection;

/**
 * Example buff, {@link ExampleBuff#texture} is our buff icon,
 * inside init the modifiers are set for the buff. Inside
 * {@link ExampleBuff#clientTick(ActiveBuff) clientTick}
 * the particle effect is added to the buff.
 */
public class ExampleBuff extends Buff {
    public static GameTexture texture;
    public static GameTextureSection particleTexture;

    public ExampleBuff() {
        canCancel = true;
        isVisible = true;
        shouldSave = true;
    }


    @Override
    public void init(ActiveBuff activeBuff, BuffEventSubscriber buffEventSubscriber) {
        activeBuff.setModifier(BuffModifiers.BUILD_RANGE, 5.0f);
        activeBuff.setModifier(BuffModifiers.BUILDING_SPEED, 1.5f);
        activeBuff.setModifier(BuffModifiers.MINING_SPEED, 2.0f);
        activeBuff.setModifier(BuffModifiers.MINING_RANGE, 2.0f);
        activeBuff.setModifier(BuffModifiers.TOOL_DAMAGE, 2.0f);
        activeBuff.setModifier(BuffModifiers.SPELUNKER, true);
        activeBuff.setModifier(BuffModifiers.TREASURE_HUNTER, true);
        activeBuff.setModifier(BuffModifiers.MAX_SUMMONS, 10);
        activeBuff.setModifier(BuffModifiers.ITEM_PICKUP_RANGE, 20.0f);
    }

    @Override
    public void serverTick(ActiveBuff buff) {
    }

    @Override
    public void clientTick(ActiveBuff buff) {
        EntityManager manager = buff.owner.getLevel().entityManager;
        Mob owner = buff.owner;

        //Particle effect adds light...
        if (ExampleSettings.shouldEmit) {
            float height = GameRandom.globalRandom.getFloatBetween(4.0F, 10.0F);
            //Add the particle
            manager.addParticle(new ExampleParticle(manager.level,
                            particleTexture, owner.x, owner.y,
                            (float) (GameRandom.globalRandom.nextGaussian() * 12.0),
                            (float) (GameRandom.globalRandom.nextGaussian() * 12.0)),
                    Particle.GType.COSMETIC);
            /*
            Simple particle example that does not require creating a new particle effect.
            manager.addParticle(owner.x + (float) (GameRandom.globalRandom.nextGaussian() * 12.0),
                    owner.y + (float) (GameRandom.globalRandom.nextGaussian() * 2.0),
                    Particle.GType.COSMETIC).movesConstant(owner.dx / 10.0F, owner.dy / 8.0F).color(new Color(182, 66, 245)).sizeFades(8, 18).height(height).givesLight(135).lifeTime(120);
                     */
        }
    }
}
