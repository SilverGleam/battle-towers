package com.passer.battletowers;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.passer.battletowers.registry.StructurePiecesInit;
import com.passer.battletowers.registry.Structures;
import com.passer.battletowers.world.BattleTowersConfig;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("battle-towers")
public class BattleTowers
{
    public static final Logger LOGGER = LogManager.getLogger();

    public BattleTowers() {
    	final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BattleTowersConfig.COMMON_SPEC);
        StructurePiecesInit.FEATURES.register(modEventBus);
        modEventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event){
    	Structures.setup();
    }
}
