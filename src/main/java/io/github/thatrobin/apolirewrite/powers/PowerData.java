package io.github.thatrobin.apolirewrite.powers;

import com.mojang.serialization.Codec;

public class PowerData {
    public static final Codec<PowerData> CODEC = Codec.unit(PowerData::new);

    public PowerData() {
    }
}
