package AnotherExampleMod.Patch;

import AnotherExampleMod.ModMain;
import AnotherExampleMod.Presets.ModSettingsForm;
import necesse.engine.modLoader.annotations.ModMethodPatch;
import necesse.gfx.forms.ContinueComponentManager;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.components.FormComponent;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.presets.SettingsForm;
import net.bytebuddy.asm.Advice;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class PatchSettingsForm {

    public static final Predicate<FormComponent> isLocalTextButton =
            c -> c instanceof FormLocalTextButton;

    public static void updateModSettingsButton(SettingsForm settingsForm, ContinueComponentManager manager) {
        // ModSettingsForm modSettingsForm = ModSettingsForm.modSettingsForm;

        if (ModSettingsForm.modSettingsForm != null) {
            System.err.println(PatchSettingsForm.class.getSimpleName() + " Error: " + "modSettingsForm is not null");
            return;
        }
        //add the created component to ModSettingsForm
        ModSettingsForm.modSettingsForm = settingsForm.addComponent(new ModSettingsForm(manager) {
        }, (c, isCurrent) -> {
            if (isCurrent.booleanValue()) c.resetCurrent();
        });
    }

    public static void processButton(FormComponent component) {
        if (component instanceof FormLocalTextButton) {
            FormLocalTextButton button = (FormLocalTextButton) component;
            //push back button down to fit our button above it
            if (button.getText().equals("Back")) {
                button.addPosition(0, 40);
            }
        }
    }

    /**
     * Uses reflection to get and make the mainMenu field public, I've chosen instead to use
     * {@code @Advice.FieldValue("mainMenu")}, fieldvalue gives us access to mainMenu.
     * mitigating the need to use our own reflection
     * But I've left this here as a reflection example.
     *
     * @return accessible mainMenu
     */

    public static Form mainMenuAccess(SettingsForm settingsForm) {
        if (settingsForm == null) {
            throw new IllegalArgumentException("settingsForm cannot be null.");
        }
        try {
            Field field = SettingsForm.class.getDeclaredField("mainMenu");
            field.setAccessible(true);
            Form mainMenu = (Form) field.get(settingsForm);
            if (ModMain.isDevMode)
                System.out.println("Accessed Field: " + field.getName() + " -> Form Name: " + mainMenu.name);
            return mainMenu;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Reflection Error: The field mainMenu does not exist in SettingsForm.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Reflection Error: Failed to access mainMenu despite setAccessible(true).", e);
        }
    }

    @ModMethodPatch(target = SettingsForm.class, name = "setupMenuForm", arguments = {})
    public static class Patch2 {

        @Advice.OnMethodExit
        static void onExit(@Advice.This SettingsForm settingsForm, @Advice.FieldValue("mainMenu") Form mainMenu) {
            if (mainMenu != null) {
                mainMenu.setHeight(440);
                //finds the back button
                mainMenu.getComponentList().stream().filter(isLocalTextButton)
                        /*
                         to prevent the below error we must take the lambda outside the patch methods
(necesse.gfx.forms.presets.SettingsForm and AnotherExampleMod.Patch.PatchSettingsForm$Patch2 are in unnamed module of loader 'app')
                         e.g. the below logic can not be ran from the onExit/onEnter patch methods.
                        .filter(c -> c instanceof FormLocalTextButton)
                         the same for the forEach below, if we were to have the logic inside
                         we would get similar error as the previous one shown
                        */
                        .forEach(PatchSettingsForm::processButton);
            }
            ModSettingsForm.mainMenu = mainMenu;
            ModSettingsForm.settingsForm = settingsForm;
            updateModSettingsButton(settingsForm, (ContinueComponentManager) settingsForm.getManager());
        }
    }
}
