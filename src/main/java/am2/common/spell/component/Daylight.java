package am2.common.spell.component;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.api.affinity.Affinity;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.common.defs.ItemDefs;
import am2.common.items.ItemCore;
import am2.common.items.ItemOre;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class Daylight extends SpellComponent{

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_SUNSTONE),
				Items.CLOCK,
				new ItemStack(ItemDefs.core, 1, ItemCore.META_PURE)
		};
	}

	private boolean setDayTime(World world){
		if (world.isDaytime())
			return false;
		if (!world.isRemote){
			long curTime = ((WorldServer)world).getWorldTime();
			int day = (int)Math.ceil((curTime / 24000));
			((WorldServer)world).setWorldTime(day * 24000);
		}
		return true;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.noneOf(SpellModifiers.class);
	}

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		return setDayTime(world);
	}

	@Override
	public boolean applyEffectEntity(SpellData spell, World world, EntityLivingBase caster, Entity target){
		return setDayTime(world);
	}

	@Override
	public float manaCost(){
		return 25000;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

    @Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.NONE);
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}

}
