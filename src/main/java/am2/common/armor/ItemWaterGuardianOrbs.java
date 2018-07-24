package am2.common.armor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemWaterGuardianOrbs extends AMArmor{

	public ItemWaterGuardianOrbs(ArmorMaterial inheritFrom, ArsMagicaArmorMaterial enumarmormaterial, int par3, EntityEquipmentSlot par4){
		super(inheritFrom, enumarmormaterial, par3, par4);
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot){
		return 0;
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type){
		return "arsmagica2:textures/mobs/bosses/water_guardian.png";
	}

	@Override
	public void onArmorTick(World world, EntityPlayer plr, ItemStack stack) {
		super.onArmorTick(world, plr, stack);
		Potion waterBreathing = Potion.getPotionFromResourceLocation(new ResourceLocation("water_breathing").toString());
		if (!plr.isPotionActive(waterBreathing))
			plr.addPotionEffect(new PotionEffect(waterBreathing));

		if (plr.isInWater());
			reverseMaterialAcceleration(world, plr);
	}

	public void reverseMaterialAcceleration(World world, EntityPlayer entityIn)
	{
		AxisAlignedBB bb = entityIn.getEntityBoundingBox();

		int minX = MathHelper.floor_double(bb.minX);
		int maxX = MathHelper.ceiling_double_int(bb.maxX);
		int minY = MathHelper.floor_double(bb.minY);
		int maxY = MathHelper.ceiling_double_int(bb.maxY);
		int minZ = MathHelper.floor_double(bb.minZ);
		int maxZ = MathHelper.ceiling_double_int(bb.maxZ);

		Vec3d vec3d = Vec3d.ZERO;
		BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();

		for (int k1 = minX; k1 < maxX; ++k1)
			for (int l1 = minY; l1 < maxY; ++l1)
				for (int i2 = minZ; i2 < maxZ; ++i2)
				{
					blockpos$pooledmutableblockpos.setPos(k1, l1, i2);
					IBlockState iblockstate = world.getBlockState(blockpos$pooledmutableblockpos);
					Block block = iblockstate.getBlock();

					Boolean result = block.isEntityInsideMaterial(world, blockpos$pooledmutableblockpos, iblockstate, entityIn, (double)maxY, Material.WATER, false);
					if (result != null && result == true)
					{
						vec3d = block.modifyAcceleration(world, blockpos$pooledmutableblockpos, entityIn, vec3d);
						continue;
					}
					else if (result != null && result == false) continue;
					if (iblockstate.getMaterial() == Material.WATER)
					{
						double d0 = (double)((float)(l1 + 1) - BlockLiquid.getLiquidHeightPercent(((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue()));
						if ((double)maxY >= d0)
							vec3d = block.modifyAcceleration(world, blockpos$pooledmutableblockpos, entityIn, vec3d);
					}
				}
		blockpos$pooledmutableblockpos.release();
		if (vec3d.lengthVector() > 0.0D && entityIn.isPushedByWater())
		{
			vec3d = vec3d.normalize();
			//double d1 = 0.014D;
			entityIn.motionX -= vec3d.xCoord * 0.014D;
			entityIn.motionY -= vec3d.yCoord * 0.014D;
			entityIn.motionZ -= vec3d.zCoord * 0.014D;
		}

	}
}
