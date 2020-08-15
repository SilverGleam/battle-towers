package com.passer.battletowers.registry;

import com.passer.battletowers.BattleTowers;
import com.passer.battletowers.world.BattleTowerStairsPieces;
import com.passer.battletowers.world.BattleTowerStructure;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class StructurePiecesInit {
	
	public static final DeferredRegister<Feature<?>> FEATURES = new DeferredRegister<>(ForgeRegistries.FEATURES, "battle-towers");
	public static final RegistryObject<Structure<NoFeatureConfig>> BATTLE_TOWER = registerStructure("battle_tower", new BattleTowerStructure(NoFeatureConfig::deserialize));
	
	private static <T extends Feature<?>> RegistryObject<T> registerStructure(String name, T feature) {
        BattleTowers.LOGGER.info(name + " BattleTower registered");
        return FEATURES.register(name, () -> feature);
    }
	public static final IStructurePieceType BATTLE_TOWER_PIECE = registerPiece("battle_tower", BattleTowerStairsPieces.Piece::new);
	
	private static IStructurePieceType registerPiece(String key, IStructurePieceType type) {
		BattleTowers.LOGGER.info(key + " structure piece registered");
        return Registry.register(Registry.STRUCTURE_PIECE, new ResourceLocation("battle-towers", key), type);
    }

}
