package io.github.thatrobin.apolirewrite.powers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import io.github.thatrobin.apolirewrite.Apoli;
import io.github.thatrobin.apolirewrite.powers.power_types.PowerType;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class PowerLoader implements SimpleSynchronousResourceReloadListener {

    public static final String PATH = "power";
    public static final String EXTENSION = ".json";

    @Override
    public Identifier getFabricId() {
        return Apoli.identifier("power");
    }

    @Override
    public void reload(ResourceManager manager) {
        PowerRegistry.clear();

        for (Identifier id : manager.findResources(PATH, path -> path.getPath().endsWith(EXTENSION)).keySet()) {
            if (manager.getResource(id).isPresent()) {

                try (InputStream stream = manager.getResource(id).get().getInputStream()) {
                    byte[] bytes = stream.readAllBytes();
                    String string = new String(bytes, StandardCharsets.UTF_8);

                    JsonObject jsonObject = JsonParser.parseString(string).getAsJsonObject();

                    Optional<? extends Pair<CorePowerType, JsonElement>> decoded = CorePowerType.CODEC.decode(JsonOps.INSTANCE, jsonObject).ifError(Apoli.LOGGER::error).result();

                    decoded.ifPresent((powerJsonElementPair -> {
                        int pfLen = (PATH + "/").length();
                        String fixedPath = id.getPath().substring(pfLen);
                        fixedPath = fixedPath.substring(0, fixedPath.length() - EXTENSION.length());
                        CorePowerType corePowerType = powerJsonElementPair.getFirst();
                        StringBuilder newId = new StringBuilder(id.getNamespace() + ":" + fixedPath);
                        Identifier powerId = Identifier.tryParse(newId.toString());
                        Apoli.LOGGER.info(newId.toString());
                        for (Map.Entry<Either<Identifier, String>, PowerType> eitherPowerTypeEntry : corePowerType.getUndefinedMap().entrySet()) {
                            StringBuilder newId2 = new StringBuilder(id.getNamespace() + ":" + fixedPath);
                            if(eitherPowerTypeEntry.getKey().right().isPresent()) {
                                newId2.append("_").append(eitherPowerTypeEntry.getKey().right().get());
                            } else if(eitherPowerTypeEntry.getKey().left().isPresent()) {
                                newId2 = new StringBuilder(eitherPowerTypeEntry.getKey().left().get().toString());
                            }
                            Apoli.LOGGER.info(newId2.toString());
                            Identifier powerId2 = Identifier.tryParse(newId2.toString());
                            corePowerType.getSubPowers().put(powerId2, eitherPowerTypeEntry.getValue());
                            PowerRegistry.register(powerId2, eitherPowerTypeEntry.getValue());
                        }
                        PowerRegistry.register(powerId, corePowerType);
                    }));


                } catch (Exception e) {
                    Apoli.LOGGER.error("Error occurred while loading Power " + id.toString(), e);
                }
            }
        }
    }
}
