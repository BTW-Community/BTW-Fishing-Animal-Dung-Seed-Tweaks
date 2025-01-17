package net.fabricmc.example.mixin;

import btw.entity.mob.SheepEntity;
import btw.item.BTWItems;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepEntity.class)
public abstract class SheepMixin extends EntitySheep {

	public SheepMixin(World world) {
		super(world);
	}

	@Inject(method = "updateHungerState", at = @At("HEAD"))
	private void injectUpdateHungerState(CallbackInfo info) {
		updateShitState();
	}

	@Inject(method = "isBreedingItem", at = @At("TAIL"), cancellable = true)
	private void injectIsBreedingItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack != null && (stack.itemID == Item.pumpkinPie.itemID || stack.itemID == BTWItems.wheat.itemID)) {
			cir.setReturnValue(true);
		}
	}

	private void updateShitState() {
		if (isFullyFed()) {
			int chanceOfShitting = 1;

			if (isDarkEnoughToAffectShitting()) {
				chanceOfShitting *= 2;
			}

			if (worldObj.rand.nextInt(10800) < chanceOfShitting) {
				attemptToShit();
			}
		}
	}

	private boolean isDarkEnoughToAffectShitting() {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);

		int lightValue = worldObj.getBlockLightValue(i, j, k);

		return lightValue <= 5;
	}

	private boolean attemptToShit() {
		float poopVectorX;
		poopVectorX = MathHelper.sin((rotationYawHead / 180F) * (float) Math.PI);
		float poopVectorZ;
		poopVectorZ = -MathHelper.cos((rotationYawHead / 180F) * (float) Math.PI);

		double shitPosX = posX + poopVectorX;
		double shitPosY = posY + 0.25D;
		double shitPosZ = posZ + poopVectorZ;

		int shitPosI = MathHelper.floor_double(shitPosX);
		int shitPosJ = MathHelper.floor_double(shitPosY);
		int shitPosK = MathHelper.floor_double(shitPosZ);

		if (!isPathToBlockOpenToShitting(shitPosI, shitPosJ, shitPosK)) {
			return false;
		}

		EntityItem entityitem = new EntityItem(worldObj, shitPosX, shitPosY, shitPosZ, new ItemStack(BTWItems.dung));

		float velocityFactor = 0.01F;

		entityitem.motionX = poopVectorX * 5.0f * velocityFactor;
		entityitem.motionZ = poopVectorZ * 5.0f * velocityFactor;
		entityitem.motionY = (float) worldObj.rand.nextGaussian() * velocityFactor + 0.2F;

		entityitem.delayBeforeCanPickup = 10;

		worldObj.spawnEntityInWorld(entityitem);

		worldObj.playSoundAtEntity(this, "random.explode", 0.2F, 1.25F);

		for (int counter = 0; counter < 5; counter++) {
			double smokeX = posX + (poopVectorX * 0.5f) + (worldObj.rand.nextDouble() * 0.25F);
			double smokeY = posY + worldObj.rand.nextDouble() * 0.5F + 0.25F;
			double smokeZ = posZ + (poopVectorZ * 0.5f) + (worldObj.rand.nextDouble() * 0.25F);

			worldObj.spawnParticle("smoke", smokeX, smokeY, smokeZ, 0.0D, 0.0D, 0.0D);
		}

		return true;
	}

	private boolean isPathToBlockOpenToShitting(int i, int j, int k) {
		if (!isBlockOpenToShitting(i, j, k)) {
			return false;
		}

		int sheepI = MathHelper.floor_double(posX);
		int sheepK = MathHelper.floor_double(posZ);

		int deltaI = i - sheepI;
		int deltaK = k - sheepK;

		if (deltaI != 0 && deltaK != 0) {
			if (!isBlockOpenToShitting(sheepI, j, k) && !isBlockOpenToShitting(i, j, sheepK)) {
				return false;
			}
		}

		return true;
	}

	private boolean isBlockOpenToShitting(int i, int j, int k) {
		int blockId = worldObj.getBlockId(i, j, k);
		Block block = Block.blocksList[blockId];

		if (block != null && (block == Block.waterMoving || block == Block.waterStill || block == Block.lavaMoving || block == Block.lavaStill || block == Block.fire || block.blockMaterial.isReplaceable())) {
			block = null;
		}

		if (block != null) {
			return false;
		}

		return true;
	}
}