package io.github.thatrobin.apolirewrite;

import com.mojang.serialization.Codec;
import io.github.thatrobin.apolirewrite.powers.CorePowerType;
import io.github.thatrobin.apolirewrite.powers.PowerHolderComponent;
import io.github.thatrobin.apolirewrite.powers.power_types.PowerType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

public class ApoliRegistryKeys {

    public static final RegistryKey<Registry<Codec<? extends PowerType>>> POWER_TYPE = RegistryKey.ofRegistry(Apoli.identifier("power_type"));
    public static final RegistryKey<Registry<CorePowerType>> POWER = RegistryKey.ofRegistry(Apoli.identifier("power"));
    public static final RegistryKey<Registry<PowerHolderComponent>> POWER_HOLDER_COMPONENT = RegistryKey.ofRegistry(Apoli.identifier("power_holder_component"));
}
