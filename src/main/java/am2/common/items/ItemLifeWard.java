package am2.common.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLifeWard extends ItemArsMagica{

	public ItemLifeWard(){
		super();
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5){
		if (par4 < 9 && par3Entity.ticksExisted % 80 == 0 && par3Entity instanceof EntityLivingBase){
			float abs = ((EntityLivingBase)par3Entity).getAbsorptionAmount();
			if (abs < 20){
				abs++;
				((EntityLivingBase)par3Entity).setAbsorptionAmount(abs);
			}
		}
	}
}
