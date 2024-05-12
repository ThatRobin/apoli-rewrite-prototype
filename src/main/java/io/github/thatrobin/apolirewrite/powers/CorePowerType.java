package io.github.thatrobin.apolirewrite.powers;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.thatrobin.apolirewrite.Apoli;
import io.github.thatrobin.apolirewrite.powers.power_types.PowerType;
import lombok.Getter;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
public class CorePowerType implements PowerType {

    private final Text name, description;
    private final Map<Either<Identifier,String>, PowerType> undefinedMap;
    private Map<Identifier, PowerType> subPowers;

    public static final Codec<CorePowerType> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec) TextCodecs.CODEC.fieldOf("name")).forGetter((power) -> ((CorePowerType)power).name),
            ((MapCodec) TextCodecs.CODEC.fieldOf("description")).forGetter((power) -> ((CorePowerType)power).description),
            Codec.unboundedMap(Codec.either(Apoli.CODEC, Codec.STRING), PowerType.getPowerTypeCodec()).fieldOf("powers").forGetter((CorePowerType::getUndefinedMap))).apply(instance, (nameText, descText, powers) -> {
        return new CorePowerType((Text) nameText, (Text) descText, (Map<Either<Identifier, String>, PowerType>) powers);
    }));

    public CorePowerType(Text nameText, Text descText, Map<Either<Identifier, String>, PowerType> powers) {
        this.name = nameText;
        this.description = descText;
        this.undefinedMap = powers;
        this.subPowers = new HashMap<>();
    }

    @Override
    public String toString() {
        return "{name:" + name.getString() + ", description:" + description.getString() + ", powers: " + subPowers + "}";
    }

    public <T extends PowerType> List<T> getPowers(Class<T> type) {
        List<T> powers = new LinkedList<>();
        for (PowerType powerType : subPowers.values()) {
            if(powerType instanceof CorePowerType corePowerType) {
                if(type.isAssignableFrom(corePowerType.getClass())) {
                    powers.add((T) corePowerType);
                }
            } else if(type.isAssignableFrom(powerType.getClass())) {
                powers.add((T) powerType);
            }

        }
        return powers;
    }

    @Nullable
    @Override
    public Codec<CorePowerType> getCodec() {
        return CorePowerType.CODEC;
    }

    @Override
    public PacketCodec getPacketCodec() {
        return null;
    }
}
