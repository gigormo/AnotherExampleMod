package AnotherExampleMod;

import AnotherExampleMod.Buffs.ExampleBuff;
import AnotherExampleMod.Commands.BuffCommand;
import AnotherExampleMod.Commands.ExampleCommand;
import AnotherExampleMod.Commands.InteractPatchCommand;
import AnotherExampleMod.Commands.TrinketSlotCommand;
import AnotherExampleMod.Events.ModLoopEvent;
import AnotherExampleMod.Packet.PacketExample;
import AnotherExampleMod.Patch.ClientLoopPatch;
import AnotherExampleMod.Patch.ServerLoopPatch;
import necesse.engine.GameEventListener;
import necesse.engine.GameEvents;
import necesse.engine.GlobalData;
import necesse.engine.commands.CommandsManager;
import necesse.engine.commands.PermissionLevel;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.registries.BuffRegistry;
import necesse.engine.registries.PacketRegistry;
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
    public static boolean isServer = GlobalData.isServer();

    /**
     * Called before preInit to initialize settings
     *
     * @return {@link necesse.engine.modLoader.ModSettings ExampleSettings} Required
     * @see ExampleSettings
     * @see necesse.engine.modLoader.ModSettings
     */
    @SuppressWarnings("unused")
    public ExampleSettings initSettings() {
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
        BuffRegistry.registerBuff("examplebuff", new ExampleBuff());
        //Add our event listeners
        addLoopEventListeners();
        //Register hotkeys
        ExampleControl.registerHotkeys();
        //register packets
        PacketRegistry.registerPacket(PacketExample.class);
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
        if (modServerLoop != null)
            modServerLoop.addServerEventListeners();
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
     * ServerClientConnectedEvent is used to reset the spawn message boolean {@link ModServerLoop#hasSentSpawnMessage}
     * @see ClientLoopPatch
     * @see ServerLoopPatch
     */
    private void addLoopEventListeners() {
        GameEvents.addListener(ModLoopEvent.class, new GameEventListener<ModLoopEvent>() {
            @Override
            public void onEvent(ModLoopEvent e) {
                //add gameloops
                if (isServer) {
                    e.gameloop.addGameLoopListener(modServerLoop);
                    modServerLoop.setServer(e.server);
                    if (isDevMode) {
                        System.out.println("ModServerLoop added to : " + e.gameloop.getName());
                    }
                    //Dispose the event listener, we do not need it after adding our gamelooplisteners
                    this.dispose();
                    return;
                }
                e.gameloop.addGameLoopListener(modClientLoop);
                if (isDevMode) {
                    System.out.println("ModClientLoop added to : " + e.gameloop.getName() + " Client State : " + GlobalData.getCurrentState());
                }
                //Dispose the event listener, It's no longer needed after adding our gamelooplisteners
                this.dispose();
            }
        });
    }
}

