package am2.common.spell.component;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.api.affinity.Affinity;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.common.defs.ItemDefs;
import am2.common.extensions.EntityExtension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class Mark extends SpellComponent{

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		EntityExtension.For(caster).setMark(impactX, impactY, impactZ, caster.worldObj.provider.getDimension());
		if (caster instanceof EntityPlayer && world.isRemote){
			((EntityPlayer)caster).addChatMessage(new TextComponentString("Mark Set"));
		}
		return true;
	}

	@Override
	public boolean applyEffectEntity(SpellData spell, World world, EntityLivingBase caster, Entity target){
		if (!(target instanceof EntityLivingBase)){
			return false;
		}
		if (EntityExtension.For(caster).getMarkDimensionID() != -512){
			EntityExtension.For(caster).setMarkDimensionID(-512);;
			if (caster instanceof EntityPlayer && world.isRemote){
				((EntityPlayer)caster).addChatMessage(new TextComponentString("Mark Cleared"));
			}
		}else{
			EntityExtension.For(caster).setMark(target.posX, target.posY, target.posZ, caster.worldObj.provider.getDimension());
			if (caster instanceof EntityPlayer && world.isRemote){
				((EntityPlayer)caster).addChatMessage(new TextComponentString("Mark Set"));
			}
		}
		return true;
	}

	@Override
	public float manaCost(){
		return 5;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.noneOf(SpellModifiers.class);
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
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.RED.getDyeDamage()),
				new ItemStack(Items.MAP)
		};
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
