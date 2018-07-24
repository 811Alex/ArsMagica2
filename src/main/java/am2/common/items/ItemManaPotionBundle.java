package am2.common.items;

import am2.common.defs.ItemDefs;
import am2.common.extensions.EntityExtension;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemManaPotionBundle extends ItemArsMagica{
	public ItemManaPotionBundle(){
		super();
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
	}

	private Item getPotion(int damage){
		int id = damage >> 8;
		switch (id){
		case 0:
			return ItemDefs.lesserManaPotion;
		case 1:
			return ItemDefs.standardManaPotion;
		case 2:
			return ItemDefs.greaterManaPotion;
		case 3:
			return ItemDefs.epicManaPotion;
		case 4:
			return ItemDefs.legendaryManaPotion;
		}
		return ItemDefs.lesserManaPotion;
	}

	private int getUses(int damage){
		return (damage & 0x0F);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack){
		return 32;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack){
		return EnumAction.DRINK;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, EnumHand hand){
		EntityExtension props = EntityExtension.For(par3EntityPlayer);
		if (props.getCurrentMana() < props.getMaxMana()){
			par3EntityPlayer.setActiveHand(hand);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, par1ItemStack);
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, par1ItemStack);
	}


	@Override
	public ItemStack onItemUseFinish(ItemStack par1ItemStack, World par2World, EntityLivingBase ent){
		if (!(ent instanceof EntityPlayer)) return super.onItemUseFinish(par1ItemStack, par2World, ent);
		EntityPlayer par3EntityPlayer = (EntityPlayer)ent;
		Item potion = getPotion(par1ItemStack.getItemDamage());
		if (potion == ItemDefs.lesserManaPotion){
			ItemDefs.lesserManaPotion.onItemUseFinish(par1ItemStack, par2World, par3EntityPlayer);
		}else if (potion == ItemDefs.standardManaPotion){
			ItemDefs.standardManaPotion.onItemUseFinish(par1ItemStack, par2World, par3EntityPlayer);
		}else if (potion == ItemDefs.greaterManaPotion){
			ItemDefs.greaterManaPotion.onItemUseFinish(par1ItemStack, par2World, par3EntityPlayer);
		}else if (potion == ItemDefs.epicManaPotion){
			ItemDefs.epicManaPotion.onItemUseFinish(par1ItemStack, par2World, par3EntityPlayer);
		}else if (potion == ItemDefs.legendaryManaPotion){
			ItemDefs.legendaryManaPotion.onItemUseFinish(par1ItemStack, par2World, par3EntityPlayer);
		}

		par1ItemStack.setItemDamage(((par1ItemStack.getItemDamage() >> 8) << 8) + getUses(par1ItemStack.getItemDamage()) - 1);

		if (getUses(par1ItemStack.getItemDamage()) == 0){
			giveOrDropItem(par3EntityPlayer, new ItemStack(Items.STRING));
			if (par1ItemStack.stackSize-- == 0)
				par1ItemStack = null;
		}

		giveOrDropItem(par3EntityPlayer, new ItemStack(Items.GLASS_BOTTLE));

		return par1ItemStack;
	}

	private void giveOrDropItem(EntityPlayer player, ItemStack stack){
		if (!player.inventory.addItemStackToInventory(stack))
			player.dropItem(stack, true);
	}

	@Override
	public boolean getHasSubtypes(){
		return true;
	}

}
