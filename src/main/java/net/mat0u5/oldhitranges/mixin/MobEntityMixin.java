package net.mat0u5.oldhitranges.mixin;

import net.fabricmc.fabric.impl.object.builder.FabricEntityTypeImpl;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.VindicatorEntity;
import net.minecraft.entity.passive.PolarBearEntity;
import net.minecraft.entity.passive.RabbitEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class MobEntityMixin {

    @Inject(method = "isInAttackRange", at = @At("HEAD"), cancellable = true)
    public void isInAttackRange(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        MobEntity mob = (MobEntity)(Object)this;
        double distance = mob.squaredDistanceTo(entity.getX(), entity.getY(), entity.getZ());
        double attackDistance = 0;
        if (mob instanceof RavagerEntity) attackDistance = getSquaredMaxAttackDistance(mob.getWidth() - 0.1f, entity);
        else if (mob instanceof SpiderEntity) attackDistance = 4.0f + mob.getWidth();
        else if (mob instanceof PolarBearEntity) attackDistance = 4.0f + entity.getWidth();
        else if (mob instanceof RabbitEntity) attackDistance = 4.0f + entity.getWidth();
        else if (mob instanceof VindicatorEntity) {
            if (mob.getVehicle() instanceof RavagerEntity) {
                float f = mob.getVehicle().getWidth() - 0.1f;
                attackDistance = f * 2.0f * (f * 2.0f) + entity.getWidth();
            }
            else {
                attackDistance = getSquaredMaxAttackDistance(mob, entity);
            }
        }
        else attackDistance = getSquaredMaxAttackDistance(mob, entity);

        boolean canAttack = attackDistance >= distance;
        cir.setReturnValue(canAttack);
    }
    public double getSquaredMaxAttackDistance(MobEntity mob, LivingEntity entity) {
        return mob.getWidth() * 2.0f * (mob.getWidth() * 2.0f) + entity.getWidth();
    }
    public double getSquaredMaxAttackDistance(double modifiedWidth, LivingEntity entity) {
        return modifiedWidth * 2.0f * (modifiedWidth * 2.0f) + entity.getWidth();
    }
}
