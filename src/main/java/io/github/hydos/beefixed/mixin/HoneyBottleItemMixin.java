package io.github.hydos.beefixed.mixin;

import io.github.hydos.beefixed.BeeFixed;
import io.github.hydos.beefixed.fluid.HoneyFluid;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoneyBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoneyBottleItem.class)
public class HoneyBottleItemMixin extends Item {

    public HoneyBottleItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "finishUsing", at = @At("TAIL"))
    public void applyHoneyEffect(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir){
        if(!world.isClient){
            user.addStatusEffect(new StatusEffectInstance(BeeFixed.HONEY_EFFECT, 120 * 20, 0, false, false));
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 10;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(user.isSneaking()){
            BlockHitResult hitResult = rayTrace(world, user, RayTraceContext.FluidHandling.NONE);
            BlockPos pos = hitResult.getBlockPos();
            HoneyFluid.placeFluid(world, pos, hitResult, BeeFixed.HONEY_LIQUID);
            return TypedActionResult.consume((user.getStackInHand(hand)));
        }else{
            return ItemUsage.consumeHeldItem(world, user, hand);
        }
    }


}
