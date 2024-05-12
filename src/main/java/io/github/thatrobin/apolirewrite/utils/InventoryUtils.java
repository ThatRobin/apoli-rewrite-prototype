package io.github.thatrobin.apolirewrite.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SlotRanges;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;

import java.util.Objects;
import java.util.function.Consumer;

public class InventoryUtils {

    public static void forEachStack(Entity entity, Consumer<ItemStack> itemStackConsumer) {

        int slotToSkip = getDuplicatedSlotIndex(entity);
        for (String s : SlotRanges.streamSingleSlotNames().toList()) {
            int slot = SlotRanges.fromName(s).getSlotIds().getInt(0);

            if (slot == slotToSkip) {
                slotToSkip = Integer.MIN_VALUE;
                continue;
            }

            StackReference stackReference = entity.getStackReference(slot);
            if (stackReference == StackReference.EMPTY) {
                continue;
            }

            ItemStack stack = stackReference.get();
            if (!stack.isEmpty()) {
                itemStackConsumer.accept(stack);
            }

        }

        /*
        PowerHolderComponent component = PowerHolderComponent.KEY.maybeGet(entity).orElse(null);
        if (component == null) {
            return;
        }

        List<InventoryPower> inventoryPowers = component.getPowers(InventoryPower.class);
        for (InventoryPower inventoryPower : inventoryPowers) {
            for (int index = 0; index < inventoryPower.size(); index++) {

                ItemStack stack = inventoryPower.getStack(index);
                if (!stack.isEmpty()) {
                    itemStackConsumer.accept(stack);
                }

            }
        }
         */

    }

    private static int getDuplicatedSlotIndex(Entity entity) {
        if(entity instanceof PlayerEntity player) {
            int selectedSlot = player.getInventory().selectedSlot;
            return Objects.requireNonNull(SlotRanges.fromName("hotbar." + selectedSlot)).getSlotIds().getInt(0);
        }
        return Integer.MIN_VALUE;
    }
}
