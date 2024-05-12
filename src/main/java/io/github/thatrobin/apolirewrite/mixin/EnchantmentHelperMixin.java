package io.github.thatrobin.apolirewrite.mixin;

import io.github.thatrobin.apolirewrite.Apoli;
import io.github.thatrobin.apolirewrite.powers.PowerHelper;
import io.github.thatrobin.apolirewrite.powers.power_types.PowerType;
import io.github.thatrobin.apolirewrite.utils.EntityLinkedItemStack;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(method = "hasAnyEnchantmentsWith", at = @At("RETURN"), cancellable = true)
    private static void hasAnyPowersWith(ItemStack stack, ComponentType<?> componentType, CallbackInfoReturnable<Boolean> cir) {
        if(componentType instanceof PowerType powerType) {
            Apoli.LOGGER.info(PowerHelper.hasPowerOf(((EntityLinkedItemStack)(Object)stack).apoli$getEntity(), powerType.getClass()));
            cir.setReturnValue(cir.getReturnValue() || PowerHelper.hasPowerOf(((EntityLinkedItemStack)(Object)stack).apoli$getEntity(), powerType.getClass()));
        }
    }

}
