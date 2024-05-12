package io.github.thatrobin.apolirewrite.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.thatrobin.apolirewrite.powers.PowerHolderComponent;
import io.github.thatrobin.apolirewrite.powers.power_types.ModelColorPowerType;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin extends EntityRenderer<LivingEntity> {

    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @WrapOperation(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"))
    private void apoli$renderColorChangedModel(EntityModel<?> model, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, Operation<Void> original, LivingEntity entity) {

        List<ModelColorPowerType> modelColorPowerTypes = PowerHolderComponent.getPowers(entity, ModelColorPowerType.class);

        if (modelColorPowerTypes.isEmpty()) {
            original.call(model, matrices, vertices, light, overlay, red, green, blue, alpha);
            return;
        }

        //  TODO: Implement custom blending modes for blending colors
        float newRed = modelColorPowerTypes
            .stream()
            .map(ModelColorPowerType::getRed)
            .reduce(red, (a, b) -> a * b);
        float newGreen = modelColorPowerTypes
            .stream()
            .map(ModelColorPowerType::getGreen)
            .reduce(green, (a, b) -> a * b);
        float newBlue = modelColorPowerTypes
            .stream()
            .map(ModelColorPowerType::getBlue)
            .reduce(blue, (a, b) -> a * b);
        float newAlpha = modelColorPowerTypes
            .stream()
            .map(ModelColorPowerType::getAlpha)
            .min(Float::compareTo)
            .map(alphaFactor -> alpha * alphaFactor)
            .orElse(alpha);

        original.call(model, matrices, vertices, light, overlay, newRed, newGreen, newBlue, newAlpha);

    }

}
