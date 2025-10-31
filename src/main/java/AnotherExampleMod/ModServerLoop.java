package AnotherExampleMod;

import necesse.engine.gameLoop.GameLoop;
import necesse.engine.gameLoop.GameLoopListener;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.network.server.Server;
import necesse.engine.window.GameWindow;

public class ModServerLoop implements GameLoopListener {
    public Server server;
    private boolean isDisposed = false;
    private boolean runOnce;

    /**
     * called from {@link ModServerLoop#frameTick(TickManager, GameWindow) frameTick}
     * gameTick logic should be handled here, runs at 20tps
     *
     * @see TickManager#tickLogic()
     * @see TickManager#ticksPerSec
     */
    public void gameTick() {
        if (!runOnce) {
            System.out.println("Server gametick example print");
            runOnce = true;
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
        if (tickManager.isGameTick()) gameTick();
    }


    public void getClientLoop(){

    }

    public void dispose() {
        this.isDisposed = true;
    }

    public boolean isDisposed() {
        return this.isDisposed;
    }
}

