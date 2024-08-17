package net.fabricmc.example.mixin;

import btw.entity.mob.ChickenEntity;
import btw.item.BTWItems;
import btw.world.util.WorldUtils;
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

	@Inject(method = "onEatBreedingItem", at = @At("HEAD"), cancellable = true)
	private void modifyOnEatBreedingItem(CallbackInfo ci) {
		ChickenEntity chicken = (ChickenEntity) (Object) this;
		long lCurrentTime = WorldUtils.getOverworldTimeServerOnly();

		// Calculate the current time of day
		long timeOfDay = lCurrentTime % 24000L;

		// Calculate the time for the next morning (0 ticks)
		long nextMorningTime = (lCurrentTime - timeOfDay) + 24000L;

		// Set timeToLayEgg to the next morning with a small random variance
		long timeToLayEgg = nextMorningTime + (long)(1200 + chicken.rand.nextInt(600));

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

		try {
			Field timeToLayEggField = ChickenEntity.class.getDeclaredField("timeToLayEgg");
			timeToLayEggField.setAccessible(true);
			timeToLayEggField.setLong(chicken, timeToLayEgg);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

		ci.cancel();
	}
}