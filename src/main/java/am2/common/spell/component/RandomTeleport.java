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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class RandomTeleport extends SpellComponent{

	@Override
	public boolean applyEffectEntity(SpellData spell, World world, EntityLivingBase caster, Entity target){
		Vec3d rLoc = getRandomTeleportLocation(world, spell, caster, target);
		return teleportTo(rLoc.xCoord, rLoc.yCoord, rLoc.zCoord, target);
	}

	private Vec3d getRandomTeleportLocation(World world, SpellData spell, EntityLivingBase caster, Entity target){
		Vec3d origin = new Vec3d(target.posX, target.posY, target.posZ);
		float maxDist = (float)spell.getModifiedValue(9, SpellModifiers.RANGE, Operation.MULTIPLY, world, caster, target);
		origin = origin.add(new Vec3d((world.rand.nextDouble() - 0.5) * maxDist, (world.rand.nextDouble() - 0.5) * maxDist, (world.rand.nextDouble() - 0.5) * maxDist));
		return origin;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.RANGE);
	}

	protected boolean teleportTo(double par1, double par3, double par5, Entity target){
		if (target instanceof EntityLivingBase){
			EnderTeleportEvent event = new EnderTeleportEvent((EntityLivingBase)target, par1, par3, par5, 0);
			if (MinecraftForge.EVENT_BUS.post(event)){
				return false;
			}
			par1 = event.getTargetX();
			par3 = event.getTargetY();
			par5 = event.getTargetZ();
		}

		double d3 = target.posX;
		double d4 = target.posY;
		double d5 = target.posZ;
		target.posX = par1;
		target.posY = par3;
		target.posZ = par5;
		boolean locationValid = false;
		BlockPos pos = target.getPosition();
		Block l;

		if (target.worldObj.getBlockState(pos) != null){
			boolean targetBlockIsSolid = false;

			while (!targetBlockIsSolid && pos.getY() > 0){
				l = target.worldObj.getBlockState(pos.down()).getBlock();

				if (l != Blocks.AIR && l.isPassable(target.worldObj, pos)){
					targetBlockIsSolid = true;
				}else{
					--target.posY;
					pos = pos.down();
				}
			}

			if (targetBlockIsSolid){
				target.setPosition(target.posX, target.posY, target.posZ);
				if (target.worldObj.getCollisionBoxes(target, target.getEntityBoundingBox()).isEmpty()){
					locationValid = true;
				}
			}
		}

		if (!locationValid){
			target.setPosition(d3, d4, d5);
			return false;
		}else{
			return true;
		}
	}

	@Override
	public float manaCost(){
		return 52.5f;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

    @Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.ENDER);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.BLACK.getDyeDamage()),
				Items.ENDER_PEARL
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {}

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos blockPos, EnumFacing blockFace,
			double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		return false;
	}
}
