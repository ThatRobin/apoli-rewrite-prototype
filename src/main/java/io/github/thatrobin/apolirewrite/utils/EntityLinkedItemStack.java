package io.github.thatrobin.apolirewrite.utils;

import net.minecraft.entity.Entity;

public interface EntityLinkedItemStack {
    Entity apoli$getEntity();

    Entity apoli$getEntity(boolean prioritiseVanillaHolder);

    void apoli$setEntity(Entity entity);
}
