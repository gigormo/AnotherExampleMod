
# Another Example Mod

Just another example mod for the Necesse modding community.

The examples and notes in this repo maybe be confusing,
I've likely not used the best or even the correct wording. 

In any case I hope you find the examples here helpful, and if your confused you can
always send me a DM on discord at `ichibankrunchy` or reach out in the
[Necesse discord](https://discord.gg/FAFgrKD). Don't be afraid to ask questions and learn.

>[!CAUTION]
>It is recommend to:
>1. ️Backup your character data before using this mod.
>2. Create a new, dedicated world to test the mod's functionality.\
>⚠️Use at your own risk, features added to this mod may break saves.⚠️

---

## 🩹Patching Notes🩹

>[!WARNING]
>**Using @Advice.OnMethodEnter(skipOn = ...) can cause patch conflicts.
>If a previously-loaded mod triggers a skip, your patch may never run.**

If possible avoid patching non-null returns, instead use a return check.\
`if (originalValue != null) return originalValue;`

------

## 📦Packet & Network Notes🌐️

Packets can have a processServer and processClient, 
the client is unable to access server variables and vice versa.

Think about it like this, the Client and Server are separate. If you want to "talk" to the server
a "packet" is your voice and intent.\
This works the opposite direction as well, "talking" to the client requires
sending a "packet" or our voice and directive.

**This is a very simplified explanation**

Look at some of the packets in the Necesse source/jar to get a better understanding of how they work.

Examples on how `streamClients` can be used are found in `ModServerLoop.gameTick()`

<details>
<summary>🗒️Expand for additional notes🗒️</summary>

⚠️**These notes assume ️strictServerAuthority is disabled**⚠️

An example as to what ️strictServerAuthority does below:

    If strictServerAuthority is disabled this variable `clientHandlesHit` from MobAbilityLevelEvent
    will be true then client will perform the hit logic not the server

Not all invalid packets will disconnect a client, sending the wrong slot in mods PacketExample\
> E.g.: `PacketExample(-2, "some message")` will print a error but not disconnect the client.\
>
> An invalid movement packet would snap a client to the location the server expects
>
> An invalid PacketActiveMountAbility would disconnect a client 

---

    Client → Server: The client sends a packet to request an action (e.g: moving, attacking, chatting).
    Packet runs : processServer and processClient

    Server → Client: The server sends a packet to inform the client of a change (e.g: applying a buff, updating world state).
    Packet runs : processClient
---
</details>

---
## ➿Gameloop Notes➿

As the name suggests GameLoop is the gameloop, listeners can be added
and are called every frame. Each listener must implement a frameTick and drawTick method

In this example mod, we have a ModClientLoop and ModServerLoop class, 
they both implement GameLoopListener

> `addLoopEventListeners -> trigger event -> addGameLoopListener -> ModLoop`
---------

## 🗨️Chat Commands💬

>* settrinketslots amount - Adds more trinket slots to player

>* settotalsets amount - Add more sets

>* toggleinteractpatch on/off - toggles interaction range patching

>* givebuff buff duration(default 10000), enable particle effect(default on) on/off, gives an assortment of buffs. is a cheat

>* examplecommand  int peram as example, string preset as example


**Check [modding wiki page](https://necessewiki.com/Modding) for more.**\
**See also: [Fair's ExampleMod](https://github.com/DrFair/ExampleMod)**

