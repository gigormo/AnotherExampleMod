package AnotherExampleMod.Presets;

import AnotherExampleMod.ModMain;
import necesse.engine.modLoader.LoadedMod;
import necesse.engine.modLoader.ModLoader;
import necesse.engine.platforms.steam.SteamData;
import necesse.engine.window.GameWindow;
import necesse.engine.window.WindowManager;
import necesse.gfx.forms.ContinueComponentManager;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.FormSwitcher;
import necesse.gfx.forms.components.*;
import necesse.gfx.forms.components.localComponents.FormLocalCheckBox;
import necesse.gfx.forms.components.localComponents.FormLocalLabel;
import necesse.gfx.forms.components.localComponents.FormLocalSlider;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;
import necesse.gfx.forms.position.FormPositionContainer;
import necesse.gfx.forms.presets.SettingsForm;
import necesse.gfx.gameFont.FontOptions;

import java.awt.*;

public class ModSettingsForm extends FormSwitcher {
    /**
     * reference for our ModSettingsForm, it's created in {@link AnotherExampleMod.Patch.PatchSettingsForm}
     * acts as a singleton I suppose.
     */
    public static ModSettingsForm modSettingsForm;
    /**
     * Stores reference to the mainMenu form, we need this to add our buttons to the settingsMenu
     * it is also used to make the settings mainmenu current
     */
    public static Form mainMenu;
    public static SettingsForm settingsForm;
    private final ContinueComponentManager manager;
    private final Color originalColor;
    /**
     * The form used for the menu
     */
    public Form main;
    public FormCheckBox exampleCheckbox;
    public FormSlider exampleSlider;

    /**
     * ModSettingsForm constructor, where the menu items are initialized
     * when ModSettingsForm as added as a component the constructor will be called
     *
     * @param manager
     */
    public ModSettingsForm(ContinueComponentManager manager) {
        addModSettingsButton();
        this.manager = manager;
        FormFlow mainFlow = new FormFlow(10);
        main = (Form) addComponent((FormComponent) new Form("modsettingsmenu", 450, 400));
        this.main.addComponent((FormComponent) new FormLocalLabel("modsettingsui", "front", new FontOptions(20), 0, this.main.getWidth() / 2, mainFlow.next(30)));
        ((FormLocalTextButton) this.main.addComponent((FormComponent) new FormLocalTextButton("ui", "backbutton", this.main.getWidth() / 2 + 2, this.main.getHeight() - 40, this.main.getWidth() / 2 - 6)))
                .onClicked(e -> {
                    this.backPressed();
                });
        exampleCheckbox = (FormLocalCheckBox) this.main.addComponent((FormComponent) mainFlow.nextY((FormPositionContainer) new FormLocalCheckBox("modsettingsui", "examplecheckbox", 10, 0, this.main.getWidth() - 20), 8));
        this.exampleCheckbox.onClicked(e -> {
            System.err.println("🧌🧌Trolls a troll, a tolls a toll. Get away from my bridge " + SteamData.getSteamName() + "!!!!!");
            WindowManager.getWindow().requestClose();
        });
        originalColor = main.getInterfaceStyle().activeElementColor;
        System.out.println(originalColor.getRGB());
        exampleSlider = (FormSlider) this.main.addComponent((FormComponent) mainFlow.nextY((FormPositionContainer) new FormLocalSlider("modsettingsui", "exampleslider", 10, 0, originalColor.getRGB(), -40000, 40000, this.main.getWidth())));
        exampleSlider.onChanged(e -> {
            main.getInterfaceStyle().activeElementColor = new Color(exampleSlider.getValue());
        });
    }


    public void addModSettingsButton() {
        // we have to do this since we can't get access to the FlowForm inside SettingsForm.setupMainForm()
        // an annoying workaround
        FormFlow flow = new FormFlow(11);
        flow.next(30);
        flow.next(40);
        flow.next(40);
        flow.next(40);
        flow.next(40);
        flow.next(40);
        flow.next(40);
        flow.next(40);
        flow.next(40);
        FormLocalTextButton modSettingsButton = (FormLocalTextButton) mainMenu.addComponent((FormComponent) new FormLocalTextButton("ui", "modsettings", 4, flow.next(40), mainMenu.getWidth() - 8));
        modSettingsButton.onClicked(e -> makeModSettingsCurrent());
    }

    public void resetCurrent() {
        makeCurrent(this.main);
    }

    public void onWindowResized(GameWindow window) {
        super.onWindowResized(window);
        this.main.setPosMiddle(window.getHudWidth() / 2, window.getHudHeight() / 2);
    }

    public void makeModSettingsCurrent() {
        settingsForm.makeCurrent(modSettingsForm);
    }

    public void backPressed() {
        main.getInterfaceStyle().activeElementColor = originalColor;
        exampleSlider.setValue(originalColor.getBlue());
        settingsForm.makeCurrent(mainMenu);
    }
}
