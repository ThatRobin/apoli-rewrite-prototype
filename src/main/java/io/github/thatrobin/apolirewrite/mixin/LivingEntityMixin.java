package io.github.thatrobin.apolirewrite.mixin;

import io.github.thatrobin.apolirewrite.utils.EntityLinkedItemStack;
import io.github.thatrobin.apolirewrite.utils.InventoryUtils;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "baseTick", at = @At("TAIL"))
    private void updateItemStackHolder(CallbackInfo ci) {
        InventoryUtils.forEachStack((LivingEntity)(Object)this, stack -> ((EntityLinkedItemStack)(Object) stack).apoli$setEntity((LivingEntity)(Object)this));
    }
}
