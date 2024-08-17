package net.fabricmc.example.mixin;

import btw.block.blocks.GrassBlock;
import btw.community.example.FASTDAddon;
import btw.item.util.ItemUtils;
import btw.item.BTWItems;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrassBlock.class)
public class HempSeedMixin {

	@Inject(method = "convertBlock", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", ordinal = 0), cancellable = true)
	private void injectChangeProbability(ItemStack stack, World world, int x, int y, int z, int fromSide, CallbackInfoReturnable<Boolean> cir) {
		if (!world.isRemote) {
			if (world.rand.nextInt(FASTDAddon.getHempSeedChance()) == 0) {
				ItemUtils.ejectStackFromBlockTowardsFacing(world, x, y, z, new ItemStack(BTWItems.hempSeeds), fromSide);
			}
			if (world.rand.nextInt(FASTDAddon.getWheatSeedChance()) == 1) {
				ItemUtils.ejectStackFromBlockTowardsFacing(world, x, y, z, new ItemStack(BTWItems.wheatSeeds), fromSide);

			}
		}
		cir.setReturnValue(true);
	}
}