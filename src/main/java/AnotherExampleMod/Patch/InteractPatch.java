package AnotherExampleMod.Patch;

import AnotherExampleMod.ExampleSettings;
import AnotherExampleMod.ModMain;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import net.bytebuddy.asm.Advice;

/**
 * Patches {@link GameObject#getInteractRange(Level, int, int)}
 * to return 5000 of shouldPatchInteractRange is true
 */
@ModMethodPatch(target = GameObject.class, name = "getInteractRange", arguments = {Level.class, int.class, int.class})
public class InteractPatch {
    @Advice.OnMethodExit
    static int onExit(@Advice.Return(readOnly = false) int range) {
        if (ExampleSettings.shouldPatchInteractRange) {
            range = 5000;
            if (ModMain.isDevMode) {
                System.out.println("Interaction range patched, range is now : " + range);
            }
        }
        return range;
    }
}
