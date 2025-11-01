# Another Example Mod

Just another example mod for the 
Necesse modding community.

[!CAUTION]
It is recommend to:

1. ️Backup your character data before using this mod.  
2. Create a new, dedicated world to test the mod's functionality.

[!WARNING]
⚠️Use at your own risk, features added to this mod may break saves.⚠️

<details>
<summary>👇Patching Notes👇</summary>

## Notes

**Using @Advice.OnMethodEnter(skipOn = ...) can cause patch conflicts.
If a previously-loaded mod triggers a skip, your patch may never run.**

This mod does not utilize any method skip although InteractPatch
patches the range while shouldPatchInteractRange is true

`if (originalValue != null) return originalValue;`

If possible avoid patching non-null returns use a return check like above.
</details>

------

<details>
<summary>👇Packet & network notes👇</summary>

Packets can have a processServer and processClient

Client sending a packet will run processServer

ServerClient sending a packet will run processClient

SendToAllClients runs processClient on all connected clients
</details>

---------

<details >
<summary>👇Chat commands👇</summary>
* settrinketslots amount - Adds more trinket slots to player

* settotalsets amount - Add more sets

* toggleinteractpatch on/off - toggles interaction range patching

* givebuff buff duration(default 10000), enable particle effect(default on) on/off, gives an assortment of buffs. is a cheat

* examplecommand  int peram as example, string preset as example
</details>


Check out the [modding wiki page](https://necessewiki.com/Modding) for more.\
Fairs example mod [Fair's ExampleMod](https://github.com/DrFair/ExampleMod)

