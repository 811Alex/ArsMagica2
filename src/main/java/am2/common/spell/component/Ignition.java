package am2.common.spell.component;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.api.affinity.Affinity;
import am2.api.spell.Operation;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.common.defs.ItemDefs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
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

public class Ignition extends SpellComponent{

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){

		switch (blockFace){
		case EAST:
		case WEST:
		case SOUTH:
		case NORTH:
			pos = pos.offset(blockFace);
			break;
		case DOWN:
		case UP:
		default:
			break;
		}

		Block block = world.getBlockState(pos).getBlock();

		if (world.isAirBlock(pos) || block.equals(Blocks.SNOW) || block instanceof BlockFlower){
			if (!world.isRemote) world.setBlockState(pos, Blocks.FIRE.getDefaultState());
			return true;
		}else{
			pos = pos.up();
			block = world.getBlockState(pos).getBlock();
			if (world.isAirBlock(pos) || block.equals(Blocks.SNOW) || block instanceof BlockFlower){
				if (!world.isRemote) world.setBlockState(pos, Blocks.FIRE.getDefaultState());
				return true;
			}
		}
		return false;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.DURATION);
	}


	@Override
	public boolean applyEffectEntity(SpellData spell, World world, EntityLivingBase caster, Entity target){
		int burnTime = (int) spell.getModifiedValue(3, SpellModifiers.DURATION, Operation.MULTIPLY, world, caster, target);
		//burnTime = SpellUtils.modifyDurationBasedOnArmor(caster, burnTime);
		if (target.isBurning()){
			return false;
		}
		target.setFire(burnTime);
		return true;
	}

	@Override
	public float manaCost(){
		return 35;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

    @Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.FIRE);
	}
	
	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.ORANGE.getDyeDamage()),
				Items.FLINT_AND_STEEL
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}

}
