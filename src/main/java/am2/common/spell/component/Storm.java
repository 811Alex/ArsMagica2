package am2.common.spell.component;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.api.affinity.Affinity;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.common.defs.ItemDefs;
import am2.common.items.ItemOre;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Storm extends SpellComponent{

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos blockPos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		applyEffect(caster, world);
		return true;
	}

	@Override
	public boolean applyEffectEntity(SpellData spell, World world, EntityLivingBase caster, Entity target){
		applyEffect(caster, world);
		return true;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.noneOf(SpellModifiers.class);
	}

	private void applyEffect(EntityLivingBase caster, World world){
		float rainStrength = world.getRainStrength(1.0f);
		if (rainStrength > 0.9D){
			if (!world.isRemote){
				int xzradius = 50;
				int random = world.rand.nextInt(100);
				if (random < 20){
					int randPosX = (int)caster.posX + world.rand.nextInt(xzradius * 2) - xzradius;
					int randPosZ = (int)caster.posZ + world.rand.nextInt(xzradius * 2) - xzradius;
					int posY = (int)caster.posY;

					while (!world.canBlockSeeSky(new BlockPos(randPosX, posY, randPosZ))){
						posY++;
					}
					while (world.getBlockState(new BlockPos(randPosX, posY - 1, randPosZ)).getBlock().equals(Blocks.AIR)){
						posY--;
					}

					EntityLightningBolt bolt = new EntityLightningBolt(world, randPosX, posY, randPosZ, false);
					world.addWeatherEffect(bolt);
				}else if (random < 80){
					List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, caster.getEntityBoundingBox().expand(xzradius, 10D, xzradius), IMob.MOB_SELECTOR);
					if (entities.size() <= 0){
						return;
					}
					Entity target = entities.get(world.rand.nextInt(entities.size()));
					if (target != null && world.canBlockSeeSky(target.getPosition())){
						if (caster instanceof EntityPlayer){
							target.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)caster), 1);
						}
						EntityLightningBolt bolt = new EntityLightningBolt(world, target.posX, target.posY, target.posZ, false);
						world.addWeatherEffect(bolt);
					}
				}
			}
		}else{
			if (!world.isRemote){
				world.getWorldInfo().setRaining(true);
			}
		}
	}

	@Override
	public float manaCost(){
		return 15;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

    @Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.LIGHTNING, Affinity.NATURE);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.YELLOW.getDyeDamage()),
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_BLUE_TOPAZ),
				Items.GHAST_TEAR
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.00001f;
	}
	
	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {}
}
