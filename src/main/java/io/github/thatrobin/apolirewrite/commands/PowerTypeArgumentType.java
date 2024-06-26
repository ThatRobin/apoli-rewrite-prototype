package io.github.thatrobin.apolirewrite.commands;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.thatrobin.apolirewrite.powers.PowerRegistry;
import io.github.thatrobin.apolirewrite.powers.power_types.PowerType;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class PowerTypeArgumentType implements ArgumentType<Identifier> {

    public static final DynamicCommandExceptionType POWER_NOT_FOUND = new DynamicCommandExceptionType(
        o -> Text.translatable("commands.apoli.power_not_found", o)
    );

    public static PowerTypeArgumentType power() {
        return new PowerTypeArgumentType();
    }
    
    public Identifier parse(StringReader reader) throws CommandSyntaxException {
        return Identifier.fromCommandInput(reader);
    }

    public static PowerType getPower(CommandContext<ServerCommandSource> context, String argumentName) throws CommandSyntaxException {
        Identifier id = context.getArgument(argumentName, Identifier.class);
        try {
            return PowerRegistry.get(id);
        } catch (IllegalArgumentException e) {
            throw POWER_NOT_FOUND.create(id);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestIdentifiers(PowerRegistry.identifiers(), builder);
    }
}
