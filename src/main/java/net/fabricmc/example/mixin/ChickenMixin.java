package net.fabricmc.example.mixin;

import btw.entity.mob.ChickenEntity;
import btw.item.BTWItems;
import net.minecraft.src.EntityChicken;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Mixin(ChickenEntity.class)
public abstract class ChickenMixin {


	@Inject(method = "isBreedingItem", at = @At("TAIL"), cancellable = true)
	private void injectIsBreedingItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack != null && (stack.itemID == BTWItems.chickenFeed.itemID || stack.itemID == BTWItems.hempSeeds.itemID)) {
			cir.setReturnValue(true);
		}
	}

	// Inject to set timeToLayEgg to 10 seconds after the current time
	@Inject(method = "onEatBreedingItem", at = @At("HEAD"), cancellable = true)
	private void modifyOnEatBreedingItem(CallbackInfo ci) {
		ChickenEntity chicken = (ChickenEntity) (Object) this;
		long timeToLayEgg = 4800L;
		try {
			Method getDeathSoundMethod = EntityChicken.class.getDeclaredMethod("getDeathSound");
			getDeathSoundMethod.setAccessible(true);
			String deathSound = (String) getDeathSoundMethod.invoke(chicken);

			Method getSoundVolumeMethod = EntityLiving.class.getDeclaredMethod("getSoundVolume");
			getSoundVolumeMethod.setAccessible(true);
			float soundVolume = (float) getSoundVolumeMethod.invoke(chicken);

			World world = chicken.worldObj;
			world.playSoundAtEntity(chicken, deathSound, soundVolume, chicken.rand.nextFloat() * 0.2F + 1.5F);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Use reflection to set the protected timeToLayEgg field
		try {
			Field timeToLayEggField = ChickenEntity.class.getDeclaredField("timeToLayEgg");
			timeToLayEggField.setAccessible(true);
			timeToLayEggField.setLong(chicken, timeToLayEgg);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		// Cancel further execution of the original method
		ci.cancel();
	}
}