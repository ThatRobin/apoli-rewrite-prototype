package io.github.thatrobin.apolirewrite.powers;

import io.github.thatrobin.apolirewrite.powers.power_types.PowerType;
import net.minecraft.entity.Entity;
import org.apache.commons.lang3.mutable.MutableBoolean;

import java.util.List;
import java.util.function.Consumer;

public class PowerHelper {

    public static void forEachPower(Entity entity, Consumer<PowerType> consumer) {
        if(entity == null) return;
        List<PowerType> powerTypes = PowerHolderComponent.getPowers(entity, PowerType.class);
        for (PowerType powerType : powerTypes) {
            consumer.accept(powerType);
        }
    }

    public static boolean hasPowerOf(Entity entity, Class<? extends PowerType> clazz) {
        if(entity == null) return false;
        MutableBoolean mutableBoolean = new MutableBoolean(false);
        PowerHelper.forEachPower(entity, (PowerType powerType) -> {
            if(powerType.getClass().isAssignableFrom(clazz)) {
                mutableBoolean.setTrue();
            }
        });
        return mutableBoolean.booleanValue();
    }

}
