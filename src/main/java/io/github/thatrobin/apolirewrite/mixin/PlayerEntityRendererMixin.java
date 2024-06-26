package io.github.thatrobin.apolirewrite.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.thatrobin.apolirewrite.powers.PowerHolderComponent;
import io.github.thatrobin.apolirewrite.powers.power_types.ModelColorPowerType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

    @WrapOperation(method = "renderArm", at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", ordinal = 0), @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", ordinal = 1)})
    private void apoli$makeArmAndSleeveTransparent(ModelPart instance, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, Operation<Void> original, MatrixStack mMatrices, VertexConsumerProvider mVertexConsumers, int mLight, AbstractClientPlayerEntity mPlayer, @Local Identifier skinTextureId) {

        List<ModelColorPowerType> modelColorPowerTypes = PowerHolderComponent.getPowers(mPlayer, ModelColorPowerType.class);
        if (modelColorPowerTypes.isEmpty()) {
            original.call(instance, matrices, vertices, light, overlay);
            return;
        }
        float red = modelColorPowerTypes.stream().map(ModelColorPowerType::getRed).reduce((a, b) -> a * b).orElse(1.0f);
        float green = modelColorPowerTypes.stream().map(ModelColorPowerType::getGreen).reduce((a, b) -> a * b).orElse(1.0f);
        float blue = modelColorPowerTypes.stream().map(ModelColorPowerType::getBlue).reduce((a, b) -> a * b).orElse(1.0f);
        float alpha = modelColorPowerTypes.stream().map(ModelColorPowerType::getAlpha).min(Float::compare).orElse(1.0f);

        instance.render(matrices, mVertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(skinTextureId)), light, overlay, red, green, blue, alpha);

    }

}
