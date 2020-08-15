package com.passer.battletowers.world;

import com.passer.battletowers.BattleTowers;
import com.passer.battletowers.registry.StructurePiecesInit;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class BattleTowerStairsPieces {
	
	public static BattleTowerStairsPieces battleTower;
	
	private static final ResourceLocation TOWER_TOP = new ResourceLocation("battle-towers", "tower_stairs_template/tower_stairs_top");
	private static final ResourceLocation TOWER_ENTRANCE = new ResourceLocation("battle-towers", "tower_stairs_template/tower_stairs_entrance");
	/**----------------------------------------------**/
	/** Loot tables for each layers chest **/
	private static final ResourceLocation LOOT_HIGH_LAYER = BattleTowersConfig.high_loot;
	private static final ResourceLocation LOOT_LOW_LAYER = BattleTowersConfig.low_loot;
	private static final ResourceLocation LOOT_TOP_LAYER = BattleTowersConfig.top_chest;
	/**------------------------------------**/
	
	public BattleTowerStairsPieces(TemplateManager structureManager, BlockPos blockPos, Rotation rotation, List<StructurePiece> pieces, Random random, NoFeatureConfig featureConfig,String folder){
		battleTower = this;
		Piece base;
	    // spawn in base of tower
		int floor_total = BattleTowersConfig.floor_minimum + random.nextInt(BattleTowersConfig.floor_random);
		pieces.add(base = new BattleTowerStairsPieces.Piece(structureManager, TOWER_ENTRANCE, blockPos, rotation));
	    int high = 0;
	    boolean have_transitioned = false;
	    for (int i = 1; i < floor_total; i++) {//Create layers
	        pieces.add(new BattleTowerStairsPieces.Piece(structureManager, new ResourceLocation("battle-towers", folder+"/tower_stairs_" +(high == 1 && !have_transitioned ? (high-1)+"_transition" : high+"_normal")), blockPos, rotation, i, base));
	        if(high != 1 && i >= (floor_total/2)){
	        	high = 1;
	        }else if(i == ((floor_total/2)+1)){
	        	have_transitioned = true;
	        }
	    }
	    // finish with the top
	    pieces.add(new BattleTowerStairsPieces.Piece(structureManager, TOWER_TOP, blockPos, rotation,floor_total, base).setTop());
	}
	 
	 public static class Piece extends TemplateStructurePiece {
		 private final ResourceLocation structurePart;
	     private final Rotation rotation;
	     private int floor = 0;
	     private int baseHeight = 0;
	     public Piece base;
	     
	     public Piece(TemplateManager templateManager, ResourceLocation structurePart, BlockPos absolutePos, Rotation rotation) {//Entrance
	    	 super(StructurePiecesInit.BATTLE_TOWER_PIECE, 0);
	         this.structurePart = structurePart;
	         this.templatePosition = absolutePos;
	         this.rotation = rotation;
	         this.initializePlacementData(templateManager);
	     }

	     public Piece(TemplateManager templateManager, ResourceLocation structurePart, BlockPos absolutePos, Rotation rotation, int floor, Piece base) {//Filler
	    	 super(StructurePiecesInit.BATTLE_TOWER_PIECE, 0);
	         this.structurePart = structurePart;
	         this.templatePosition = absolutePos;
	         this.rotation = rotation;
	         this.floor = floor;
	         this.base = base;
	         this.initializePlacementData(templateManager);
	     }

	     public Piece(TemplateManager templateManager, CompoundNBT p_i50566_2_) {
	         super(StructurePiecesInit.BATTLE_TOWER_PIECE, p_i50566_2_);
	         this.structurePart = new ResourceLocation(p_i50566_2_.getString("Template"));
	         this.rotation = Rotation.valueOf(p_i50566_2_.getString("Rot"));
	         this.initializePlacementData(templateManager);
	     }

	     private void initializePlacementData(TemplateManager templateManager) {
	         Template template = templateManager.getTemplateDefaulted(this.structurePart);
	         PlacementSettings structurePlacementData_1  = (new PlacementSettings().setRotation(this.rotation).setMirror(floor != 0 && floor % 2 != 0 ? Mirror.FRONT_BACK : Mirror.NONE)).addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
	         this.setup(template, this.templatePosition, structurePlacementData_1);
	     }
	     
	     public int getBaseHeight(){
	    	 return baseHeight;
	     }
	     
	     public Piece setTop(){
	    	 baseHeight = 1;
	    	 return this;
	     }

	     /**
	      * (abstract) Helper method to read subclass data from NBT
	      */
	     protected void readAdditional(CompoundNBT tagCompound) {
	     	super.readAdditional(tagCompound);
	        tagCompound.putString("Template", this.structurePart.toString());
	        tagCompound.putString("Rot", this.rotation.name());
	     }

	     protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand, MutableBoundingBox sbb) {
	    	 TileEntity blockEntity = null;
	         switch(function){
	         	case "low_spawner":
	         		worldIn.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 2);
	         		blockEntity = worldIn.getTileEntity(pos);
	                if(blockEntity instanceof MobSpawnerTileEntity){
	                	CompoundNBT mobInSpawn = new CompoundNBT();
	         			CompoundNBT spawn_potentials = new CompoundNBT();
	         			if(rand.nextBoolean()){
	         				mobInSpawn.putString("id", "minecraft:zombie");
	         				mobInSpawn.putBoolean("IsBaby", true);
	         				spawn_potentials = new CompoundNBT();
			         		spawn_potentials.putInt("Weight", 10);
			         		spawn_potentials.put("Entity", mobInSpawn);
	         			}else{
	         				mobInSpawn.putString("id", "minecraft:skeleton");
	         				spawn_potentials = new CompoundNBT();
			         		spawn_potentials.putInt("Weight", 10);
			         		spawn_potentials.put("Entity", mobInSpawn);
	         			}
	                	((MobSpawnerTileEntity) blockEntity).getSpawnerBaseLogic().setDelayToMin(0);
		         		((MobSpawnerTileEntity) blockEntity).getSpawnerBaseLogic().setNextSpawnData(new WeightedSpawnerEntity(spawn_potentials));
	         		}
	         	break;
	         	case "high_spawner":
	         		worldIn.setBlockState(pos, Blocks.SPAWNER.getDefaultState(), 2);
	         		blockEntity = worldIn.getTileEntity(pos);
	         		if(blockEntity instanceof MobSpawnerTileEntity){
	         			CompoundNBT mobInSpawn = new CompoundNBT();
	         			CompoundNBT spawn_potentials = new CompoundNBT();
	         			if(rand.nextBoolean()){
	         				
			         		mobInSpawn.putString("id", "minecraft:cave_spider");
	         				spawn_potentials = new CompoundNBT();
			         		spawn_potentials.putInt("Weight", 10);
			         		spawn_potentials.put("Entity", mobInSpawn);
	         			}else{
	         				mobInSpawn.putString("id", "minecraft:witch");
	         				spawn_potentials = new CompoundNBT();
			         		spawn_potentials.putInt("Weight", 10);
			         		spawn_potentials.put("Entity", mobInSpawn);
	         			}
	                	((MobSpawnerTileEntity) blockEntity).getSpawnerBaseLogic().setDelayToMin(0);
		         		((MobSpawnerTileEntity) blockEntity).getSpawnerBaseLogic().setNextSpawnData(new WeightedSpawnerEntity(spawn_potentials));
	         		}
	         	break;
	         	case "low_chest":
	         		worldIn.setBlockState(pos, Blocks.CHEST.getDefaultState(), 2);
	         		blockEntity = worldIn.getTileEntity(pos);
	         		if(blockEntity instanceof ChestTileEntity){
	         			((ChestTileEntity) blockEntity).setLootTable(LOOT_LOW_LAYER, rand.nextLong());
	         		}
	         	break;
	         	case "high_chest":
	         		worldIn.setBlockState(pos, Blocks.CHEST.getDefaultState(), 2);
	         		blockEntity = worldIn.getTileEntity(pos);
	         		if(blockEntity instanceof ChestTileEntity){
	         			((ChestTileEntity) blockEntity).setLootTable(LOOT_HIGH_LAYER, rand.nextLong());
	         		}
	         	break;
	         	case "top_chest":
	         		worldIn.setBlockState(pos, Blocks.CHEST.getDefaultState(), 2);
	         		blockEntity = worldIn.getTileEntity(pos);
	         		if(blockEntity instanceof ChestTileEntity){
	         			((ChestTileEntity) blockEntity).setLootTable(LOOT_TOP_LAYER, rand.nextLong());
	         		}
	         	break;
	         	case "secret_chest":
	         		worldIn.setBlockState(pos, Blocks.CHEST.getDefaultState(), 2);
	         		blockEntity = worldIn.getTileEntity(pos);
	         		if(blockEntity instanceof ChestTileEntity){
	         			((ChestTileEntity) blockEntity).setLootTable(LOOT_HIGH_LAYER, rand.nextLong());
	         		}
	         	break;
	         	case "boss":
	         		worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
	         		MobEntity boss;
	         		if(rand.nextBoolean()){
	         			boss = EntityType.EVOKER.create(worldIn.getWorld());
	         		}else{
	         			boss = EntityType.ILLUSIONER.create(worldIn.getWorld());
	         		}
	         		boss.enablePersistence();
         			boss.setHomePosAndDistance(pos, 8);
         			boss.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
         			boss.onInitialSpawn(worldIn, worldIn.getDifficultyForLocation(pos), SpawnReason.STRUCTURE, (ILivingEntityData)null, (CompoundNBT)null);
         			worldIn.addEntity(boss);
	         	break;
	         }
	     }
	     @Override
	     public boolean create(IWorld world, ChunkGenerator<?> chunkGeneratorIn, Random random, MutableBoundingBox mutableIntBoundingBox, ChunkPos chunkPos) {
	    	if(floor!=0){//Top/Filler
	    		if(floor == 0)BattleTowers.LOGGER.debug("BattleTowers -- Floor is 0!");
	    		this.templatePosition = new BlockPos(floor % 2 != 0 ? base.templatePosition.getX() + 16 : base.templatePosition.getX(), (base.getBaseHeight() + (floor*4)) - (baseHeight == 1 ? 2 : 0), base.templatePosition.getZ());
	    	}else{//Entrance
	        		int height;
			        int minHeight = Integer.MAX_VALUE;
			        for (int x=0; x<8; x++) {// Get Minimum height, radius of tower
			        	for (int y=0; y<8; y++) {
			        		height = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, this.templatePosition.getX() + x, this.templatePosition.getZ() + y);
			                if (height < minHeight) {
			                	minHeight = (height);
			                    baseHeight = minHeight;
			                }
			             }
			        }
			        this.templatePosition = new BlockPos(this.templatePosition.getX(), minHeight, this.templatePosition.getZ());
	    	}
	        return super.create(world, chunkGeneratorIn, random, mutableIntBoundingBox, chunkPos);
	     }
	 }
}