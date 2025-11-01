package AnotherExampleMod.Commands;

import AnotherExampleMod.ExampleGameColors;
import AnotherExampleMod.ExampleSettings;
import AnotherExampleMod.ModMain;
import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.parameterHandlers.IntParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.packet.PacketUpdateTotalItemSets;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.gfx.GameColor;

/**
 * Command that allows a client to increase its total sets,
 * takes an {@link IntParameterHandler} for the set amount.
 * max sets amount is here {@link ExampleSettings#maxSets}
 */
public class TotalSetsCommand extends ModularChatCommand {

    public TotalSetsCommand() {
        super("settotalsets", "increase equipment slots", ExampleSettings.permissionLevel, false, new CmdParameter("setAmount", new IntParameterHandler()));
    }

    @Override
    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog commandLog) {
        int i = (int) args[1];
        if (i > ExampleSettings.maxSets) {
            if (client != null) {
                commandLog.add(GameColor.RED.getColorCode() + "Set amount cannot exceed : " + ExampleSettings.maxSets);
                return;
            }
            if (ModMain.isDevMode) {
                System.out.println("totalsets command exceeds mods maxSets limit." + ExampleSettings.maxSets + " is the current max");
            }
            return;
        }
        serverClient.playerMob.getInv().equipment.changeTotalItemSets(i);
        sendPacket(server, serverClient);
        commandLog.add(ExampleGameColors.MPURP.getColorCode() + "Set Count Set : " + i);
    }

    private void sendPacket(Server server, ServerClient serverClient) {
        serverClient.playerMob.equipmentBuffManager.updateAll();
        serverClient.closeContainer(false);
        serverClient.updateInventoryContainer();
        server.network.sendToAllClients(new PacketUpdateTotalItemSets(serverClient));
    }
}