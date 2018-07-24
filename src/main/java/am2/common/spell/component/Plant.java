package am2.common.spell.component;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.api.affinity.Affinity;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.common.defs.ItemDefs;
import am2.common.utils.DummyEntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

public class Plant extends SpellComponent{

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		Block soil = world.getBlockState(pos).getBlock();
		IInventory inventory = DummyEntityPlayer.fromEntityLiving(caster).inventory;
		HashMap<Integer, ItemStack> seeds = GetAllSeedsInInventory(inventory);
		int currentSlot = 0;
		if (soil != Blocks.AIR && seeds.size() > 0){
			currentSlot = seeds.keySet().iterator().next();
			ItemStack seedStack = seeds.get(currentSlot);

			IPlantable seed = (IPlantable)seedStack.getItem();

			if (soil != null && soil.canSustainPlant(world.getBlockState(pos), world, pos, EnumFacing.UP, seed) && world.isAirBlock(pos.up())){
				world.setBlockState(pos.up(), seed.getPlant(world, pos));

				seedStack.stackSize--;
				if (seedStack.stackSize <= 0){
					inventory.setInventorySlotContents(currentSlot, null);
					seeds.remove(currentSlot);
					if (seeds.size() == 0) return true;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.noneOf(SpellModifiers.class);
	}
	
	@Override
	public boolean applyEffectEntity(SpellData spell, World world, EntityLivingBase caster, Entity target){
		return false;
	}

	@Override
	public float manaCost(){
		return 80;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

    @Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.NATURE);
	}

	private HashMap<Integer, ItemStack> GetAllSeedsInInventory(IInventory inventory){
		HashMap<Integer, ItemStack> seeds = new HashMap<Integer, ItemStack>();
		for (int i = 0; i < inventory.getSizeInventory(); ++i){
			ItemStack slotStack = inventory.getStackInSlot(i);
			if (slotStack == null) continue;
			Item item = slotStack.getItem();
			if (!(item instanceof IPlantable)) continue;
			seeds.put(i, slotStack);
		}

		return seeds;
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.GREEN.getDyeDamage()),
				Items.WHEAT_SEEDS,
				new ItemStack(Blocks.SAPLING, 1, OreDictionary.WILDCARD_VALUE),
				Items.WHEAT_SEEDS
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}
	
	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {}
}
