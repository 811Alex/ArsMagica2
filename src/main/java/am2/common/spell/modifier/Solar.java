package am2.common.spell.modifier;

import java.util.EnumSet;

import am2.api.affinity.Affinity;
import am2.api.spell.SpellModifier;
import am2.api.spell.SpellModifiers;
import am2.common.defs.ItemDefs;
import am2.common.items.ItemOre;
import am2.common.utils.AffinityShiftUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Solar extends SpellModifier{

	@Override
	public EnumSet<SpellModifiers> getAspectsModified(){
		return EnumSet.of(SpellModifiers.RANGE, SpellModifiers.RADIUS, SpellModifiers.DAMAGE, SpellModifiers.DURATION, SpellModifiers.HEALING);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public float getModifier(SpellModifiers type, EntityLivingBase caster, Entity target, World world, NBTTagCompound metadata){
		switch (type){
		case RANGE:
			return modifyValueOnInverseLunarCycle(world, 3f);
		case RADIUS:
			return modifyValueOnInverseLunarCycle(world, 3f);
		case DAMAGE:
			return modifyValueOnTime(world, 2.4f);
		case DURATION:
			return modifyValueOnTime(world, 5f);
		case HEALING:
			return modifyValueOnTime(world, 2f);
		}
		return 1.0f;
	}

	private float modifyValueOnTime(World world, float value){
		long x = world.provider.getWorldTime() % 24000;
		float multiplierFromTime = (float)(Math.cos(((x / 3800f) * (x / 24000f) - 13000f) * (180f / Math.PI)) * 1.5f) + 1;
		if (multiplierFromTime < 0)
			multiplierFromTime *= -0.5f;
		return value * multiplierFromTime;
	}

	private float modifyValueOnInverseLunarCycle(World world, float value){
		long boundedTime = world.provider.getWorldTime() % 24000;
		int phase = 8 - (8 - world.provider.getMoonPhase(world.getWorldInfo().getWorldTime()));
		if (boundedTime > 23500 && boundedTime < 12500){
			return value + (phase / 2);
		}
		return Math.abs(value - 1);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				AffinityShiftUtils.getEssenceForAffinity(Affinity.NATURE),
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_SUNSTONE),
				Items.CLOCK
		};
	}

	@Override
	public float getManaCostMultiplier() {
		return 4.0f;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}
}
