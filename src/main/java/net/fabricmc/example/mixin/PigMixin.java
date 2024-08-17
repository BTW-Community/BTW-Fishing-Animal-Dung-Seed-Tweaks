package net.fabricmc.example.mixin;

import btw.entity.mob.PigEntity;
import btw.item.BTWItems;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PigEntity.class)
public class PigMixin {

	@Inject(method = "isBreedingItem", at = @At("TAIL"), cancellable = true)
	private void injectIsBreedingItem(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
		if (itemStack !=null && (itemStack.itemID == BTWItems.chocolate.itemID || itemStack.itemID == Item.fishRaw.itemID || itemStack.itemID == Item.fishCooked.itemID)) {
			cir.setReturnValue(true);
		}
	}
	@Inject(method = "getItemFoodValue", at = @At("HEAD"), cancellable = true)
	private void injectGetItemFoodValue(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (stack.itemID == Item.fishRaw.itemID || stack.itemID == Item.fishCooked.itemID) {
			cir.setReturnValue(6400);
		}
	}
}