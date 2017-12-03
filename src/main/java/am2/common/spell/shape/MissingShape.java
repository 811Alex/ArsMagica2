package am2.common.spell.shape;

import java.util.EnumSet;

import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.api.spell.SpellShape;
import am2.common.spell.SpellCastResult;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class MissingShape extends SpellShape {

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isChanneled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float manaCostMultiplier() {
		// TODO Auto-generated method stub
		return 0F;
	}

	@Override
	public boolean isTerminusShape() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrincipumShape() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SpellCastResult beginStackStage(SpellData spell, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, EnumFacing side, boolean giveXP, int useCount) {
		// TODO Auto-generated method stub
		return SpellCastResult.MALFORMED_SPELL_STACK;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.noneOf(SpellModifiers.class);
	}


	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}

}
