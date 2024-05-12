package io.github.thatrobin.apolirewrite.networking;

import io.github.thatrobin.apolirewrite.Apoli;
import io.github.thatrobin.apolirewrite.powers.CorePowerType;
import io.github.thatrobin.apolirewrite.powers.PowerData;
import io.github.thatrobin.apolirewrite.powers.PowerHolderComponent;
import io.github.thatrobin.apolirewrite.powers.PowerRegistry;
import io.github.thatrobin.apolirewrite.powers.power_types.PowerType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.*;

public record SyncAttachmentPayload(PowerHolderComponent component) implements CustomPayload {

    public static final Id<SyncAttachmentPayload> ID = CustomPayload.id("apoli:sync_attachment_payload");
    public static final PacketCodec<PacketByteBuf, SyncAttachmentPayload> CODEC = PacketCodec.of((value, buf) -> {
        int powerSize = value.component.powers().size();
        Apoli.LOGGER.info(powerSize + " powers written.");
        buf.writeInt(powerSize);
        if(powerSize > 0) {
            for (Map.Entry<PowerType, PowerData> entry : value.component.powers().entrySet()) {
                Identifier id = PowerRegistry.getId(entry.getKey());
                buf.writeIdentifier(id);
                Apoli.LOGGER.info("Power written with id: " + id);
            }
        }
        Apoli.LOGGER.info("Sending now...");
    }, buf -> {
        Apoli.LOGGER.info("Packet recieved! processing...");
        int powerSize = buf.readInt();
        Apoli.LOGGER.info(powerSize + " powers recieved.");
        PowerHolderComponent powerHolderComponent = new PowerHolderComponent(new HashMap<>());
        if(powerSize > 0) {
            for (int i = 0; i < powerSize; i++) {
                Identifier id = buf.readIdentifier();
                PowerType powerType = PowerRegistry.get(id);
                if(powerType instanceof CorePowerType corePowerType) {
                    powerHolderComponent.addPower(corePowerType);
                }
                Apoli.LOGGER.info("Power recieved with id: " + id);
            }
        }
        return new SyncAttachmentPayload(powerHolderComponent);
    });

    public static void register() {
        PayloadTypeRegistry.playS2C().register(SyncAttachmentPayload.ID, SyncAttachmentPayload.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(SyncAttachmentPayload.ID, (payload, context) -> {
            Apoli.LOGGER.info("Recieved packet from server, syncing...");
            PowerHolderComponent component = context.player().getAttachedOrCreate(PowerHolderComponent.POWERS);
            for (Map.Entry<PowerType, PowerData> entry : payload.component.powers().entrySet()) {
                component.addPower(entry.getKey());
            }
            context.player().setAttached(PowerHolderComponent.POWERS, component);
        });

        PayloadTypeRegistry.playC2S().register(SyncAttachmentPayload.ID, SyncAttachmentPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SyncAttachmentPayload.ID, ((payload, context) -> {
            Apoli.LOGGER.info("Recieved packet from client, syncing...");
            PowerHolderComponent component = context.player().getAttachedOrCreate(PowerHolderComponent.POWERS);
            for (Map.Entry<PowerType, PowerData> entry : payload.component.powers().entrySet()) {
                component.addPower(entry.getKey());
            }
            context.player().setAttached(PowerHolderComponent.POWERS, component);
        }));
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
