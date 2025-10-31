package AnotherExampleMod;

import AnotherExampleMod.Buffs.ExampleBuff;
import AnotherExampleMod.Commands.*;
import AnotherExampleMod.Events.ModLoopEvent;
import AnotherExampleMod.Patch.ClientLoopPatch;
import AnotherExampleMod.Patch.ServerLoopPatch;
import necesse.engine.GameEventListener;
import necesse.engine.GameEvents;
import necesse.engine.GlobalData;
import necesse.engine.commands.CommandsManager;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.events.ServerClientConnectedEvent;
import necesse.engine.events.ServerClientDisconnectEvent;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.BuffRegistry;
import necesse.gfx.gameTexture.GameTexture;
import necesse.gfx.gameTexture.GameTextureSection;

/**
 * Main class of the mod, handles registry, settings and anything
 * that needs to be initialized early on. Holds references to our
 * {@link ModServerLoop}, {@link ModClientLoop}, mods {@link PermissionLevel},
 * and if mod is running on a server or client {@link ModMain#isServer isServer}
 */
@ModEntry
public class ModMain {
    public static ModClientLoop modClientLoop;
    public static ModServerLoop modServerLoop;
    // if your coming from a language like c++ or C#
    // this is essentially an ifndef/ifendif.
    public static final boolean isDevMode = true;
    public static final boolean runTestPatches = false;
    public static boolean isServer;

    /**
     * Called before preInit to initialize settings
     *
     * @return {@link necesse.engine.modLoader.ModSettings ExampleSettings} Required
     * @see ExampleSettings
     * @see necesse.engine.modLoader.ModSettings
     */
    @SuppressWarnings("unused")
    public ExampleSettings initSettings() {
        //Setting isServer here because initSettings is called so early
        isServer = GlobalData.isServer();
        return new ExampleSettings();
    }

    /**
     * Called pre core game, wiki has suggested only use this if
     * you know what your doing.
     */
    @SuppressWarnings("unused")
    public void preInit() {
        if (isServer) {
            modServerLoop = new ModServerLoop();
            return;
        }
        modClientLoop = new ModClientLoop();
    }

    /**
     * Initializes the Mods core components ||
     * General order you should register mod objects per the wiki:
     * <ul>
     * <li>Tiles</li>
     * <li>Objects</li>
     * <li>Biomes</li>
     * <li>Buffs</li>
     * <li>Global ingredients</li>
     * <li>Recipe techs</li>
     * <li>Items</li>
     * <li>Enchantments</li>
     * <li>Mobs</li>
     * <li>Pickup entities and projectiles</li>
     * <li>Level, world events, level data and settlers</li>
     * <li>Containers</li>
     * <li>World generators</li>
     * <li>Packets</li>
     * <li>Quests</li>
     * <li>Other</li>
     * </ul>
     */
    @SuppressWarnings("unused")
    public void init() {
        //Register mod buff
        BuffRegistry.registerBuff("kexamplebuff", new ExampleBuff());
        //Add our event listeners
        addEventListeners();
        //Register hotkeys
        ExampleControl.registerHotkeys();
    }

    /**
     * Initializes mod resources
     * Few use cases:
     * <ul>
     * <li>icons</li>
     * <li>sprites</li>
     * </ul>
     */
    @SuppressWarnings("unused")
    public void initResources() {
        ExampleBuff.texture = GameTexture.fromFile("buffs/krunchybuff");
        ExampleBuff.particleTexture = new GameTextureSection(GameTexture.fromFile("particles/kspark"));
    }

    /**
     * Called after the core, mods and resources have initialized.
     * Register recipes|chat commands and modify loot|spawn tables here
     */
    @SuppressWarnings("unused")
    public void postInit() {
        CommandsManager.registerServerCommand(new TrinketSlotCommand());
        CommandsManager.registerServerCommand(new BuffCommand());
        CommandsManager.registerServerCommand(new InteractPatchCommand());
        CommandsManager.registerServerCommand(new ExampleCommand());
    }

    /**
     * Called on game close
     * dispose anything that would not be automatically disposed by the game
     * textures loaded using GameTexture.fromFile() and sounds are automatically disposed.
     */
    @SuppressWarnings("unused")
    public void dispose() {
        if (modClientLoop != null) modClientLoop.dispose();
        if (modServerLoop != null) modServerLoop.dispose();
    }

    /**
     * Method called from init, registers our event listeners
     * our Loops start here. By patching both game loops we trigger the loop events
     * ServerClientConnectedEvent is used to reset the spawn message boolean {@link ModClientLoop#hasSentSpawnMessage}
     * if we don't do this where a client to leave and rejoin they would not get our welcome messages
     * @see ClientLoopPatch
     * @see ServerLoopPatch
     */
    private void addEventListeners() {
        GameEvents.addListener(ServerClientConnectedEvent.class, new GameEventListener<ServerClientConnectedEvent>() {
            @Override
            public void onEvent(ServerClientConnectedEvent e) {
                if (modClientLoop != null)
                    modClientLoop.hasSentSpawnMessage = false;
                if (isDevMode) {
                    System.out.println("serverClientConnectedEvent for " + e.client.getName());
                }
            }
        });
        GameEvents.addListener(ServerClientDisconnectEvent.class, new GameEventListener<ServerClientDisconnectEvent>() {
            @Override
            public void onEvent(ServerClientDisconnectEvent e) {
                if (isDevMode){
                    System.out.println("Example listener for client disconnect" + e.client.getName());
                }
            }
        });

        GameEvents.addListener(ModLoopEvent.class, new GameEventListener<ModLoopEvent>() {
            @Override
            public void onEvent(ModLoopEvent e) {
                if (isServer) {
                    e.gameloop.addGameLoopListener(modServerLoop);
                    modServerLoop.server = e.server;
                    if (isDevMode) {
                        System.out.println("ModServerLoop added to : " + e.gameloop.getName());
                    }
                    return;
                }
                e.gameloop.addGameLoopListener(modClientLoop);
                if (isDevMode) {
                    System.out.println("ModClientLoop added to : " + e.gameloop.getName() + " Client State : " + GlobalData.getCurrentState());
                }
            }
        });
    }
}

