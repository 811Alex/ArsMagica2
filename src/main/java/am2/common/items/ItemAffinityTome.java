package am2.common.items;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.api.extensions.IAffinityData;
import am2.common.extensions.AffinityData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemAffinityTome extends ItemArsMagica {

	
	public ItemAffinityTome() {
		setHasSubtypes(true);
		setMaxDamage(0);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand){
		
		if (par2World.isRemote) return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer, hand);
		
		if (par1ItemStack.getItemDamage() == ArsMagicaAPI.getAffinityRegistry().getId(Affinity.NONE)){
			IAffinityData data = AffinityData.For(par3EntityPlayer);
			data.setLocked(false);
			for (Affinity aff : ArsMagicaAPI.getAffinityRegistry().getValues()){
				data.setAffinityDepth(aff, data.getAffinityDepth(aff) * AffinityData.MAX_DEPTH - 20);
			}
		}else{
			AffinityData.For(par3EntityPlayer).incrementAffinity(ArsMagicaAPI.getAffinityRegistry().getObjectById(par1ItemStack.getItemDamage()), 20);
		}
		par1ItemStack.stackSize--;

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, par1ItemStack);
	}
}
