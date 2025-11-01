package AnotherExampleMod.Commands;

import AnotherExampleMod.ExampleGameColors;
import AnotherExampleMod.ExampleSettings;
import AnotherExampleMod.ModMain;
import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.parameterHandlers.IntParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.packet.PacketUpdateTrinketSlots;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.gfx.GameColor;

/**
 * Command that allows a client to increase its trinket slots,
 * takes an {@link IntParameterHandler} for the trinket amount.
 * max trinket amount is here {@link ExampleSettings#maxTrinkets}
 */
public class TrinketSlotCommand extends ModularChatCommand {

    public TrinketSlotCommand() {
        super("settrinketslots", "increase trinket slots", ExampleSettings.permissionLevel, false, new CmdParameter("trinketAmount", new IntParameterHandler()));
    }

    @Override
    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog commandLog) {
        int i = (int) args[0];
        if (i > ExampleSettings.maxTrinkets) {
            if (client != null) {
                commandLog.add(GameColor.RED.getColorCode() + "Trinket slots cannot exceed : " + ExampleSettings.maxTrinkets);
                return;
            }
            if (ModMain.isDevMode) {
                System.out.println("trinketslots command exceeds mods maxTrinkets limit. " + ExampleSettings.maxTrinkets + " is the current max");

            }
            return;
        }
        serverClient.playerMob.getInv().equipment.changeTrinketSlotsSize(i);
        sendPacket(server, serverClient);
        commandLog.add(ExampleGameColors.MPURP.getColorCode() + "Trinket Slots Set : " + i);
    }


    private void sendPacket(Server server, ServerClient serverClient) {
        serverClient.playerMob.equipmentBuffManager.updateAll();
        serverClient.closeContainer(false);
        serverClient.updateInventoryContainer();
        server.network.sendToAllClients(new PacketUpdateTrinketSlots(serverClient));
    }
}