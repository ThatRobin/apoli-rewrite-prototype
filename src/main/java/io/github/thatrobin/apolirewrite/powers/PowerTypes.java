package io.github.thatrobin.apolirewrite.powers;

import com.mojang.serialization.Codec;
import io.github.thatrobin.apolirewrite.Apoli;
import io.github.thatrobin.apolirewrite.ApoliRegistries;
import io.github.thatrobin.apolirewrite.powers.power_types.ElytraFlightPowerType;
import io.github.thatrobin.apolirewrite.powers.power_types.ModelColorPowerType;
import io.github.thatrobin.apolirewrite.powers.power_types.PowerType;
import net.minecraft.registry.Registry;

public class PowerTypes {

    public static final Codec<ElytraFlightPowerType> ELYTRA_FLIGHT = PowerTypes.register("elytra_flight", ElytraFlightPowerType.CODEC);

    public static final Codec<ModelColorPowerType> MODEL_COLOR = PowerTypes.register("model_color", ModelColorPowerType.CODEC);

    private static <T extends PowerType> Codec<T> register(String id, Codec<T> codec) {
        Apoli.LOGGER.info(Apoli.identifier(id));
        return Registry.register(ApoliRegistries.POWER_TYPES, Apoli.identifier(id), codec);
    }

    public static void init() {}
}
