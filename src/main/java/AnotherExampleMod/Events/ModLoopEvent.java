package AnotherExampleMod.Events;

import necesse.engine.events.GameEvent;
import necesse.engine.gameLoop.GameLoop;
import necesse.engine.network.server.Server;

/**
 * Event used to get the gameloop
 */
public class ModLoopEvent extends GameEvent {
    public final GameLoop gameloop;
    public final Server server;


    public ModLoopEvent(GameLoop gameloop, Server server) {
        this.gameloop = gameloop;
        this.server = server;
    }
    public ModLoopEvent(GameLoop gameloop) {
        this.gameloop = gameloop;
        this.server = null;
    }
}
