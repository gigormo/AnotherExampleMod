package AnotherExampleMod.Patch;

import AnotherExampleMod.ExampleGameColors;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.gfx.GameColor;
import net.bytebuddy.asm.Advice;

import java.awt.*;
import java.util.function.Supplier;

/**
 * Patch {@link GameColor#parseColorSupplierString(String)}
 * checks if the string matches one from mods color enum
 * @see ExampleGameColors
 */
@ModMethodPatch(target = GameColor.class, name = "parseColorSupplierString", arguments = {String.class})
public class GameColorPatch {
    @Advice.OnMethodExit
    static Supplier<Color> onExit(@Advice.Return(readOnly = false) Supplier<Color> color, @Advice.Argument(0) String str) {
        if (color != null) {
            return color;
        }
        if (str.charAt(0) == 167) {
            char code = str.charAt(1);
            ExampleGameColors[] colors = ExampleGameColors.values();

            for (ExampleGameColors gc : colors) {
                if (gc.codeChar == code) {
                    color = gc.colorParser.apply(str.substring(2));
                    return color;
                }
            }
        }
        return color;
    }
}