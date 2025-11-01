package AnotherExampleMod.Commands;

import AnotherExampleMod.ExampleSettings;
import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.parameterHandlers.PresetStringParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;

/**
 * Command that enables {@link ExampleSettings#shouldPatchInteractRange}.
 * <p>
 * The command takes a parameter handled by
 * {@link PresetStringParameterHandler}.
 * Valid arguments are "off" and "on". NonOptional
 */
public class InteractPatchCommand extends ModularChatCommand {
    public InteractPatchCommand() {
        super("toggleinteractpatch", "Toggles Interaction Distance Patch", ExampleSettings.permissionLevel, false, new CmdParameter("Toggle", new PresetStringParameterHandler("off", "on")));
    }

    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog commandLog) {
        ExampleSettings.shouldPatchInteractRange = args[0] == "on";
    }
}

