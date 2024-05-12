package io.github.thatrobin.apolirewrite.mixin;

import io.github.thatrobin.apolirewrite.powers.*;
import io.github.thatrobin.apolirewrite.powers.power_types.ElytraFlightPowerType;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Optional;

@Mixin(ElytraFeatureRenderer.class)
public class ElytraFeatureRendererMixin {
    @Unique
    private LivingEntity livingEntity;

    @Redirect(method = "render*", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean modifyEquippedStackToElytra(ItemStack itemStack, Item item, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, LivingEntity livingEntity, float f, float g, float h, float j, float k, float l) {
        this.livingEntity = livingEntity;
        MutableBoolean mutableBoolean = new MutableBoolean(false);
        List<ElytraFlightPowerType> elytraFlightPowerTypeList = PowerHolderComponent.getPowers(livingEntity, ElytraFlightPowerType.class);
        if(elytraFlightPowerTypeList.stream().anyMatch(ElytraFlightPowerType::getRenderElytra)) {
            mutableBoolean.setTrue();
        }
        if(mutableBoolean.booleanValue()) return true;
        return itemStack.isOf(item);
    }

    @ModifyArg(method = "render*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getArmorCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private Identifier setTexture(Identifier identifier) {
        List<ElytraFlightPowerType> elytraFlightPowerTypeList = PowerHolderComponent.getPowers(livingEntity, ElytraFlightPowerType.class);
        Optional<ElytraFlightPowerType> elytraFlight = elytraFlightPowerTypeList.stream().filter((elytraFlightPowerType -> elytraFlightPowerType.getTextureLocation() != null)).findFirst();
        if(elytraFlight.isPresent()) {
            return elytraFlight.get().getTextureLocation();
        }
        return identifier;
    }
}
