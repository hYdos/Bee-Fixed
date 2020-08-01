package io.github.hydos.beefixed.fluid;

import io.github.hydos.beefixed.BeeFixed;
import net.minecraft.block.*;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import javax.annotation.Nullable;

public class HoneyFluid extends FlowableFluid {

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == getStill() || fluid == getFlowing();
    }

    public static boolean placeFluid(World world, BlockPos pos, @Nullable BlockHitResult blockHitResult, FlowableFluid fluid) {
        if (fluid == null) {
            return false;
        } else {
            BlockState blockState = world.getBlockState(pos);
            Block block = blockState.getBlock();
            Material material = blockState.getMaterial();
            boolean bl = blockState.canBucketPlace(fluid);
            boolean bl2 = blockState.isAir() || bl || block instanceof FluidFillable && ((FluidFillable) block).canFillWithFluid(world, pos, blockState, fluid);
            if (!bl2) {
                return blockHitResult != null && placeFluid(world, blockHitResult.getBlockPos().offset(blockHitResult.getSide()), null, fluid);
            } else if (block instanceof FluidFillable && fluid == BeeFixed.HONEY_LIQUID) {
                ((FluidFillable) block).tryFillWithFluid(world, pos, blockState, fluid.getStill(false));
                return true;
            } else {
                if (!world.isClient && bl && !material.isLiquid()) {
                    world.breakBlock(pos, true);
                }

                return world.setBlockState(pos, fluid.getDefaultState().getBlockState(), 11) || blockState.getFluidState().isStill();
            }
        }
    }

    @Override
    public Item getBucketItem() {
        return Items.HONEY_BOTTLE;
    }

    @Override
    protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    @Override
    public int getTickRate(WorldView world) {
        return 0;
    }

    @Override
    protected float getBlastResistance() {
        return 0;
    }

    @Override
    public BlockState toBlockState(FluidState state) {
        return BeeFixed.HONEY_LIQUID_BLOCK.getDefaultState().with(FluidBlock.LEVEL, method_15741(state));
    }

    @Override
    public boolean isStill(FluidState state) {
        return false;
    }

    @Override
    public int getLevel(FluidState state) {
        return state.getLevel();
    }

    @Override
    public Fluid getFlowing() {
        return BeeFixed.HONEY_LIQUID_FLOWING;
    }

    @Override
    public Fluid getStill() {
        return BeeFixed.HONEY_LIQUID;
    }

    @Override
    protected boolean isInfinite() {
        return false;
    }

    @Override
    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
    }

    @Override
    protected int getFlowSpeed(WorldView world) {
        return 2;
    }

    @Override
    protected int getLevelDecreasePerBlock(WorldView world) {
        return 1;
    }

    public static class Flowing extends HoneyFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends HoneyFluid {
        @Override
        public int getLevel(FluidState fluidState) {
            return 9;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}
