package AnotherExampleMod.Packet;

import AnotherExampleMod.ModMain;
import necesse.engine.localization.message.GameMessage;
import necesse.engine.localization.message.StaticMessage;
import necesse.engine.network.NetworkPacket;
import necesse.engine.network.Packet;
import necesse.engine.network.PacketReader;
import necesse.engine.network.PacketWriter;
import necesse.engine.network.client.Client;
import necesse.engine.network.client.ClientClient;
import necesse.engine.network.packet.PacketRequestPlayerData;
import necesse.engine.network.server.Server;
import necesse.engine.network.server.ServerClient;
import necesse.entity.mobs.PlayerMob;
import necesse.gfx.GameColor;
import necesse.level.maps.Level;
import necesse.level.maps.hudManager.floatText.ChatBubbleText;

public class PacketExample extends Packet {
    public final int slot;
    public final GameMessage gameMessage;

    //Must handle the data in the same order
    //when read/writing
    public PacketExample(byte[] data) {
        super(data);
        PacketReader reader = new PacketReader(this);
        this.slot = reader.getNextByteUnsigned();
        this.gameMessage = GameMessage.fromContentPacket(reader.getNextContentPacket());
    }

    public PacketExample(int slot, GameMessage message) {
        this.slot = slot;
        this.gameMessage = message;
        PacketWriter writer = new PacketWriter(this);
        writer.putNextByteUnsigned(slot);
        writer.putNextContentPacket(message.getContentPacket());
    }

    public PacketExample(GameMessage message) {
        this(-1, message);
    }

    public PacketExample(String message) {
        this(-1, new StaticMessage(message));
    }

    public PacketExample(int slot, String message) {
        this(slot, new StaticMessage(message));
    }

    public void processServer(NetworkPacket packet, Server server, ServerClient client) {
        String message = this.gameMessage.translate();

        if (ModMain.isDevMode)
            System.out.println(message + Thread.currentThread().getStackTrace()[1]);

        if (this.slot == client.slot) {
            System.out.println("(" + client.getName() + "): " + message);
            server.network.sendToAllClients(this);
        } else {
            System.out.println("Received message from wrong slot sent by slot " + client.slot + ": " + this.slot + ": " + message);
        }

    }

    public void processClient(NetworkPacket packet, Client client) {
        String message = this.gameMessage.translate();

        if (ModMain.isDevMode)
            System.out.println(message + Thread.currentThread().getStackTrace()[1]);

        //client slot
        if ((byte) this.slot == -1) {
            client.chat.addMessage(message);
        } else {
            ClientClient target = client.getClient(this.slot);
            PlayerMob player = target == null ? null : target.playerMob;
            client.chat.addMessage((player == null ? "N/A" : player.getDisplayName()) + ": " + message);
            if (target == null) {
                client.network.sendPacket(new PacketRequestPlayerData(this.slot));
            } else {
                Level level = client.getLevel();
                if (player != null && target.isSamePlace(level)) {
                    level.hudManager.addElement(new ChatBubbleText(player, GameColor.stripCodes(message)));
                }
            }
        }

    }
}

