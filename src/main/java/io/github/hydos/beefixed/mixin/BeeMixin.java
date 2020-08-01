package io.github.hydos.beefixed.mixin;

import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeeEntity.BeeWanderAroundGoal.class)
public abstract class BeeMixin {

    @Shadow
    public BeeEntity field_20380;

    @Inject(method = "getRandomLocation", at = @At("RETURN"), cancellable = true)
    private void getRandomLocationButGood(CallbackInfoReturnable<Vec3d> cir) {
        Vec3d output = cir.getReturnValue();
        if (output == null || field_20380.getPos().getY() < 1) {
            field_20380.addVelocity(0, 0.5, 0);
        }
        cir.setReturnValue(output);
    }
}
