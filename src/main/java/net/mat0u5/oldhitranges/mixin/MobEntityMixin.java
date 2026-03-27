package net.mat0u5.oldhitranges.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.polarbear.PolarBear;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.illager.Vindicator;
import net.minecraft.world.entity.monster.spider.Spider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobEntityMixin {

    @Inject(method = "isWithinMeleeAttackRange", at = @At("HEAD"), cancellable = true)
    public void isWithinMeleeAttackRange(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        Mob mob = (Mob)(Object)this;
        double distance = mob.distanceToSqr(entity.getX(), entity.getY(), entity.getZ());
        double attackDistance = 0;
        if (mob instanceof Ravager) attackDistance = getSquaredMaxAttackDistance(mob.getBbWidth() - 0.1f, entity);
        else if (mob instanceof Spider) attackDistance = 4.0f + mob.getBbWidth();
        else if (mob instanceof PolarBear) attackDistance = 4.0f + entity.getBbWidth();
        else if (mob instanceof Rabbit) attackDistance = 4.0f + entity.getBbWidth();
        else if (mob instanceof Vindicator) {
            if (mob.getVehicle() instanceof Ravager) {
                float f = mob.getVehicle().getBbWidth() - 0.1f;
                attackDistance = f * 2.0f * (f * 2.0f) + entity.getBbWidth();
            }
            else {
                attackDistance = getSquaredMaxAttackDistance(mob, entity);
            }
        }
        else attackDistance = getSquaredMaxAttackDistance(mob, entity);

        boolean canAttack = attackDistance >= distance;
        cir.setReturnValue(canAttack);
    }

    public double getSquaredMaxAttackDistance(Mob mob, LivingEntity entity) {
        return mob.getBbWidth() * 2.0f * (mob.getBbWidth() * 2.0f) + entity.getBbWidth();
    }

    public double getSquaredMaxAttackDistance(double modifiedWidth, LivingEntity entity) {
        return modifiedWidth * 2.0f * (modifiedWidth * 2.0f) + entity.getBbWidth();
    }
}