package AnotherExampleMod.Patch;

import AnotherExampleMod.Events.ModLoopEvent;
import AnotherExampleMod.ModMain;
import necesse.engine.GameEvents;
import necesse.engine.gameLoop.ClientGameLoop;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import net.bytebuddy.asm.Advice;

/**
 * Patches {@link ClientGameLoop#init()} triggering {@link ModLoopEvent}
 */

@ModMethodPatch(target = ClientGameLoop.class, name = "init", arguments = {})
public class ClientLoopPatch {
    @Advice.OnMethodExit
    static void onExit(@Advice.This ClientGameLoop clientGameLoop, @Advice.FieldValue("firstTick") boolean b) {
        if (ModMain.isDevMode) {
            System.out.println("Trigger client loop event : " + clientGameLoop.getName());
        }
        GameEvents.triggerEvent(new ModLoopEvent(clientGameLoop));
    }
}