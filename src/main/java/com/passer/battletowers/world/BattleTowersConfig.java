package com.passer.battletowers.world;

import com.passer.battletowers.BattleTowers;
import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = "battle-towers", bus = Mod.EventBusSubscriber.Bus.MOD)
public class BattleTowersConfig {
	
	public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    public static int rarity;
    public static List<String> all_mod_biomes_blacklist;
    public static List<String> biome_blacklist;
    public static int floor_minimum;
    public static int floor_random;
    
    public static ResourceLocation low_loot = new ResourceLocation("battle-towers:low_loot");
    public static ResourceLocation high_loot = new ResourceLocation("battle-towers:high_loot");
    public static ResourceLocation top_chest = new ResourceLocation("battle-towers:top_chest");
    
    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(BattleTowersConfig.CommonConfig::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }
    
    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == BattleTowersConfig.COMMON_SPEC) {
            bakeConfig();
            BattleTowers.LOGGER.info("Config set");
        }
    }
    
    public static void bakeConfig() {
        rarity = COMMON.tower_rarity.get();
        all_mod_biomes_blacklist = COMMON.all_mod_biomes_blacklist.get();
        biome_blacklist = COMMON.biome_blacklist.get();
        floor_minimum = COMMON.floor_minimum.get();
        floor_random = COMMON.floor_random.get();
    }
    
    public static class CommonConfig {

        public final ForgeConfigSpec.IntValue tower_rarity;
        public final ForgeConfigSpec.ConfigValue<List<String>> all_mod_biomes_blacklist;
        public final ForgeConfigSpec.ConfigValue<List<String>> biome_blacklist;
        public final ForgeConfigSpec.IntValue floor_minimum;
        public final ForgeConfigSpec.IntValue floor_random;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("battle_towers");
            tower_rarity = builder
                    .comment("How rarely the towers will spawn (low: common, high: rare). Default: 35")
                    .defineInRange("tower_rarity", 35, 10, 1000);

            all_mod_biomes_blacklist = builder
                    .comment("A list of mod ids. In each and every biome added by those mods, the towers will not spawn. Default : The Midnight.")
                    .define("all_mod_biomes_blacklist",
                            Lists.newArrayList(
                                "midnight"
                            ));

            biome_blacklist = builder
                    .comment("A list of biomes where the towers will not spawn. Default: Oceans, Rivers, Beaches, Nether, End.")
                    .define("biome_blacklist",
                            Lists.newArrayList(
                                "minecraft:ocean",
                                "minecraft:deep_ocean",
                                "minecraft:frozen_ocean",
                                "minecraft:deep_frozen_ocean",
                                "minecraft:cold_ocean",
                                "minecraft:deep_cold_ocean",
                                "minecraft:lukewarm_ocean",
                                "minecraft:deep_lukewarm_ocean",
                                "minecraft:warm_ocean",
                                "minecraft:deep_warm_ocean",
                                "minecraft:river",
                                "minecraft:frozen_river",
                                "minecraft:beach",
                                "minecraft:stone_shore",
                                "minecraft:snowy_beach",
                                "minecraft:nether",
                                "minecraft:the_end",
                                "minecraft:small_end_islands",
                                "minecraft:end_midlands",
                                "minecraft:end_highlands",
                                "minecraft:end_barrens",
                                "minecraft:the_void",
                                "biomesoplenty:gravel_beach",
                                "biomesoplenty:white_beach",
                                "biomesoplenty:ashen_inferno",
                                "biomesoplenty:undergarden",
                                "biomesoplenty:visceral_heap"
                    ));
            floor_minimum = builder
                    .comment("Minimum number of floors a tower will have. Default: 11 - Range between 3-30")
                    .defineInRange("floor_minimum", 11, 3, 30);
            
            floor_random = builder
                    .comment("Random number of floors that can be added. Default: 3 - Range between 0-11")
                    .defineInRange("floor_random", 3, 0, 11);

            builder.pop();
        }
    }
	
}
