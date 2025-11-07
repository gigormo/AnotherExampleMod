package AnotherExampleMod;

import necesse.engine.input.Control;
import necesse.engine.localization.message.LocalMessage;

public class ExampleControl {
    //Create the control
    public static Control exampleKey1;
    public static Control exampleKey2;

    /**
     * Registers mod hotkeys, should be called in init()
     * For valid key codes, see:
     *
     * @see necesse.engine.input.InputID
     */
    public static void registerHotkeys() {
        LocalMessage exampleKey1Message = new LocalMessage("modcontrols", "examplekey1");
        LocalMessage exampleKey2Message = new LocalMessage("modcontrols", "examplekey2");
        //Key Numpad0
        Control control1 = new Control(320, "examplekey1", exampleKey1Message);
        //Key Numpad1
        Control control2 = new Control(321, "examplekey2", exampleKey2Message);
        exampleKey1 = Control.addModControl(control1);
        exampleKey2 = Control.addModControl(control2);
    }
}
