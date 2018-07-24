package am2.common.items;

import java.util.Map;

import am2.ArsMagica2;
import am2.common.container.InventorySpellBook;
import am2.common.defs.IDDefs;
import am2.common.defs.ItemDefs;
import am2.common.enchantments.AMEnchantmentHelper;
import am2.common.enchantments.AMEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;


public class ItemSpellBook extends ItemArsMagica{
	public static final byte ID_NEXT_SPELL = 0;
	public static final byte ID_PREV_SPELL = 1;
	
//	private final String[] npc_textureFiles = {"affinity_tome_general", "affinity_tome_ice", "affinity_tome_life", "affinity_tome_fire", "affinity_tome_lightning", "affinity_tome_ender"};
//	private final String[] player_textureFiles = {"spell_book_cover", "spell_book_decoration"};

	public ItemSpellBook(){
		super();
		this.setMaxDamage(0);
		this.setMaxStackSize(1);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemstack){
		if (getMaxItemUseDuration(itemstack) == 0){
			return EnumAction.NONE;
		}
		return EnumAction.BLOCK;
	}

	@Override
	public final int getMaxItemUseDuration(ItemStack itemstack){
		ItemSpellBase scroll = GetActiveScroll(itemstack);
		if (scroll != null){
			return scroll.getMaxItemUseDuration(itemstack);
		}
		return 0;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (playerIn.isSneaking()){
			FMLNetworkHandler.openGui(playerIn, ArsMagica2.instance, IDDefs.GUI_SPELL_BOOK, worldIn, (int)playerIn.posX, (int)playerIn.posY, (int)playerIn.posZ);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
		}

		playerIn.setActiveHand(hand);

		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}
	
	private ItemStack[] getMyInventory(ItemStack itemStack){
		return ReadFromStackTagCompound(itemStack);
	}

	public ItemStack[] getActiveScrollInventory(ItemStack bookStack){
		ItemStack[] inventoryItems = getMyInventory(bookStack);
		ItemStack[] returnArray = new ItemStack[8];
		for (int i = 0; i < 8; ++i){
			returnArray[i] = inventoryItems[i];
		}
		return returnArray;
	}

	public ItemSpellBase GetActiveScroll(ItemStack bookStack){
		ItemStack[] inventoryItems = getMyInventory(bookStack);
		if (inventoryItems[GetActiveSlot(bookStack)] == null){
			return null;
		}
		return (ItemSpellBase)inventoryItems[GetActiveSlot(bookStack)].getItem();
	}

	public ItemStack GetActiveItemStack(ItemStack bookStack){
		ItemStack[] inventoryItems = getMyInventory(bookStack);
		if (inventoryItems[GetActiveSlot(bookStack)] == null){
			return null;
		}
		return inventoryItems[GetActiveSlot(bookStack)].copy();
	}

