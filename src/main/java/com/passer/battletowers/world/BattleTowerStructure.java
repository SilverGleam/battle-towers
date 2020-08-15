package com.passer.battletowers.world;

import java.util.Random;
import java.util.function.Function;

import com.passer.battletowers.registry.StructurePiecesInit;
import com.mojang.datafixers.Dynamic;

import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class BattleTowerStructure extends ScatteredStructure<NoFeatureConfig> {
	/** List of enemies that can spawn in the Pillage Outpost. */
	public static final String NAME = "battle-towers:battletower";
	private static int FEATURE_DISTANCE;
    private static final int FEATURE_SEPARATION = 5;

	public BattleTowerStructure(Function<Dynamic<?>, ? extends NoFeatureConfig> battleTowerConfigIn) {
		super(battleTowerConfigIn);
	}

	public String getStructureName() {
	return NAME;
	}
	   
	@Override
	public int getSize() {
	   return 17;
	}
	@Override
    protected ChunkPos getStartPositionForPosition(ChunkGenerator<?> generator, Random random, int chunkX, int chunkZ, int offsetX, int offsetZ) {
        FEATURE_DISTANCE = BattleTowersConfig.rarity;
        int chunkPosX = chunkX + FEATURE_DISTANCE * offsetX;
        int chunkPosZ = chunkZ + FEATURE_DISTANCE * offsetZ;
        int chunkPosX1 = chunkPosX < 0 ? chunkPosX - FEATURE_DISTANCE + 1 : chunkPosX;
        int chunkPosZ1 = chunkPosZ < 0 ? chunkPosZ - FEATURE_DISTANCE + 1 : chunkPosZ;
        int lvt_13_1_ = chunkPosX1 / FEATURE_DISTANCE;
        int lvt_14_1_ = chunkPosZ1 / FEATURE_DISTANCE;
        ((SharedSeedRandom)random).setLargeFeatureSeedWithSalt(generator.getSeed(), lvt_13_1_, lvt_14_1_, 16897777);
        lvt_13_1_ *= FEATURE_DISTANCE;
        lvt_14_1_ *= FEATURE_DISTANCE;
        lvt_13_1_ += random.nextInt(FEATURE_DISTANCE - FEATURE_SEPARATION);
        lvt_14_1_ += random.nextInt(FEATURE_DISTANCE - FEATURE_SEPARATION);
        return new ChunkPos(lvt_13_1_, lvt_14_1_);
    }
	@Override
	public Structure.IStartFactory getStartFactory() {
        return Start::new;
    }
	
	@Override
	protected int getSeedModifier() {
	   return 14357618;//52857294
	}
	
	@Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        return super.place(worldIn, generator, rand, pos, config);
    }
	
	public static class Start extends StructureStart {
        public Start(Structure<?> feature, int chunkX, int chunkZ, MutableBoundingBox box, int references, long l) {
            super(feature, chunkX, chunkZ, box, references, l);
        }

		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
            NoFeatureConfig nofeatureconfig = (NoFeatureConfig)generator.getStructureConfig(biomeIn, StructurePiecesInit.BATTLE_TOWER.get());
            int x = chunkX * 16;
            int z = chunkZ * 16;
            BlockPos startingPos = new BlockPos(x, 90, z);
            //this.rand.setSeed(14357618);
            Rotation rotation = Rotation.randomRotation(this.rand);
            /* Biome specific towers? */
            // Biome tower generation here
            // default:
            //BattleTowerStairsPieces battleTower = new BattleTowerStairsPieces(templateManagerIn, startingPos, rotation, this.components, this.rand, nofeatureconfig);
            BattleTowerStairsPieces battleTower = new BattleTowerStairsPieces(templateManagerIn, startingPos, Rotation.NONE, this.components, this.rand, nofeatureconfig,"tower_stairs_template");
            // break;
            /* ---------------------- */
            this.recalculateStructureSize();
        }
    }	
}
