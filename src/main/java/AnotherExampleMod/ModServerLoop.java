package AnotherExampleMod;

import AnotherExampleMod.Packet.PacketExample;
import necesse.engine.GameEventListener;
import necesse.engine.GameEvents;
import necesse.engine.events.ServerClientConnectedEvent;
import necesse.engine.events.ServerClientDisconnectEvent;
import necesse.engine.gameLoop.GameLoop;
import necesse.engine.gameLoop.GameLoopListener;
import necesse.engine.gameLoop.tickManager.TickManager;
import necesse.engine.network.server.Server;
import necesse.engine.window.GameWindow;

import java.util.Objects;

import static AnotherExampleMod.ModMain.isDevMode;

public class ModServerLoop implements GameLoopListener {
    public boolean hasSentSpawnMessage;
    private boolean isDisposed = false;
    private boolean runOnce;
    private Server server;
    private long lastRunTime = 0;

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
        if (isDevMode) {
            if (System.currentTimeMillis() >= lastRunTime + 10000) {
                
                //streamClients findAny ifPresent example
                server.streamClients().findAny().ifPresent(c -> {
                    c.sendPacket(new PacketExample("streamClients findAny example: " + c.getName()));
                });

                //streamClients find by name ifPresent example
                server.streamClients().filter(c -> c.playerMob.playerName.equals("Krunchy")).findFirst().ifPresent(c -> {
                    c.sendPacket(new PacketExample("streamClients filter by player name example: " + c.getName()));
                    System.out.println(c);
                });
                server.network.sendToAllClients(new PacketExample("Example server packet"));

                lastRunTime = System.currentTimeMillis();
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
        if (tickManager.isGameTick()) gameTick();
    }

    /**
     * Retrieves the current {@link Server} instance.
     *
     * @return The current Server instance, or {@code null}
     * if the server has not yet been initialized
     */
    public Server getServer() {
        if (server != null) {
            return server;
        } else {
            System.err.println("ModServerLoop Error: Server instance is null");
            return null;
        }
    }

    /**
     * Set the server instance
     *
     * @param server A {@link Server} instance
     */
    public void setServer(Server server) {
        this.server = server;
    }

    public void addServerEventListeners() {
        GameEvents.addListener(ServerClientConnectedEvent.class, new GameEventListener<ServerClientConnectedEvent>() {
            @Override
            public void onEvent(ServerClientConnectedEvent e) {
                //reset our spawn message anytime someone connects.
                //does not need to be done its here to prevent the occasional spawn spam
                hasSentSpawnMessage = false;

                if (isDevMode) {
                    server.network.sendToAllClients(new PacketExample("Example server packet"));
                    System.out.println("serverClientConnectedEvent for " + e.client.getName());
                }
            }
        });
        GameEvents.addListener(ServerClientDisconnectEvent.class, new GameEventListener<ServerClientDisconnectEvent>() {
            @Override
            public void onEvent(ServerClientDisconnectEvent e) {
                if (isDevMode) {
                    System.out.println("Example listener for client disconnect called: " + e.client.getName());
                }
            }
        });
    }

    public void dispose() {
        this.isDisposed = true;
    }

    public boolean isDisposed() {
        return this.isDisposed;
    }
}

