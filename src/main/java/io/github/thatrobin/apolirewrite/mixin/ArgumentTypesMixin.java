package io.github.thatrobin.apolirewrite.mixin;

import com.mojang.brigadier.arguments.ArgumentType;
import io.github.thatrobin.apolirewrite.Apoli;
import io.github.thatrobin.apolirewrite.commands.PowerHolderArgumentType;
import io.github.thatrobin.apolirewrite.commands.PowerTypeArgumentType;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.serialize.ArgumentSerializer;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ArgumentTypes.class)
public abstract class ArgumentTypesMixin {

    @Unique
    private static <A extends ArgumentType<?>, T extends ArgumentSerializer.ArgumentTypeProperties<A>> ArgumentSerializer<A, T> register(Registry<ArgumentSerializer<?, ?>> registry, String string, Class<? extends A> clazz, ArgumentSerializer<A, T> argumentSerializer) {
        CLASS_MAP.put(clazz, argumentSerializer);
        return Registry.register(registry, string, argumentSerializer);
    }

    @Shadow @Final private static Map<Class<?>, ArgumentSerializer<?, ?>> CLASS_MAP;

    @Inject(method = "register(Lnet/minecraft/registry/Registry;)Lnet/minecraft/command/argument/serialize/ArgumentSerializer;", at = @At("RETURN"))
    private static void registerApoliArgumentTypes(Registry<ArgumentSerializer<?, ?>> registry, CallbackInfoReturnable<ArgumentSerializer<?, ?>> cir) {
        register(registry, Apoli.MODID + ":power", PowerTypeArgumentType.class, ConstantArgumentSerializer.of(PowerTypeArgumentType::power));
        register(registry , Apoli.MODID + ":power_holder", PowerHolderArgumentType.class, new EntityArgumentType.Serializer());
    }
}
