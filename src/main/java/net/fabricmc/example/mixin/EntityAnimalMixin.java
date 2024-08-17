package net.fabricmc.example.mixin;

import btw.community.example.FASTDAddon;
import btw.item.BTWItems;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAnimal.class)
public abstract class EntityAnimalMixin extends EntityLiving {

	public EntityAnimalMixin(World world) {
		super(world);
	}

	@Inject(method = "updateHungerState", at = @At("HEAD"))
	public void injectUpdateHungerState(CallbackInfo info) {
		if (this.getClass().getSimpleName().equals("PigEntity")) {
			updateShitState();
		}
	}
	public void updateShitState() {
		if (isFullyFed()) {
			int chanceOfShitting = 1;

			if (isDarkEnoughToAffectShitting()) {
				chanceOfShitting *= 2;
			}

			if (worldObj.rand.nextInt(FASTDAddon.getPigDungTime()) < chanceOfShitting) {
				attemptToShit();
			}
		}
	}

	public boolean isFullyFed() {
		return ((EntityAnimal) (Object) this).getHungerLevel() == 0;
	}

	public boolean isDarkEnoughToAffectShitting() {
		int i = MathHelper.floor_double(posX);
		int j = MathHelper.floor_double(posY);
		int k = MathHelper.floor_double(posZ);

		int lightValue = worldObj.getBlockLightValue(i, j, k);

		return lightValue <= 5;
	}

	public boolean attemptToShit() {
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

	public boolean isPathToBlockOpenToShitting(int i, int j, int k) {
		if (!isBlockOpenToShitting(i, j, k)) {
			return false;
		}

		int pigI = MathHelper.floor_double(posX);
		int pigK = MathHelper.floor_double(posZ);

		int deltaI = i - pigI;
		int deltaK = k - pigK;

		if (deltaI != 0 && deltaK != 0) {
			if (!isBlockOpenToShitting(pigI, j, k) && !isBlockOpenToShitting(i, j, pigK)) {
				return false;
			}
		}

		return true;
	}

	public boolean isBlockOpenToShitting(int i, int j, int k) {
		int blockId = worldObj.getBlockId(i, j, k);
		Block block = Block.blocksList[blockId];

		if (block != null && (block == Block.waterMoving || block == Block.waterStill || block == Block.lavaMoving || block == Block.lavaStill || block == Block.fire || block.blockMaterial.isReplaceable())) {
			block = null;
		}

		return block == null;
	}
}