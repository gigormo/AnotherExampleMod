package AnotherExampleMod.Patch;

import AnotherExampleMod.ModMain;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import net.bytebuddy.asm.Advice;

/**
 * test patch to show what happens when OnMethodEnter is used with skipOn returning true.
 * this patch will likely run first, causing {@link tPatch1} to be skipped. In case it isn't clear
 * the original method will also be skipped
 */
@ModMethodPatch(target = GameObject.class, name = "getInteractRange", arguments = {Level.class, int.class, int.class})
public class tPatch2 {
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    static boolean onEnter(@Advice.This GameObject gameObject) {
        if (ModMain.runTestPatches) {
            System.out.println("Patch 1 onEnter called");
            return true;
        }
        return false;
    }
}
