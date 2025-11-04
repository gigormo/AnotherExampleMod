package AnotherExampleMod.Presets;

import AnotherExampleMod.ModMain;
import necesse.gfx.forms.ContinueComponentManager;
import necesse.gfx.forms.Form;
import necesse.gfx.forms.FormSwitcher;
import necesse.gfx.forms.components.FormComponent;
import necesse.gfx.forms.components.FormFlow;
import necesse.gfx.forms.components.localComponents.FormLocalTextButton;

public class ModSettingsForm extends FormSwitcher {
    public Form modSettings;

    public ModSettingsForm(String modsettings, ContinueComponentManager manager){
        testing();
    }

    public void testing(){
        Form mainMenu = ModMain.modClientLoop.getMainMenuForm();
        // we have to do this since we can't get access to the FlowForm inside SettingsForm.setupMainForm()
        // this is another one of those frustrating workarounds we need to do

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
        FormLocalTextButton modSettingsButton = (FormLocalTextButton)mainMenu.addComponent((FormComponent)new FormLocalTextButton("ui", "modsettings", 4, flow.next(40), mainMenu.getWidth() - 8));
        modSettingsButton.onClicked(e -> makeModsCurrent());
    }

    public void resetCurrent() {
        makeCurrent((FormComponent)this.modSettings);
    }

    public void makeModsCurrent() {
        //makeCurrent((FormComponent)this.mods);
        System.out.println("makeModsCurrent ran");
    }

    public void backPressed() {}
}
