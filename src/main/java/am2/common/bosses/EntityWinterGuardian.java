package am2.common.bosses;

import java.util.List;

import am2.api.ArsMagicaAPI;
import am2.api.DamageSources;
import am2.api.affinity.Affinity;
import am2.api.sources.DamageSourceFire;
import am2.api.sources.DamageSourceFrost;
import am2.common.bosses.ai.EntityAICastSpell;
import am2.common.bosses.ai.EntityAISmash;
import am2.common.bosses.ai.EntityAIStrikeAttack;
import am2.common.bosses.ai.EntityWinterGuardianLaunchArm;
import am2.common.bosses.ai.ISpellCastCallback;
import am2.common.buffs.BuffEffectFrostSlowed;
import am2.common.defs.AMSounds;
import am2.common.defs.ItemDefs;
import am2.common.packet.AMNetHandler;
import am2.common.utils.NPCSpells;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.World;

public class EntityWinterGuardian extends AM2Boss{

	private boolean hasRightArm;
	private boolean hasLeftArm;
	private float orbitRotation;

	public EntityWinterGuardian(World par1World){
		super(par1World);
		this.setSize(1.25f, 3.25f);
		hasRightArm = true;
		hasLeftArm = true;
	}

	@Override
	protected void initSpecificAI(){
		this.tasks.addTask(1, new EntityAICastSpell<EntityWinterGuardian>(this, NPCSpells.instance.dispel, 16, 23, 50, BossActions.CASTING, new ISpellCastCallback<EntityWinterGuardian>(){
			@Override
			public boolean shouldCast(EntityWinterGuardian host, ItemStack spell){
				return host.getActivePotionEffects().size() > 0;
			}
		}));
		this.tasks.addTask(2, new EntityAISmash(this, 0.5f, DamageSources.DamageSourceTypes.FROST));
		this.tasks.addTask(3, new EntityAIStrikeAttack(this, 0.5f, 6f, DamageSources.DamageSourceTypes.FROST));
		this.tasks.addTask(4, new EntityWinterGuardianLaunchArm(this, 0.5f));
	}

	public void returnOneArm(){
		if (!hasLeftArm) hasLeftArm = true;
		else if (!hasRightArm) hasRightArm = true;
	}

	public void launchOneArm(){
		if (hasLeftArm) hasLeftArm = false;
		else if (hasRightArm) hasRightArm = false;
	}

	public boolean hasLeftArm(){
		return hasLeftArm;
	}

	public boolean hasRightArm(){
		return hasRightArm;
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(290D);
	}

	@Override
	public int getTotalArmorValue(){
		return 23;
	}

	@Override
	public void onUpdate(){
		if (worldObj.getBiome(getPosition()).getEnableSnow() && worldObj.getWorldInfo().isRaining()){
			this.heal(0.1f);
		}
		if (this.ticksExisted % 100 == 0){
			List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(2, 2, 2));
			for (EntityLivingBase entity : entities){
				if (entity == this)
					continue;
				entity.addPotionEffect(new BuffEffectFrostSlowed(220, 1));
				entity.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("mining_fatigue"), 220, 3));
			}
		}
		super.onUpdate();
	}

	@Override
	public void setCurrentAction(BossActions action){
		super.setCurrentAction(action);

		if (!worldObj.isRemote){
			AMNetHandler.INSTANCE.sendActionUpdateToAllAround(this);
		}
	}

	private void updateRotations(){
		this.orbitRotation += 2f;
		this.orbitRotation %= 360;
	}

	public float getOrbitRotation(){
		return this.orbitRotation;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2){
		if (par1)
			this.entityDropItem(new ItemStack(ItemDefs.infinityOrb, 1, 2), 0.0f);

		int i = rand.nextInt(4);

		for (int j = 0; j < i; j++){
			this.entityDropItem(new ItemStack(ItemDefs.essence, 1, ArsMagicaAPI.getAffinityRegistry().getId(Affinity.ICE)), 0.0f);
		}
		i = rand.nextInt(10);

		if (i < 3){
			this.entityDropItem(ItemDefs.winterArmEnchanted.copy(), 0.0f);
		}
	}

	@Override
	protected float modifyDamageAmount(DamageSource source, float damageAmt){
		if (source instanceof DamageSourceFrost)
			damageAmt = 0;
		if (source.isFireDamage() || source instanceof DamageSourceFire)
			damageAmt *= 2;
		return damageAmt;
	}

	@Override
	protected SoundEvent getHurtSound(){
		return AMSounds.WINTER_GUARDIAN_HIT;
	}

	@Override
	protected SoundEvent getDeathSound(){
		return AMSounds.WINTER_GUARDIAN_DEATH;
	}

	@Override
	protected SoundEvent getAmbientSound(){
		return AMSounds.WINTER_GUARDIAN_IDLE;
	}

	@Override
	public SoundEvent getAttackSound(){
		return AMSounds.WINTER_GUARDIAN_ATTACK;
	}

	@Override
	protected Color getBarColor() {
		return Color.RED;
	}
}
