package am2.spell.component;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.api.affinity.Affinity;
import am2.defs.ItemDefs;
import am2.enchantments.AMEnchantments;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleMoveOnHeading;
import am2.spell.SpellComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Disarm extends SpellComponent{

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){

//		if (target instanceof EntityLightMage || target instanceof EntityDarkMage)
//			return false;

		if (target instanceof EntityPlayer && (!ArsMagica2.config.getDisarmAffectsPlayers() || (!world.isRemote && !FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled())))
			return false;

		if (target instanceof EntityPlayer && ((EntityPlayer)target).getActiveItemStack() != null && !target.worldObj.isRemote){
			if (EnchantmentHelper.getEnchantmentLevel(AMEnchantments.soulbound, ((EntityPlayer)target).getActiveItemStack()) > 0)
				return true;
			((EntityPlayer)target).dropItem(true);
			return true;
		}else if (target instanceof EntityMob && ((EntityMob)target).getActiveItemStack() != null){

			if (EnchantmentHelper.getEnchantmentLevel(AMEnchantments.soulbound, ((EntityMob)target).getActiveItemStack()) > 0)
				return true;

			if (!world.isRemote){
				EntityItem item = new EntityItem(world);
				ItemStack dropstack = ((EntityMob)target).getActiveItemStack().copy();
				if (dropstack.getMaxDamage() > 0)
					dropstack.setItemDamage((int)Math.floor(dropstack.getMaxDamage() * (0.8f + (world.rand.nextFloat() * 0.19f))));
				item.setEntityItemStack(dropstack);
				item.setPosition(target.posX, target.posY, target.posZ);
				world.spawnEntityInWorld(item);
			}
			((EntityMob)target).setItemStackToSlot(EntityMob.getSlotForItemStack(stack), null);;

			((EntityMob)target).setAttackTarget(caster);

			Iterator<EntityAITaskEntry> it = ((EntityMob)target).tasks.taskEntries.iterator();
			boolean removed = false;
			while (it.hasNext()){
				EntityAITaskEntry task = (EntityAITaskEntry)it.next();
				if (task.action instanceof EntityAIAttackRanged){
					it.remove();
					removed = true;
					break;
				}
			}

			if (removed){
				((EntityMob)target).tasks.addTask(5, new EntityAIAttackMelee((EntityCreature)target, 0.5, true));
				((EntityMob)target).setCanPickUpLoot(true);
			}
		}else if (target instanceof EntityEnderman){
			IBlockState blockID = ((EntityEnderman)target).getHeldBlockState();

			if (blockID != null){
				((EntityEnderman)target).setHeldBlockState(null);
				ItemStack dropstack = new ItemStack(blockID.getBlock(), 1, blockID.getBlock().getMetaFromState(blockID));
				EntityItem item = new EntityItem(world);
				item.setEntityItemStack(dropstack);
				item.setPosition(target.posX, target.posY, target.posZ);
				world.spawnEntityInWorld(item);
			}
			((EntityMob)target).setAttackTarget(caster);
		}
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 130;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 25; ++i){
			AMParticle particle = (AMParticle)ArsMagica2.proxy.particleManager.spawn(world, "sparkle2", x, y, z);
			if (particle != null){
				particle.addRandomOffset(1, 2, 1);
				particle.AddParticleController(new ParticleMoveOnHeading(particle, MathHelper.wrapDegrees((target instanceof EntityLivingBase ? ((EntityLivingBase)target).rotationYawHead : target.rotationYaw) + 90), MathHelper.wrapDegrees(target.rotationPitch), 0.1 + rand.nextDouble() * 0.5, 1, false));
				particle.AddParticleController(new ParticleFadeOut(particle, 1, false).setFadeSpeed(0.05f));
				particle.setAffectedByGravity();
				if (rand.nextBoolean())
					particle.setRGBColorF(0.7f, 0.7f, 0.1f);
				else
					particle.setRGBColorF(0.1f, 0.7f, 0.1f);
				particle.setMaxAge(40);
				particle.setParticleScale(0.1f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.NONE);
	}
	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.ORANGE.getDyeDamage()),
				Items.IRON_SWORD
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

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos blockPos, EnumFacing blockFace,
			double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		// TODO Auto-generated method stub
		return false;
	}
}
