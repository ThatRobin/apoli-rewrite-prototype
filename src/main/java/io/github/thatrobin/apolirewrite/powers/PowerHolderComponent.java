package io.github.thatrobin.apolirewrite.powers;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.thatrobin.apolirewrite.Apoli;
import io.github.thatrobin.apolirewrite.powers.power_types.PowerType;
import io.github.thatrobin.apolirewrite.utils.AttachmentSyncer;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.Entity;

import java.util.*;

public record PowerHolderComponent(Map<PowerType, PowerData> powers) {

    public static final Codec<PowerHolderComponent> CODEC = RecordCodecBuilder.create(inst ->
            inst.group(Codec.list(Codec.pair(PowerType.getPowerTypeCodec(), PowerData.CODEC)).fieldOf("powers").forGetter(PowerHolderComponent::getListPair))
                    .apply(inst, (listPair) -> new PowerHolderComponent(getMap(listPair))));

    public static <T extends PowerType> List<T> getPowers(Entity entity, Class<T> type) {
        PowerHolderComponent component = entity.getAttachedOrCreate(PowerHolderComponent.POWERS);
        List<T> powers = new LinkedList<>();
        for (PowerType powerType : component.powers().keySet()) {
            if(powerType instanceof CorePowerType corePowerType1) {
                powers.addAll(corePowerType1.getPowers(type));
            }
            if(powerType.getClass().isAssignableFrom(type)) {
                powers.add((T) powerType);
            }
        }
        return powers;
    }

    private List<Pair<PowerType, PowerData>> getListPair() {
        List<Pair<PowerType, PowerData>> list = new ArrayList<>();
        for (Map.Entry<PowerType, PowerData> entry : powers.entrySet()) {

            list.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        return list;
    }

    public static <T extends PowerType> Map<T, PowerData> getMap(List<Pair<T, PowerData>> list) {
        Map<T, PowerData> map = new HashMap<>();
        for (Pair<T, PowerData> pair : list) {
            map.put(pair.getFirst(), pair.getSecond());
        }
        return map;
    }

    public void tick(Entity entity) {
        //PowerHolderComponent.getPowers(entity, TickingPower.class).forEach(tickingPower -> tickingPower.tick(entity));
    }

    public static final AttachmentType<PowerHolderComponent> POWERS = AttachmentRegistry.<PowerHolderComponent>builder()
            .persistent(PowerHolderComponent.CODEC)
            .copyOnDeath()
            .initializer(()->new PowerHolderComponent(new HashMap<>()))
            .buildAndRegister(Apoli.identifier("powers"));

    public void addPower(PowerType power) {
        this.powers.put(power, new PowerData());
    }

    public static boolean hasPower(Entity entity, Class<? extends PowerType> clazz) {
        return entity.getAttachedOrCreate(PowerHolderComponent.POWERS).powers().keySet().stream()
                    .anyMatch(power -> clazz.isAssignableFrom(power.getClass()));
    }

    public void sync(Entity entity) {
        if(entity != null) {
            entity.setAttached(PowerHolderComponent.POWERS, this);
            ((AttachmentSyncer) entity).apoli_rewrite$syncAttachment();
        }
    }

    public boolean isEmpty() {
        return powers.isEmpty();
    }

}
