package AnotherExampleMod;

import AnotherExampleMod.Patch.SpawnPacketPatch;
import necesse.engine.util.GameRandom;

import java.awt.Color;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Duplicate of {@link necesse.gfx.GameColor}
 * stripped of unnecessary functions.
 * 0-9 and a-j are unavailable, being taken by
 * GameColor. That means we have access to k-z
 * giving us 16 codes.
 * Usage example in {@link SpawnPacketPatch}
 * @see AnotherExampleMod.Patch.GameColorPatch
 */
public enum ExampleGameColors {
    MCHAOS('k', () -> {
        //test this color out, its neat I think.
        //its a bit flash though
        float randomFactor = GameRandom.globalRandom.nextFloat();

        float minHue = 0.45F;
        float maxHue = 0.65F;

        float hue = minHue + (maxHue - minHue) * randomFactor;

        float saturation = 0.6F + GameRandom.globalRandom.nextFloat() * 0.4F;
        float brightness = 0.7F + GameRandom.globalRandom.nextFloat() * 0.3F;

        return new Color(Color.HSBtoRGB(hue, saturation, brightness));
    }),
    MPERMISSIONS('l', () -> {
        long timeScale = 2000L;
        long time = System.currentTimeMillis() % timeScale;
        double angle = Math.toRadians(360) * (double)time / (double)timeScale;

        double cosWave = Math.cos(angle);
        double sinWave = Math.sin(angle);

        float cosFactor = (float)((cosWave + 1.0) / 2.0);
        float sinFactor = (float)((sinWave + 1.0) / 2.0);

        float minBright = 0.85F;
        float maxBright = 1.0F;
        float minHue = 0.25F;
        float maxHue = 0.55F;

        float hue = minHue + (maxHue - minHue) * cosFactor;
        float brightness = minBright + (maxBright - minBright) * sinFactor;

        return new Color(Color.HSBtoRGB(hue, 0.8F, brightness));
    }),
    MPURP('m', () -> {
        long timeScale = 2000L;
        long time = System.currentTimeMillis() % timeScale;
        double angle = Math.toRadians(360) * (double)time / (double)timeScale;
        double sinWave = Math.sin(angle);

        float sinFactor = (float)((sinWave + 1.0) / 2.0);

        float minBright = 0.75F;
        float maxBright = 0.9F;
        float minHue = 0.73F;
        float maxHue = 0.78F;

        float hue = minHue + (maxHue - minHue) * sinFactor;
        float brightness = minBright + (maxBright - minBright) * sinFactor;

        return new Color(Color.HSBtoRGB(hue, 1.0F, brightness));
    }),
    MCHOPPY('n', () -> {
        //each full step is an additional color
        //so 4.0F would be 4 colors, 12.0F is 12
        float steps = 6.0F;
        long cycleDuration = 1000L;
        long time = System.currentTimeMillis() % cycleDuration;
        float timeFactor = (float)time / (float)cycleDuration;
        float scaledFactor = timeFactor * steps;

        float currentStep = (float)Math.floor(scaledFactor);

        float quantizedFactor = currentStep / steps;

        float minHue = 0.25F;
        float maxHue = 0.85F;
        float hue = minHue + (maxHue - minHue) * quantizedFactor;

        return new Color(Color.HSBtoRGB(hue, 1.0F, 1.0F));
    });
    public final char codeChar;
    public final Supplier<Color> color;
    public final Function<String, Supplier<Color>> colorParser;

    private ExampleGameColors(char codeChar, Color color) {
        this.codeChar = codeChar;
        this.colorParser = (s) -> () -> color;
        this.color = () -> color;
    }

    private ExampleGameColors(char codeChar, Supplier<Color> colorSupplier) {
        this.codeChar = codeChar;
        this.color = colorSupplier;
        this.colorParser = (s) -> colorSupplier;
    }

    public String getColorCode() {
        return "§" + this.codeChar;
    }
}
