package com.RPGsys.RPGsysmod.rpg.passive;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;

import java.io.InputStreamReader;
import java.util.*;

public class RacePassiveManager {
    private static final Gson GSON = new Gson();
    private static final Map<String, Set<String>> PASSIVES = new HashMap<>();

    public static void init(ResourceManager manager) {
        reload(manager);

        System.out.println(
                "[RPGSYS] Loaded races: "
                        + PASSIVES.keySet()
        );
    }

    public static void reload(ResourceManager manager) {
        PASSIVES.clear();
        for (String race : List.of(
                "human",
                "vampire",
                "pix",
                "dwarf"
        )) {
            ResourceLocation id = ResourceLocation.parse(
                            "rpgsys:passives/" + race + ".json"
                    );
            Optional<Resource> resource = manager.getResource(id);

            if (resource.isEmpty()) {continue;}

            try (var reader = new InputStreamReader(resource.get().open()))
            {
                JsonObject root = GSON.fromJson(reader, JsonObject.class);
                JsonArray array = root.getAsJsonArray("skills");
                Set<String> set = new HashSet<>();
                array.forEach(e -> set.add(e.getAsString()));
                PASSIVES.put(race, set);
            } catch (Exception ignored) {}
        }
    }

    public static Set<String> getSkillsForRace(String race) {
        return PASSIVES.getOrDefault(race, Set.of());
    }
}