package io.github.thatrobin.apolirewrite.powers.power_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import org.jetbrains.annotations.Nullable;


@Getter
@AllArgsConstructor
public class ModelColorPowerType implements PowerType {

    final float red, green, blue, alpha;

    public static final Codec<ModelColorPowerType> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(Codec.FLOAT.optionalFieldOf("red", 1.0f).forGetter(ModelColorPowerType::getRed),
                    Codec.FLOAT.optionalFieldOf("green", 1.0f).forGetter(ModelColorPowerType::getGreen),
                    Codec.FLOAT.optionalFieldOf("blue", 1.0f).forGetter(ModelColorPowerType::getBlue),
                    Codec.FLOAT.optionalFieldOf("alpha", 1.0f).forGetter(ModelColorPowerType::getAlpha))
                    .apply(inst, ModelColorPowerType::new));

    @Nullable
    @Override
    public Codec<ModelColorPowerType> getCodec() {
        return CODEC;
    }

    @Override
    public PacketCodec<? super RegistryByteBuf, ModelColorPowerType> getPacketCodec() {
        return null;
    }

}
