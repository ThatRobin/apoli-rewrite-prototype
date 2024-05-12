package io.github.thatrobin.apolirewrite;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.github.thatrobin.apolirewrite.commands.PowerCommand;
import io.github.thatrobin.apolirewrite.networking.SyncAttachmentPayload;
import io.github.thatrobin.apolirewrite.powers.*;
import io.github.thatrobin.apolirewrite.powers.power_types.ElytraFlightPowerType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntityElytraEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.registry.sync.DynamicRegistriesImpl;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Apoli implements ModInitializer {

    public static String MODID = "apoli";
    public static final Logger LOGGER = LogManager.getLogger(Apoli.class);

    @Override
    public void onInitialize() {
        PowerTypes.init();
        SyncAttachmentPayload.register();
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new PowerLoader());
        DynamicRegistriesImpl.addSyncedRegistry(ApoliRegistryKeys.POWER, CorePowerType.CODEC);

        ServerPlayConnectionEvents.JOIN.register((serverPlayNetworkHandler, packetSender, server) -> {
            PowerHolderComponent component = serverPlayNetworkHandler.player.getAttachedOrCreate(PowerHolderComponent.POWERS);
            component.sync(serverPlayNetworkHandler.player);
        });

        EntityElytraEvents.CUSTOM.register(((entity, tickElytra) -> PowerHolderComponent.hasPower(entity, ElytraFlightPowerType.class)));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            PowerCommand.register(dispatcher);
        });
    }

    public static final Codec<Identifier> CODEC = Codec.STRING.comapFlatMap(Apoli::validate, Identifier::toString).stable();

    public static DataResult<Identifier> validate(String id) {
        try {
            if(id.contains(":")) {
                return DataResult.success(new Identifier(id));
            } else {
                throw new InvalidIdentifierException(" this must be an identifier, not a path sucks to suck minecraft.");
            }
        } catch (InvalidIdentifierException invalidIdentifierException) {
            return DataResult.error(() -> "Not a valid resource location: " + id + " " + invalidIdentifierException.getMessage());
        }
    }

    public static Identifier identifier(String path) {
        return new Identifier(MODID, path);
    }
}
