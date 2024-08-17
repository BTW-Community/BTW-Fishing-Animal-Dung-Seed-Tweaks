package net.fabricmc.example.mixin;


import btw.community.example.FASTDAddon;
import net.minecraft.src.EntityFishHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityFishHook.class)
public class FishHookMixin {

	@ModifyVariable(
			method = "checkForBite",
			at = @At(value = "STORE", ordinal = 0),
			index = 1
	)
	private int modifyBiteOdds(int iBiteOdds) {
		return FASTDAddon.getFishTimer();
	}
}
