package am2.common.spell.component;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.api.affinity.Affinity;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.common.defs.ItemDefs;
import am2.common.defs.PotionEffectsDefs;
import am2.common.utils.AffinityShiftUtils;
import am2.common.utils.DimensionUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class EnderIntervention extends SpellComponent{

	@Override
	public boolean applyEffectEntity(SpellData spell, World world, EntityLivingBase caster, Entity target){
		if (world.isRemote || !(target instanceof EntityLivingBase)) return true;

		if (((EntityLivingBase)target).isPotionActive(PotionEffectsDefs.ASTRAL_DISTORTION)){
			if (target instanceof EntityPlayer)
				((EntityPlayer)target).addChatMessage(new TextComponentString("The distortion around you prevents you from teleporting"));
			return true;
		}

		if (target.dimension == 1){
			if (target instanceof EntityPlayer)
				((EntityPlayer)target).addChatMessage(new TextComponentString("Nothing happens..."));
			return true;
		}else if (target.dimension == -1){
			if (target instanceof EntityPlayer)
				((EntityPlayer)target).addChatMessage(new TextComponentString("You are already in the nether."));
			return false;
		}else{
			DimensionUtilities.doDimensionTransfer((EntityLivingBase)target, -1);
			ArsMagica2.proxy.addDeferredDimensionTransfer((EntityLivingBase)target, -1);
		}

		return true;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.noneOf(SpellModifiers.class);
	}
	
	@Override
	public float manaCost(){
		return 400;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return new ItemStack[]{AffinityShiftUtils.getEssenceForAffinity(Affinity.ENDER)};
	}

    @Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.ENDER);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.PURPLE.getDyeDamage()),
				Blocks.OBSIDIAN,
				Blocks.OBSIDIAN,
				Items.FLINT_AND_STEEL,
				Items.ENDER_PEARL
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.4f;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos blockPos, EnumFacing blockFace,
			double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		// TODO Auto-generated method stub
		return false;
	}
}
