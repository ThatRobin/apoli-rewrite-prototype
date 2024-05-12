package io.github.thatrobin.apolirewrite;

import com.mojang.serialization.Codec;
import io.github.thatrobin.apolirewrite.powers.power_types.PowerType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class ApoliRegistries {

    public static final Registry<Codec<? extends PowerType>> POWER_TYPES = create(ApoliRegistryKeys.POWER_TYPE);

    private static <T> Registry<T> create(RegistryKey<Registry<T>> registryKey) {
        return FabricRegistryBuilder.createSimple(registryKey).buildAndRegister();
    }
}
