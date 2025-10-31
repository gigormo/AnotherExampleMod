package AnotherExampleMod.Commands;

import AnotherExampleMod.ExampleGameColors;
import AnotherExampleMod.ExampleSettings;
import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.parameterHandlers.IntParameterHandler;
import necesse.engine.commands.parameterHandlers.PresetStringParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.gfx.GameColor;

/**
 * An example command
 */
public class ExampleCommand extends ModularChatCommand {
    public ExampleCommand() {
        super("examplecommand", "example command", ExampleSettings.permissionLevel, false, new CmdParameter("Example Int param", new IntParameterHandler(10000), true), new CmdParameter("Example Preset string param", new PresetStringParameterHandler("off", "on"), true));
    }

    @Override
    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog commandLog) {
        // command can run from a server technically. this only shows a method
        // to check if command is ran on server without checking isServer
        if (serverClient == null) {
            commandLog.add(GameColor.RED.getColorCode() + "Command cannot be run from server.");
            return;
        }
        commandLog.add(ExampleGameColors.MCHAOS.getColorCode() + "Command ran");
    }
}
