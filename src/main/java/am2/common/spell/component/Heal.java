package am2.common.spell.component;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.api.DamageSources;
import am2.api.affinity.Affinity;
import am2.api.spell.Operation;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.common.bosses.AM2Boss;
import am2.common.defs.BlockDefs;
import am2.common.defs.ItemDefs;
import am2.common.extensions.AffinityData;
import am2.common.extensions.EntityExtension;
import am2.common.utils.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Heal extends SpellComponent{

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos blockPos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		return false;
	}

	@Override
	public boolean applyEffectEntity(SpellData spell, World world, EntityLivingBase caster, Entity target){
		if (target instanceof EntityLivingBase){
			if (((EntityLivingBase)target).getCreatureAttribute() == EnumCreatureAttribute.UNDEAD){
				double healing = spell.getModifiedValue(10, SpellModifiers.HEALING, Operation.MULTIPLY, world, caster, target);
				target.setFire(2);
				return SpellUtils.attackTargetSpecial(spell, target, DamageSources.causeHolyDamage(caster), (float) (healing * (0.5f + 2 * AffinityData.For(caster).getAffinityDepth(Affinity.LIFE))));
			}else{
				double healing = spell.getModifiedValue(2, SpellModifiers.HEALING, Operation.MULTIPLY, world, caster, target);
				if (!(caster instanceof AM2Boss))
					healing *= 1F + AffinityData.For(caster).getAffinityDepth(Affinity.LIFE);
				if (EntityExtension.For((EntityLivingBase)target).getHealCooldown() == 0){
					((EntityLivingBase)target).heal((float) healing);
					EntityExtension.For((EntityLivingBase)target).setHealCooldown(60);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.HEALING);
	}


	@Override
	public float manaCost(){
		return 225f;
	}
	
	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

    @Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.LIFE);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.GREEN.getDyeDamage()),
				BlockDefs.aum
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.05f;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}
}
