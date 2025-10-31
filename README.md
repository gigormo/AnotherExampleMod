# Another Example Mod

This mod serves as a collection of code examples and patching demonstration for the Necesse modding community.
I recommend backing up your character and creating a new world for the mod. Although it should not break your current saves
the mod serves to be an example. You have been warned.


### Be aware that using the @Advice.OnMethodEnter(skipOn = ...) annotation in a mod's patch can cause conflict issues
### If an earlier-loaded mod uses skipOn and returns a value that triggers the skip, our patch will be entirely bypassed

Example: Our KGameColorPatch only injects its custom color supplier if the base method returns null. 
If another mod does the same, both mods can coexist, allowing their respective color codes to be recognized without conflict assuming they do not use the same characters.

Other conflicts are likely to arise eventually, larger mods or conversion mods may or may not need to patch entire methods as an example.

Packets can have a processServer and processClient, a client sending a packet will cause processServer to run
while a serverclient sending the packet will run processClient. SendToAllClients will also run processClient


## Chat commands
* settrinketslots amount - Adds more trinket slots to player

* settotalsets amount - Add more sets

* toggleinteractpatch on/off - toggles interaction range patching

* givebuff buff duration(default 10000), enable particle effect(default on) on/off, gives an assortment of buffs. is a cheat

* examplecommand  int peram as example, string preset as example


Check out the [modding wiki page](https://necessewiki.com/Modding) for more.
Fairs example mod [Fair's ExampleMod](https://github.com/DrFair/ExampleMod)