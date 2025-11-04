package AnotherExampleMod.Patch;

import AnotherExampleMod.ModMain;
import AnotherExampleMod.Presets.ModSettingsForm;
import necesse.engine.modLoader.annotations.ModConstructorPatch;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.engine.network.client.Client;
import necesse.gfx.forms.ContinueComponentManager;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.components.FormComponent;
import necesse.gfx.forms.components.FormFlow;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.presets.SettingsForm;
import net.bytebuddy.asm.Advice;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Predicate;

@ModConstructorPatch(target = SettingsForm.class, arguments = {Client.class, ContinueComponentManager.class})
public class PatchSettingsForm {
    @Advice.OnMethodExit
    static void onExit(@Advice.This SettingsForm settingsForm, @Advice.Argument(1) ContinueComponentManager manager, @Advice.FieldValue("mainMenu") Form mainMenu) {
        if (ModMain.isDevMode) {
            System.out.println(Thread.currentThread().getStackTrace()[1].getClassName() + ": " + "Constructor Patched, onExit patch method ran");
        }
        updateModSettingsForm(settingsForm, manager, mainMenu);
    }

    @ModMethodPatch(target = SettingsForm.class, name = "setupMenuForm", arguments = {})
     public static class Patch2{

        @Advice.OnMethodExit
        static void onExit(@Advice.This SettingsForm settingsForm, @Advice.FieldValue("mainMenu") Form mainMenu, @Advice.Local("form") FormFlow flow) {
            //set mainmenu form height so we can add our button below the Back button.
            //it is not ideal but it works.
            if (mainMenu !=null ){
                mainMenu.setHeight(440);
                mainMenu.getComponentList().stream()
                        // Use the public static helper method reference for the filter
                        .filter(isLocalTextButton)
                        // Use a public static helper method reference for the forEach action
                        .forEach(PatchSettingsForm::processButton);
                //System.out.println("This is so shit" + test.getClass());

            }
        }
    }

    public static final Predicate<FormComponent> isLocalTextButton =
            c -> c instanceof FormLocalTextButton;

    // 2. Consumer for the forEach() operation
    public static void processButton(FormComponent component) {
        // 'component' is guaranteed to be a FormLocalTextButton by the filter
        if (component instanceof FormLocalTextButton) {
            FormLocalTextButton button = (FormLocalTextButton) component;
            System.out.println("Processing button: " + button.getText());
            if (button.getText().equals("Back")){
                button.addPosition(0,40);
            };
        }
    }

    public static void updateModSettingsForm(SettingsForm settingsForm, ContinueComponentManager manager, Form mainMenu) {
        //set local modsettingsform,
        ModSettingsForm modSettingsForm = ModMain.modClientLoop.modSettingsForm;
        //set mainMenu in modClientLoop
        ModMain.modClientLoop.setMainMenuForm(mainMenu);
        if (modSettingsForm != null)
            return;
        //add our ModSettingsForm, I've taken this from SettingsForum.updateModsForm()
        //backPressed - when the player pressed either escape button or presses the BackButton
        modSettingsForm = settingsForm.addComponent(new ModSettingsForm("modsettings", manager){
            public void backPressed() {
                settingsForm.makeCurrent((FormComponent) mainMenu);
            }
        },(c, isCurrent) -> {
            if (isCurrent.booleanValue())
                c.resetCurrent();
        });
    }

    //This uses reflection to get and make the mainMenu field public, I've chosen instead to use
    //@Advice.FieldValue("mainMenu"), fieldvalue advice gives us access to mainMenu.
    //mitigating the need to use our own reflection
    //But I've left this here as a reflection example.
    public static Form mainMenuAccess(SettingsForm settingsForm) {
        if (settingsForm == null) {
            throw new IllegalArgumentException("settingsForm cannot be null.");
        }
        try {
            Field field = SettingsForm.class.getDeclaredField("mainMenu");
            field.setAccessible(true);
            Form mainMenu = (Form) field.get(settingsForm);
            System.out.println("Accessed Field: " + field.getName() + " -> Form Name: " + mainMenu.name);
            return mainMenu;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Reflection Error: The field 'mainMenu' does not exist in SettingsForm.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Reflection Error: Failed to access 'mainMenu' despite setAccessible(true).", e);
        }
    }
}
