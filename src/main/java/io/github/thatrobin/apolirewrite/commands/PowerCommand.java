package io.github.thatrobin.apolirewrite.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.thatrobin.apolirewrite.Apoli;
import io.github.thatrobin.apolirewrite.powers.*;
import io.github.thatrobin.apolirewrite.powers.power_types.PowerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PowerCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                literal("power")
                        .then(literal("grant")
                              .then(argument("targets", PowerHolderArgumentType.holders())
                                  .then(argument("power", PowerTypeArgumentType.power())
                                    .executes(PowerCommand::grantPower)
                                  )
                              )
                        )
                        .then(literal("list")
                                .then(argument("target", PowerHolderArgumentType.holder())
                                        .executes(context -> listPowers(context))
                                )
                        )
        );
    }

    private static int listPowers(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        LivingEntity target = PowerHolderArgumentType.getHolder(context, "target");

        List<Text> powersTooltip = new LinkedList<>();
        AtomicInteger finalPowers = new AtomicInteger();

        PowerHelper.forEachPower(target, (PowerType powerType) -> {
            List<Text> sourcesTooltip = new LinkedList<>();

            HoverEvent sourceHoverEvent = new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    Text.translatable("commands.apoli.list.sources", sourcesTooltip.size() == 1 ? "" : "s", Texts.join(sourcesTooltip, Text.of(",")))
            );

            Text powerTooltip = Text.literal(PowerRegistry.getId(powerType).toString())
                    .setStyle(Style.EMPTY.withHoverEvent(sourceHoverEvent));

            powersTooltip.add(powerTooltip);
            finalPowers.getAndIncrement();
        });

        int powers = finalPowers.get();

        if(powers == 0){
            source.sendFeedback(() -> Text.literal("Entity has no powers."), false);
        } else {
            source.sendFeedback(() -> Text.translatable("commands.apoli.list.pass", target.getName(), powers, powers == 1 ? "" : "s", Texts.join(powersTooltip, Text.of(", "))), true);
        }
        return powers;
    }

    private static int grantPower(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        try {
            for (LivingEntity target : PowerHolderArgumentType.getHolders(context, "targets")) {
                PowerType powerToAdd = PowerTypeArgumentType.getPower(context, "power");
                PowerHolderComponent component = target.getAttachedOrCreate(PowerHolderComponent.POWERS);
                Identifier id = PowerRegistry.getId(powerToAdd);
                Apoli.LOGGER.info(id);
                if(powerToAdd instanceof CorePowerType corePowerType) {
                    Apoli.LOGGER.info(id + " is a core power type");
                    component.addPower(corePowerType);
                }
                if(!target.getWorld().isClient) component.sync(target);
            }
            source.sendFeedback(() -> Text.literal("Power has been granted"), false);
            return 1;
        } catch (CommandSyntaxException e) {
            source.sendFeedback(() -> Text.literal(e.toString()), false);
            return 0;
        }
    }
}
