package io.github.thatrobin.apolirewrite.mixin;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.util.dynamic.CodecCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DataComponentTypes.class)
public interface DataComponentTypesAccessor {

    @Accessor("CACHE")
    public static CodecCache getCache() {
        return null;
    }

}
