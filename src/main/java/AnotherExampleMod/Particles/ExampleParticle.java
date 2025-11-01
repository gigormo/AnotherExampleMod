package AnotherExampleMod.Particles;

import AnotherExampleMod.Buffs.ExampleBuff;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.util.GameMath;
import necesse.engine.util.GameRandom;
import necesse.entity.mobs.PlayerMob;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.particle.Particle;
import necesse.gfx.camera.GameCamera;
import necesse.gfx.drawOptions.texture.TextureDrawOptionsEnd;
import necesse.gfx.drawables.EntityDrawable;
import necesse.gfx.drawables.LevelSortedDrawable;
import necesse.gfx.drawables.OrderableDrawables;
import necesse.gfx.gameTexture.GameTextureSection;
import necesse.level.maps.Level;
import necesse.level.maps.light.GameLight;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * An example particle effect that utilizes a {@link GameTextureSection}.
 * In EntityDrawable notice we use {@link EntityDrawable#getSortY()}
 * to sort the particle texture behind the player in our case
 *
 * @see necesse.entity.particle.FleshParticle FleshParticle
 * @see GameTextureSection GameTextureSection
 * @see ExampleBuff#clientTick(ActiveBuff) Particle use example
 */
public class ExampleParticle extends Particle {
    private final GameTextureSection sprite;
    public float rotation;
    public boolean mirrored;
    public float height;

    public float dh;

    //Taken from FleshParticle <3
    public ExampleParticle(Level level, GameTextureSection sprite, float x, float y, float knockbackX, float knockbackY) {
        super(level, x, y, 500L);

        long max = 850L;
        this.lifeTime = GameRandom.globalRandom.nextLong(max);
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.friction = 0.85F;
        this.rotation = GameRandom.globalRandom.nextInt(360);
        this.mirrored = GameRandom.globalRandom.nextBoolean();
        this.dx = (float) GameRandom.globalRandom.nextGaussian() * 5.0F;
        this.dy = (float) GameRandom.globalRandom.nextGaussian() * 5.0F;
        Point2D.Float normVec = GameMath.normalize(knockbackX, knockbackY);
        this.dx += normVec.x * 15.0F;
        this.dy += normVec.y * 15.0F;
        this.hasCollision = false;
        //this.collision = new Rectangle(-5, -5, 10, 10);
        this.collision = new Rectangle(0, 0);
        this.height = GameRandom.globalRandom.getFloatBetween(-12.0F, 8.0F);
        this.dh = GameRandom.globalRandom.getFloatBetween(20.0F, 30.0F);
    }

    @Override
    public void addDrawables(java.util.List<LevelSortedDrawable> list, OrderableDrawables orderableDrawables, OrderableDrawables orderableDrawables1, OrderableDrawables orderableDrawables2, Level level, TickManager tickManager, GameCamera gameCamera, PlayerMob playerMob) {
        float cycle = getLifeCyclePercent();
        if (removed()) return;
        GameLight light = level.getLightLevel(this);
        int halfWidth = this.sprite.getWidth() / 2;
        int halfHeight = this.sprite.getHeight() / 2;
        int drawX = gameCamera.getDrawX(this.x) - halfWidth;
        int drawY = gameCamera.getDrawY(this.y) - halfHeight + 4 - (int) Math.max(0.0F, this.height);
        float alpha = 1.0F;
        TextureDrawOptionsEnd options = this.sprite.initDraw().light(light).alpha(alpha).rotate(this.rotation, halfWidth, halfHeight).mirror(this.mirrored, false).pos(drawX, drawY);
        if (cycle < 0.4f) {
            options.colorMult(Color.WHITE);
        } else if (cycle >= 0.4f && cycle <= 0.7f) {
            options.alpha(Math.abs(cycle - 1.0F) * 2.0F);
            options.colorMult(Color.MAGENTA);
        } else if (cycle > 0.7f) {
            options.alpha(Math.abs(cycle - 1.0F) * 3.0F);
            options.colorMult(Color.MAGENTA.darker().darker());
        }
        list.add(new EntityDrawable(this) {
            @Override
            public int getSortY() {
                // Basically where this will be sorted on the Y axis (when it will be behind the player etc.)
                // Should be in [0 - 32] range
                return 16;
            }

            @Override
            public void draw(TickManager tickManager) {
                options.draw();
            }
        });
    }
}
