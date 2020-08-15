package com.passer.battletowers.registry;

import com.passer.battletowers.world.BattleTowersConfig;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;

public class Structures {
	
	public static void setup() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            if (!BattleTowersConfig.all_mod_biomes_blacklist.contains(biome.getRegistryName().getNamespace())) {
                if (!BattleTowersConfig.biome_blacklist.contains(biome.getRegistryName().toString())) {
                    addSurfaceStructure(biome, StructurePiecesInit.BATTLE_TOWER.get());
                }
            }
        }
    }
	
	private static void addSurfaceStructure(Biome biome, Structure<NoFeatureConfig> structure) {
        biome.addStructure(structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
        biome.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
    }

}
