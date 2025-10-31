package AnotherExampleMod;

import necesse.engine.commands.PermissionLevel;
import necesse.engine.save.LoadData;
import necesse.engine.save.SaveData;

public class ExampleSettings extends necesse.engine.modLoader.ModSettings {
    public static PermissionLevel permissionLevel = PermissionLevel.OWNER;
    //prevents crashing first time using mod
    public static boolean hasAddedSaveData;
    public static boolean shouldPatchInteractRange = false;
    public static boolean hasSPM;
    public static boolean shouldEmit = true;

    public static int maxTrinkets = 10;
    public static int maxSets = 5;


    public void addSaveData(SaveData saveData) {
        //This is here because I found it annoying
        //crashing if no settings existed...

        if (!hasAddedSaveData) {
            hasAddedSaveData = true;
        }
        saveData.addBoolean("shouldEmit", shouldEmit);
        saveData.addBoolean("shouldPatchInteractRange", shouldPatchInteractRange);
        saveData.addEnum("permissionLevel", permissionLevel);
        saveData.addBoolean("hasAddedSaveData", hasAddedSaveData);
    }

    public void applyLoadData(LoadData loadData) {
        if (hasAddedSaveData) {
            shouldEmit = loadData.getBoolean("shouldEmit");
            shouldPatchInteractRange = loadData.getBoolean("shouldPatchInteractRange");
            permissionLevel = loadData.getEnum(PermissionLevel.class, "permissionLevel");
            hasAddedSaveData = loadData.getBoolean("hasAddedSaveData");
        }
    }
}
