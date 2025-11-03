package AnotherExampleMod;

import AnotherExampleMod.Buffs.ExampleBuff;
import necesse.engine.GlobalData;
import necesse.engine.gameLoop.GameLoop;
import necesse.engine.gameLoop.GameLoopListener;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.network.client.Client;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.state.MainGame;
import necesse.engine.state.MainMenu;
import necesse.engine.state.State;
import necesse.engine.window.GameWindow;
import necesse.entity.mobs.buffs.ActiveBuff;

public class ModClientLoop implements GameLoopListener {
    public Client client;
    private boolean isDisposed = false;
    private boolean runOnce;
    /**
     * called from {@link ModClientLoop#frameTick(TickManager, GameWindow) frameTick}
     * gameTick logic should be handled here, runs at 20tps
     *
     * @see TickManager#tickLogic()
     * @see TickManager#ticksPerSec
     */
    public void gameTick() {
        if (client == null){
            client = getGameClient();
        } else if (!runOnce){
            client.chat.addMessage("Client tick tick");
            runOnce = true;
            System.out.println(client.getPlayer());
        }
    }

    public void drawTick(TickManager tickManager) {

    }

    /**
     * logic that needs to run every frame should be here.
     *
     * @see GameLoop#runGameLoopListenersFrameTick(GameWindow) GameLoop.runGameLoopListenersFrameTick
     */
    public void frameTick(TickManager tickManager, GameWindow gameWindow) {
        if (ExampleControl.exampleKey1.isPressed()) {
            /*
            System.out.println("Client slot: " + client.getSlot() + " :PlayerMob slot: " + client.getPlayer(client.getPlayer().getPlayerSlot()));
            //the slot we need is not client.slot, its the slot referenced inside playermob
            client.network.sendPacket(new PacketExample(client.getPlayer().getPlayerSlot(), "Example client packet"));
             */
            ActiveBuff activeBuff = new ActiveBuff(BuffRegistry.getBuff("examplebuff"), client.getPlayer(), 1000 * 600, client.getPlayer());
            client.getPlayer().addBuff(activeBuff, true);

        } else if (ExampleControl.exampleKey2.isPressed()) {
            if (ModMain.isDevMode)
                System.out.println("Example key 2 pressed");
            ExampleSettings.shouldPatchInteractRange = !ExampleSettings.shouldPatchInteractRange;
        }
        if (tickManager.isGameTick()) gameTick();
    }

    /**
     * Gets the game client
     *
     * @return {@link Client}
     */
    public Client getGameClient() {
        State currentState = GlobalData.getCurrentState();
        return currentState instanceof MainGame ? ((MainGame) currentState).getClient() : ((MainMenu) currentState).getClient();
    }

    public void dispose() {
        this.isDisposed = true;
    }

    public boolean isDisposed() {
        return this.isDisposed;
    }
}

