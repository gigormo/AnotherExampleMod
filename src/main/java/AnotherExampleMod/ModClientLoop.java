package AnotherExampleMod;

import necesse.engine.GlobalData;
import necesse.engine.gameLoop.GameLoop;
import necesse.engine.gameLoop.GameLoopListener;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.network.client.Client;
import necesse.engine.network.server.ServerClient;
import necesse.engine.state.MainGame;
import necesse.engine.state.MainMenu;
import necesse.engine.state.State;
import necesse.engine.window.GameWindow;

public class ModClientLoop implements GameLoopListener {
    public ServerClient serverClient;
    public Client client;
    public boolean hasSentSpawnMessage;
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
        }
        if (serverClient != null) {
            if (!runOnce){
                serverClient.sendChatMessage("ServerClient Tick tick tick");
            }
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
            System.out.println("isServer : " + ModMain.isServer);
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

