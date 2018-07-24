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
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Drought extends SpellComponent{

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){


		Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlockFlower || block instanceof BlockTallGrass){
			world.setBlockState(pos, Blocks.DEADBUSH.getDefaultState());
			return true;
		}else if (block == Blocks.GRASS || block == Blocks.MYCELIUM || block == Blocks.SANDSTONE || block == Blocks.DIRT){
			world.setBlockState(pos, Blocks.SAND.getDefaultState());
			return true;
		}else if (block == Blocks.STONE){
			world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
			return true;
		}else if (block == Blocks.STONEBRICK){
			world.setBlockState(pos, Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED));
			return true;
		}

		if (block == Blocks.WATER || block == Blocks.FLOWING_WATER){
			world.setBlockToAir(pos);
			return true;
		}else{
			pos = pos.offset(blockFace);
			block = world.getBlockState(pos).getBlock();
			if (block == Blocks.WATER || block == Blocks.FLOWING_WATER){
				world.setBlockToAir(pos);
				return true;
			}
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
		return 60;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

    @Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.FIRE, Affinity.AIR);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.ORANGE.getDyeDamage()),
				Blocks.SAND,
				Blocks.DEADBUSH
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		if (affinity == Affinity.FIRE)
			return 0.008f;
		else
			return 0.004f;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}

}
