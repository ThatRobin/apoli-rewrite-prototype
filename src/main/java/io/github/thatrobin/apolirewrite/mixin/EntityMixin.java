package io.github.thatrobin.apolirewrite.mixin;

import io.github.thatrobin.apolirewrite.Apoli;
import io.github.thatrobin.apolirewrite.networking.SyncAttachmentPayload;
import io.github.thatrobin.apolirewrite.powers.PowerHolderComponent;
import io.github.thatrobin.apolirewrite.utils.AttachmentSyncer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public class EntityMixin implements AttachmentSyncer {
    @Shadow private World world;

    @Override
    public void apoli_rewrite$syncAttachment() {
        Entity entity = (Entity)(Object)this;
        PowerHolderComponent component = entity.getAttached(PowerHolderComponent.POWERS);
        if (component.isEmpty()) return;
        if (this.world.isClient) {
            ClientPlayNetworking.send(new SyncAttachmentPayload(component));
        } else {
            if(entity instanceof ServerPlayerEntity player) {
                Apoli.LOGGER.info("Sending the payload now!");
                ServerPlayNetworking.send(player, new SyncAttachmentPayload(component));
            }
        }
    }
}
