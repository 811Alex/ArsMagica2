package am2.common.items;

import am2.common.lore.Story;
import am2.common.lore.StoryManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemLostJournal extends ItemWritableBook{

	public ItemLostJournal(){
		super();
		setMaxDamage(0);
		setMaxStackSize(1);
	}

	public Story getStory(ItemStack stack){
		if (stack.hasTagCompound()){
			NBTTagCompound compound = stack.getTagCompound();
			String nbtTitle = compound.getString("title");
			Story s = StoryManager.INSTANCE.getByTitle(nbtTitle);
			return s;
		}
		return null;
	}

	public short getStoryPart(ItemStack stack){
		if (stack.hasTagCompound()){
			NBTTagCompound compound = stack.getTagCompound();
			int part = compound.getInteger("story_part");
			return (short)part;
		}
		return -1;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}
	
	public ItemLostJournal registerAndName(String name) {
		this.setUnlocalizedName(new ResourceLocation("arsmagica2", name).toString());
		GameRegistry.register(this, new ResourceLocation("arsmagica2", name));
		return this;
	}
}