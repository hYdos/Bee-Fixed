package io.github.hydos.beefixed.mixin;

import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(BeeEntity.BeeWanderAroundGoal.class)
public abstract class BeeMixin {

    @Shadow
    public BeeEntity field_20380;

    /**
     * @author hydos
     * @reason to fix a bug within the code. I doubt any other mod will change this method
     */
    @Nullable
    @Overwrite
    private Vec3d getRandomLocation() {
        Vec3d vec3d3;
        if (field_20380.isHiveValid() && !field_20380.isWithinDistance(Objects.requireNonNull(field_20380.getHivePos()), 22)) {
            Vec3d vec3d = Vec3d.ofCenter(field_20380.getHivePos());
            vec3d3 = vec3d.subtract(field_20380.getPos()).normalize();
        } else {
            vec3d3 = field_20380.getRotationVec(0.0F);
        }

        Vec3d vec3d4 = TargetFinder.findAirTarget(field_20380, 8, 7, vec3d3, 1.5707964F, 2, 1);
        Vec3d output =  vec3d4 != null ? vec3d4 : TargetFinder.findGroundTarget(field_20380, 8, 4, -2, vec3d3, 1.5707963705062866D);
        if(output == null || field_20380.getPos().getY() < 1){
            field_20380.addVelocity(0, 0.5, 0);
        }
        return output;
    }
}
