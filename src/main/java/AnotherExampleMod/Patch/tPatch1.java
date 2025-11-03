package AnotherExampleMod.Patch;

import AnotherExampleMod.ModMain;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.level.gameObject.GameObject;
import necesse.level.maps.Level;
import net.bytebuddy.asm.Advice;

/**
 * test patch to show what happens when OnMethodEnter is used with skipOn returning true.
 * this patch will be skipped to due to {@link tPatch2} returning true in OnEnter.
 */

@ModMethodPatch(target = GameObject.class, name = "getInteractRange", arguments = {Level.class, int.class, int.class})
public class tPatch1 {
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    static boolean onEnter(@Advice.This GameObject gameObject) {
        if (ModMain.runTestPatches) {
            System.out.println("Patch 1 onEnter called");
            return true;
        }
        return false;
    }
}
