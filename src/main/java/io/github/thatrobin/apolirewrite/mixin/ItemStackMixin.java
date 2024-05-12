package io.github.thatrobin.apolirewrite.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.thatrobin.apolirewrite.utils.EntityLinkedItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements EntityLinkedItemStack {


    @Unique
    private Entity apoli$holdingEntity;

    @Shadow public abstract @Nullable Entity getHolder();

    @Override
    public Entity apoli$getEntity() {
        return apoli$getEntity(true);
    }

    @Override
    public Entity apoli$getEntity(boolean prioritiseVanillaHolder) {
        Entity vanillaHolder = this.getHolder();
        if(!prioritiseVanillaHolder || vanillaHolder == null) {
            return apoli$holdingEntity;
        }
        return vanillaHolder;
    }

    @Override
    public void apoli$setEntity(Entity entity) {
        this.apoli$holdingEntity = entity;
    }

    @ModifyReturnValue(method = "copy", at = @At("RETURN"))
    private ItemStack apoli$passHolderOnCopy(ItemStack original) {

        Entity holder = this.apoli$getEntity();
        if (holder != null) {

            if (original.isEmpty()) {
                //original = ModifyEnchantmentLevelPower.getOrCreateWorkableEmptyStack(holder);
            }

            else {
                ((EntityLinkedItemStack)(Object)original).apoli$setEntity(holder);
            }

        }

        return original;

    }
}
