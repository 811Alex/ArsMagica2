package am2.common.items;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

public class ItemArcaneGuardianSpellbook extends ItemSpellBook {

	public ItemArcaneGuardianSpellbook(){
		super();
	}

	@Override
	public int getItemEnchantability(){
		return 0;
	}

	@Override
	public boolean isBookEnchantable(ItemStack bookStack, ItemStack enchantBook){
		return false;
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack){
		return EnumRarity.EPIC;
	}
}
