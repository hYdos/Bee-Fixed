package io.github.hydos.beefixed;

import io.github.hydos.beefixed.effect.HoneyStatusEffect;
import io.github.hydos.beefixed.fluid.HoneyFluid;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BeeFixed implements ModInitializer {

	private static final String MODID = "beefixed";
	public static HoneyStatusEffect HONEY_EFFECT = Registry.register(Registry.STATUS_EFFECT, new Identifier(MODID, "honey_particle"), new HoneyStatusEffect());

	public static final FlowableFluid HONEY_LIQUID = register("flowing_honey_liquid", new HoneyFluid.Still());
	public static final FlowableFluid HONEY_LIQUID_FLOWING = register("honey_liquid", new HoneyFluid.Flowing());

	public static final FluidBlock HONEY_LIQUID_BLOCK = Registry.register(Registry.BLOCK, new Identifier(MODID, "honey"), new FluidBlock(HONEY_LIQUID, FabricBlockSettings.copy(Blocks.WATER).build()){});

	@Override
	public void onInitialize() {
	}

	private static <T extends Fluid> T register(String id, T value) {
		return Registry.register(Registry.FLUID, new Identifier(MODID, id), value);
	}
}
