package am2.common.spell.shape;

import am2.ArsMagica2;
import am2.api.affinity.Affinity;
import am2.api.spell.Operation;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.api.spell.SpellShape;
import am2.client.particles.*;
import am2.common.defs.ItemDefs;
import am2.common.defs.SoundDefs;
import am2.common.entity.EntitySpellProjectile;
import am2.common.items.ItemOre;
import am2.common.power.PowerTypes;
import am2.common.spell.SpellCastResult;
import am2.common.utils.AffinityShiftUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

public class AoE extends SpellShape {

	@Override
	public SpellCastResult beginStackStage(SpellData spell, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, EnumFacing side, boolean giveXP, int useCount) {
		double radius = spell.getModifiedValue(2, SpellModifiers.RADIUS, Operation.ADD, world, caster, target); // SpellUtils.getModifiedDouble_Add(1, stack, caster, target, world, SpellModifiers.RADIUS);
		List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius));

		boolean appliedToAtLeastOneEntity = false;

		for (Entity e : entities) {
			if (e == caster || e instanceof EntitySpellProjectile) continue;
			if (e instanceof EntityDragonPart && ((EntityDragonPart) e).entityDragonObj instanceof EntityLivingBase)
				e = (EntityLivingBase) ((EntityDragonPart) e).entityDragonObj;
			if (spell.copy().applyComponentsToEntity(world, caster, e) == SpellCastResult.SUCCESS)
				appliedToAtLeastOneEntity = true;
		}

		BlockPos pos = new BlockPos(x, y, z);

		if (side != null) {
			switch (side) {
				case UP:
				case DOWN:
					int gravityMagnitude = spell.getModifierCount(SpellModifiers.GRAVITY);
					return applyStageHorizontal(spell, caster, world, pos, side, (int) Math.floor(radius), gravityMagnitude, giveXP);
				case NORTH:
				case SOUTH:
					return applyStageVerticalZ(spell, caster, world, pos, side, (int) Math.floor(radius), giveXP);
				case EAST:
				case WEST:
					return applyStageVerticalX(spell, caster, world, pos, side, (int) Math.floor(radius), giveXP);
			}
		} else {
			int gravityMagnitude = spell.getModifierCount(SpellModifiers.GRAVITY);
			return applyStageHorizontal(spell, caster, world, pos, null, (int) Math.floor(radius), gravityMagnitude, giveXP);
		}

		if (appliedToAtLeastOneEntity) {
			return SpellCastResult.SUCCESS;
		}

		return SpellCastResult.EFFECT_FAILED;
	}

	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.RADIUS, SpellModifiers.GRAVITY);
	}


	private SpellCastResult applyStageHorizontal(SpellData stack, EntityLivingBase caster, World world, BlockPos pos, EnumFacing face, int radius, int gravityMagnitude, boolean giveXP) {

		for (int i = -radius; i <= radius; ++i) {
			for (int j = -radius; j <= radius; ++j) {
				BlockPos lookPos = pos.add(i, 0, j);
				int searchDist = 0;
				if (gravityMagnitude > 0) {
					while (world.isAirBlock(lookPos) && searchDist < gravityMagnitude) {
						pos.down();
						searchDist++;
					}
				}
				if (world.isAirBlock(lookPos)) continue;
				SpellCastResult result = stack.copy().applyComponentsToGround(world, caster, lookPos, face, lookPos.getX(), lookPos.getY(), lookPos.getZ());
				if (result != SpellCastResult.SUCCESS)
					return result;
			}
		}
		return SpellCastResult.SUCCESS;
	}

	private SpellCastResult applyStageVerticalX(SpellData stack, EntityLivingBase caster, World world, BlockPos pos, EnumFacing face, int radius, boolean giveXP) {
		for (int i = -radius; i <= radius; ++i) {
			for (int j = -radius; j <= radius; ++j) {
				BlockPos lookPos = pos.add(0, j, i);
				if (world.isAirBlock(lookPos)) continue;
				SpellCastResult result = stack.copy().applyComponentsToGround(world, caster, lookPos, face, lookPos.getX(), lookPos.getY(), lookPos.getZ());
				if (result != SpellCastResult.SUCCESS)
					return result;
			}
		}
		return SpellCastResult.SUCCESS;
	}

	private SpellCastResult applyStageVerticalZ(SpellData stack, EntityLivingBase caster, World world, BlockPos pos, EnumFacing face, int radius, boolean giveXP) {
		for (int i = -radius; i <= radius; ++i) {
			for (int j = -radius; j <= radius; ++j) {
				BlockPos lookPos = pos.add(i, j, 0);
				if (world.isAirBlock(lookPos)) continue;
				SpellCastResult result = stack.copy().applyComponentsToGround(world, caster, lookPos, face, lookPos.getX(), lookPos.getY(), lookPos.getZ());
				if (result != SpellCastResult.SUCCESS)
					return result;
			}
		}
		return SpellCastResult.SUCCESS;
	}

	@Override
	public boolean isChanneled() {
		return false;
	}

	@Override
	public Object[] getRecipe() {
		return new Object[]{
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_MOONSTONE),
				AffinityShiftUtils.getEssenceForAffinity(Affinity.AIR),
				String.format("E:%d|%d|%d", PowerTypes.LIGHT.ID(), PowerTypes.NEUTRAL.ID(), PowerTypes.DARK.ID()), 1000,
				Blocks.TNT
		};
	}

	@Override
	public float manaCostMultiplier() {
		//FIXME
//		int multiplier = 2;
//		int radiusMods = 0;
//		int stages = SpellUtils.numStages(spellStack);
//		for (int i = SpellUtils.currentStage(spellStack); i < stages; ++i){
//			if (!SpellUtils.getShapeForStage(spellStack, i).equals(this)) continue;
//
//			ArrayList<SpellModifier> mods = SpellUtils.getModifiersForStage(spellStack, i);
//			for (SpellModifier modifier : mods){
//				if (modifier.getAspectsModified().contains(SpellModifiers.RADIUS)){
//					radiusMods++;
//				}
//			}
//		}
//		return multiplier * (radiusMods + 1);
		return 2F;
	}

	@Override
	public boolean isTerminusShape() {
		return true;
	}

	@Override
	public boolean isPrincipumShape() {
		return false;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
	}

	@Override
	public SoundEvent getSoundForAffinity(Affinity affinity, SpellData stack, World world) {
		return SoundDefs.CAST_MAP.get(affinity);
	}
}
