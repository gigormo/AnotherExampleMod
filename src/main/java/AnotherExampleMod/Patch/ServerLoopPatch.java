package AnotherExampleMod.Patch;

import AnotherExampleMod.Events.ModLoopEvent;
import AnotherExampleMod.ModMain;
import AnotherExampleMod.ModServerLoop;
import necesse.engine.GameEvents;
import necesse.engine.gameLoop.ServerGameLoop;
import necesse.engine.modLoader.annotations.ModConstructorPatch;
import necesse.engine.network.server.Server;
import net.bytebuddy.asm.Advice;

/**
 * Patches {@link ServerGameLoop} constructor triggering {@link ModLoopEvent},
 * also sets {@link ModServerLoop#server ModServerLoop.server}
 */
@ModConstructorPatch(target = ServerGameLoop.class, arguments = {Server.class, String.class, int.class})
public class ServerLoopPatch {
    @Advice.OnMethodExit
    static void onExit(@Advice.This ServerGameLoop serverGameLoop, @Advice.Argument(0) Server server) {
        if (ModMain.isDevMode) {
            System.out.println("Trigger server loop event : " + serverGameLoop.getName());
        }
        GameEvents.triggerEvent(new ModLoopEvent(serverGameLoop, server));
    }
}
