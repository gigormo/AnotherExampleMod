package AnotherExampleMod.Commands;

import AnotherExampleMod.Buffs.ExampleBuff;
import AnotherExampleMod.ExampleSettings;
import AnotherExampleMod.Particles.ExampleParticle;
import necesse.engine.commands.CmdParameter;
import necesse.engine.commands.CommandLog;
import necesse.engine.commands.ModularChatCommand;
import necesse.engine.commands.parameterHandlers.IntParameterHandler;
import necesse.engine.commands.parameterHandlers.PresetStringParameterHandler;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.entity.mobs.buffs.ActiveBuff;
import necesse.entity.mobs.buffs.staticBuffs.Buff;

/**
 * Command that takes a buff duration {@link IntParameterHandler}, and a string yes/no {@link PresetStringParameterHandler}
 * Checks if user entered a time, checks if particle should emit(by default yes),
 * activate the buff, add it to playerMob
 * @see necesse.engine.commands.parameterHandlers Param Handlers
 * @see ExampleParticle ExampleParticle
 * @see ExampleBuff ExampleBuff
 */
public class BuffCommand extends ModularChatCommand {
    public BuffCommand() {
        super("examplebuff", "give client example buff", ExampleSettings.permissionLevel, false, new CmdParameter("Buff duration", new IntParameterHandler(10000), true), new CmdParameter("Enable Particle Effect?", new PresetStringParameterHandler("off", "on")));
    }

    @Override
    public void runModular(Client client, Server server, ServerClient serverClient, Object[] args, String[] errors, CommandLog commandLog) {
        int time = (int) args[0];
        ExampleSettings.shouldEmit = args[1] != "off";
        ActiveBuff activeBuff = new ActiveBuff((Buff) args[2], serverClient.playerMob, time, serverClient.playerMob);
        serverClient.playerMob.addBuff(activeBuff, true);
    }
}