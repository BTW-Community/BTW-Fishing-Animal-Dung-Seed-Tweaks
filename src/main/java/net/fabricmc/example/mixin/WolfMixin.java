package net.fabricmc.example.mixin;

import btw.client.fx.BTWEffectManager;
import btw.entity.mob.WolfEntity;
import btw.item.BTWItems;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(WolfEntity.class)
public abstract class WolfMixin {

	@Inject(method = "isBreedingItem", at = @At("TAIL"), cancellable = true)
	private void injectIsBreedingItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack !=null && (stack.itemID == BTWItems.rawMysteryMeat.itemID || stack.itemID == BTWItems.cookedMysteryMeat.itemID || stack.itemID == Item.fishRaw.itemID || stack.itemID == Item.fishCooked.itemID)) {
			cir.setReturnValue(true);
		}
	}
	@Inject(method = "isEdibleItem", at = @At("HEAD"), cancellable = true)
	private void isEdibleItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getItem() == Item.fishRaw || stack.getItem() == Item.fishCooked) {
			// Set the return value to true if the item is fishRaw
			cir.setReturnValue(true);
		}
	}
	@Inject(method = "onEat", at = @At("HEAD"), cancellable = true)
	public void onEat(Item food, CallbackInfo ci) {
		WolfEntity wolf = (WolfEntity) (Object) this;
		World world = wolf.worldObj;

		// Custom behavior for FishRaw
		if (food.itemID == BTWItems.rawMysteryMeat.itemID || food.itemID == BTWItems.cookedMysteryMeat.itemID || food.itemID == Item.fishRaw.itemID || food.itemID == Item.fishCooked.itemID) {

			wolf.heal(food.getWolfHealAmount());

			int iHungerLevel = wolf.getHungerLevel();
			if (iHungerLevel > 0) {
				wolf.setHungerLevel(iHungerLevel - 1);
			}

			wolf.resetHungerCountdown();

			if (!world.isRemote) {
				world.playAuxSFX(BTWEffectManager.WOLF_EATING_EFFECT_ID,
						MathHelper.floor_double(wolf.posX), (int) (wolf.posY + wolf.height),
						MathHelper.floor_double(wolf.posZ), 0);
			}

			// Trigger breeding behavior
			wolf.onEatBreedingItem();

			// Cancel the rest of the method execution
			ci.cancel();
		}
	}
	@ModifyConstant(method = "attemptToShit", constant = @Constant(floatValue = 0.05F), remap = false)
	private float modifyVelocityFactor(float original) {
		return 0.005F;
	}

	@ModifyConstant(method = "updateShitState", constant = @Constant(intValue = 24000), remap = false)
	private int modifyShitStateInterval(int original) {
		return 4800;
	}
}