package io.github.thatrobin.apolirewrite.powers.power_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.github.thatrobin.apolirewrite.ApoliRegistries;
import lombok.NoArgsConstructor;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodec;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor
public class PowerType implements ComponentType {
    public static <T extends PowerType> Codec<? extends PowerType> getPowerTypeCodec() {
        return ApoliRegistries.POWER_TYPES.getCodec().dispatch("type", (tg) -> tg, (t) -> MapCodec.assumeMapUnsafe(t.getCodec()));
    }

    @Nullable
    @Override
    public Codec<? extends PowerType> getCodec() {
        return PowerType.getPowerTypeCodec();
    }

    @Override
    public PacketCodec getPacketCodec() {
        return null;
    }
}
