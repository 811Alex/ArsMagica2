package am2.common.items;

import am2.common.blocks.BlockArsMagicaBlock.EnumBlockType;
import am2.common.blocks.BlockArsMagicaOre.EnumOreType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.resources.I18n;

public class ItemBlockOreBlock extends ItemBlockSubtypes {

	public ItemBlockOreBlock(Block block) {
		super(block);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.format("tile.arsmagica2:block_" + EnumBlockType.values()[MathHelper.clamp_int(stack.getItemDamage(), 0, EnumOreType.values().length - 1)].getName().toLowerCase() + ".name");
	}
}
