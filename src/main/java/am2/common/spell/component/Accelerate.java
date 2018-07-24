package am2.common.spell.component;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.api.affinity.Affinity;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.common.defs.ItemDefs;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Accelerate extends SpellComponent{

	@Override
	public boolean applyEffectEntity(SpellData spell, World world, EntityLivingBase caster, Entity target) {
		caster.setAIMoveSpeed(caster.getAIMoveSpeed() * 1.6f);
		return true;
	}

	@Override
	public float manaCost(){
		return 6;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

    @Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.YELLOW.getDyeDamage()),
				Items.LEATHER_BOOTS,
				Items.REDSTONE
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		if (affinity.equals(Affinity.AIR))
			return 1F;
		return 0;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos blockPos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		if (world.rand.nextDouble() < 0.5){
			Block block = world.getBlockState(blockPos).getBlock();
			if (block != Blocks.AIR){
				block.updateTick(world, blockPos, world.getBlockState(blockPos), world.rand);
			}
		}
		return true;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.noneOf(SpellModifiers.class);
	}


	@Override
	public Set<Affinity> getAffinity() {
		return Sets.newHashSet(Affinity.AIR);
	}

}
