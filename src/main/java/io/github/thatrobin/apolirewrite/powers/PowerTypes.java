package io.github.thatrobin.apolirewrite.powers;

import com.mojang.serialization.Codec;
import io.github.thatrobin.apolirewrite.Apoli;
import io.github.thatrobin.apolirewrite.ApoliRegistries;
import io.github.thatrobin.apolirewrite.powers.power_types.ElytraFlightPowerType;
import io.github.thatrobin.apolirewrite.powers.power_types.ModelColorPowerType;
import io.github.thatrobin.apolirewrite.powers.power_types.PowerType;
import net.minecraft.registry.Registry;

import java.util.function.Supplier;

public class PowerTypes {

    public static final ElytraFlightPowerType ELYTRA_FLIGHT = PowerTypes.register("elytra_flight", ElytraFlightPowerType::new);

    public static final ModelColorPowerType MODEL_COLOR = PowerTypes.register("model_color", ModelColorPowerType::new);

    private static <T extends PowerType> T register(String id, Supplier<T> powerTypeSupplier) {
        return Registry.register(ApoliRegistries.POWER_TYPES, Apoli.identifier(id), powerTypeSupplier.get());
    }

    public static void init() {}
}
