package io.github.thatrobin.apolirewrite.powers.power_types;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ElytraFlightPowerType extends PowerType {

    Boolean renderElytra;
    Identifier textureLocation;

    public static final Codec<ElytraFlightPowerType> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(Codec.BOOL.fieldOf("render_elytra").forGetter(ElytraFlightPowerType::getRenderElytra),
                    Identifier.CODEC.optionalFieldOf("texture_location", Identifier.tryParse("minecraft:textures/entity/elytra.png")).forGetter(ElytraFlightPowerType::getTextureLocation))
                    .apply(inst, ElytraFlightPowerType::new));

    @Override
    public Codec<ElytraFlightPowerType> getCodec() {
        return CODEC;
    }

}
