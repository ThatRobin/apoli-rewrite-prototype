package io.github.thatrobin.apolirewrite.powers.power_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.thatrobin.apolirewrite.ApoliRegistries;
import net.minecraft.component.ComponentType;

public interface PowerType extends ComponentType {
    public static <T extends PowerType> Codec<T> getPowerTypeCodec() {
        return ApoliRegistries.POWER_TYPES.getCodec().dispatch("type", ComponentType::getCodec, (t) -> MapCodec.assumeMapUnsafe((Codec<T>) t));
    }

}