	public void replaceActiveItemStack(ItemStack bookStack, ItemStack newstack){
		ItemStack[] inventoryItems = getMyInventory(bookStack);
		int index = GetActiveSlot(bookStack);
		inventoryItems[index] = newstack;
		UpdateStackTagCompound(bookStack, inventoryItems);
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving.isSneaking()){
			FMLNetworkHandler.openGui((EntityPlayer) entityLiving, ArsMagica2.instance, IDDefs.GUI_SPELL_BOOK, worldIn, (int)entityLiving.posX, (int)entityLiving.posY, (int)entityLiving.posZ);
		}else{
			ItemStack currentSpellStack = GetActiveItemStack(stack);
			if (currentSpellStack != null){
				ItemDefs.spell.onPlayerStoppedUsing(currentSpellStack, worldIn, entityLiving, timeLeft);
			}
		}
	}

	public void UpdateStackTagCompound(ItemStack itemStack, ItemStack[] values){
		if (itemStack.getTagCompound() == null){
			itemStack.setTagCompound(new NBTTagCompound());
		}

		NBTTagList list = new NBTTagList();
		for (int i = 0; i < values.length; ++i){
			ItemStack stack = values[i];
			NBTTagCompound tag = new NBTTagCompound();
			if (stack != null) {
				tag.setShort("Slot", (short)i);
				stack.writeToNBT(tag);
				list.appendTag(tag);
			}
		}
		itemStack.getTagCompound().setTag("spell_book_inventory", list);

		ItemStack active = GetActiveItemStack(itemStack);
		boolean Soulbound = EnchantmentHelper.getEnchantmentLevel(AMEnchantments.soulbound, itemStack) > 0;
		if (active != null)
			AMEnchantmentHelper.copyEnchantments(active, itemStack);
		if (Soulbound)
			AMEnchantmentHelper.soulbindStack(itemStack);
	}

	public void SetActiveSlot(ItemStack itemStack, int slot){
		if (itemStack.getTagCompound() == null){
			itemStack.setTagCompound(new NBTTagCompound());
		}
		if (slot < 0) slot = 0;
		if (slot > 7) slot = 7;
		itemStack.getTagCompound().setInteger("spellbookactiveslot", slot);

		ItemStack active = GetActiveItemStack(itemStack);
		boolean Soulbound = EnchantmentHelper.getEnchantmentLevel(AMEnchantments.soulbound, itemStack) > 0;
		if (active != null)
			AMEnchantmentHelper.copyEnchantments(active, itemStack);
		if (Soulbound)
			AMEnchantmentHelper.soulbindStack(itemStack);
	}

	public int SetNextSlot(ItemStack itemStack){
		int slot = GetActiveSlot(itemStack);
		int newSlot = slot;

		do{
			newSlot++;
			if (newSlot > 7) newSlot = 0;
			SetActiveSlot(itemStack, newSlot);
		}while (GetActiveScroll(itemStack) == null && newSlot != slot);
		return slot;
	}

	public int SetPrevSlot(ItemStack itemStack){
		int slot = GetActiveSlot(itemStack);
		int newSlot = slot;

		do{
			newSlot--;
			if (newSlot < 0) newSlot = 7;
			SetActiveSlot(itemStack, newSlot);
		}while (GetActiveScroll(itemStack) == null && newSlot != slot);
		return slot;
	}

	public int GetActiveSlot(ItemStack itemStack){
		if (itemStack.getTagCompound() == null){
			SetActiveSlot(itemStack, 0);
			return 0;
		}
		return itemStack.getTagCompound().getInteger("spellbookactiveslot");
	}

	public ItemStack[] ReadFromStackTagCompound(ItemStack itemStack){
		if (itemStack.getTagCompound() == null){
			return new ItemStack[InventorySpellBook.inventorySize];
		}
		ItemStack[] items = new ItemStack[InventorySpellBook.inventorySize];
		NBTTagList list = itemStack.getTagCompound().getTagList("spell_book_inventory", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); ++i){
			NBTTagCompound spell = list.getCompoundTagAt(i);
			short slot = spell.getShort("Slot");
			ItemStack is = ItemStack.loadItemStackFromNBT(spell);
			if (is != null)
				items[slot] = is;
		}
		return items;
	}

	public InventorySpellBook ConvertToInventory(ItemStack bookStack){
		InventorySpellBook isb = new InventorySpellBook();
		isb.SetInventoryContents(getMyInventory(bookStack));
		return isb;
	}

	@Override
	public boolean getShareTag(){
		return true;
	}

	public String GetActiveSpellName(ItemStack bookStack){
		ItemStack stack = GetActiveItemStack(bookStack);
		if (stack == null){
			return I18n.format("am2.tooltip.none");
		}
		return stack.getDisplayName();
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count){
		ItemStack scrollStack = GetActiveItemStack(stack);
		if (scrollStack != null){
			ItemDefs.spell.onUsingTick(scrollStack, player, count);
		}
	}

	@Override
	public boolean isBookEnchantable(ItemStack bookStack, ItemStack enchantBook){
		Map<Enchantment, Integer> enchantMap = EnchantmentHelper.getEnchantments(enchantBook);
		for (Enchantment o : enchantMap.keySet()){
			if (o == AMEnchantments.soulbound){
				return true;
			}
		}
		return false;
	}

	@Override
	public int getItemEnchantability(){
		return 1;
	}

	@Override
	public boolean isItemTool(ItemStack par1ItemStack){
		return true;
	}
}









