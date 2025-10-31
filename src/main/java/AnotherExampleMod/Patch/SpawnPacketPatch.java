package AnotherExampleMod.Patch;

import AnotherExampleMod.ExampleGameColors;
import AnotherExampleMod.ModMain;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.packet.PacketSpawnPlayer;
import necesse.engine.network.server.ServerClient;
import net.bytebuddy.asm.Advice;

//Thanks Jakapoa in the Necesse discord for giving me this

/**
 * Patches {@link ServerClient#submitSpawnPacket}
 * adding our own spawn messages
 *
 * @see ServerClient#submitSpawnPacket(PacketSpawnPlayer)
 */
@ModMethodPatch(target = ServerClient.class, name = "submitSpawnPacket", arguments = {PacketSpawnPlayer.class})
public class SpawnPacketPatch {

    @Advice.OnMethodExit
    static void onExit(@Advice.This ServerClient serverClient, @Advice.Argument(0) PacketSpawnPlayer player) {
        //No need to run our logic on a server
        if (ModMain.isServer)
            return;
        if (!ModMain.modClientLoop.hasSentSpawnMessage) runInitialSetup(serverClient);
    }

    public static void runInitialSetup(ServerClient serverClient) {
        if (ModMain.modClientLoop != null) ModMain.modClientLoop.serverClient = serverClient;
        sendWelcomeMessages(serverClient);
        if (ModMain.isDevMode) {
            for (ExampleGameColors gc : ExampleGameColors.values()) {
                sendColorTestMessage(serverClient, gc);
            }
        }
        ModMain.modClientLoop.hasSentSpawnMessage = true;
    }

    public static void sendWelcomeMessages(ServerClient serverClient) {
        serverClient.sendChatMessage(ExampleGameColors.MPURP.getColorCode() + "Thank you bunches for using the mod!!, your super awesome " + serverClient.playerMob.playerName);
    }

    public static void sendColorTestMessage(ServerClient serverClient, ExampleGameColors color) {
        serverClient.sendChatMessage(color.getColorCode() + "Color code testing - Color: " + color.name());
    }
}

