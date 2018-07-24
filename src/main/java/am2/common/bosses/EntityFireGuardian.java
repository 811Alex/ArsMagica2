package am2.common.bosses;

import java.util.List;

import am2.api.ArsMagicaAPI;
import am2.api.DamageSources;
import am2.api.affinity.Affinity;
import am2.api.sources.DamageSourceFrost;
import am2.common.bosses.ai.EntityAICastSpell;
import am2.common.bosses.ai.EntityAIDispel;
import am2.common.bosses.ai.EntityAIDive;
import am2.common.bosses.ai.EntityAIFireRain;
import am2.common.bosses.ai.EntityAIFlamethrower;
import am2.common.defs.AMSounds;
import am2.common.defs.ItemDefs;
import am2.common.packet.AMNetHandler;
import am2.common.utils.NPCSpells;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.World;

public class EntityFireGuardian extends AM2Boss{

	private boolean isUnderground = false;
	private int hitCount = 0;

	public EntityFireGuardian(World par1World){
		super(par1World);
		this.setSize(1.0f, 4.0f);
		this.isImmuneToFire = true;
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(250);
	}

	@Override
	public int getTotalArmorValue(){
		return 17;
	}

	@Override
	protected void initSpecificAI(){
		this.tasks.addTask(1, new EntityAIFireRain(this));
		this.tasks.addTask(1, new EntityAIDispel(this));
		this.tasks.addTask(2, new EntityAIDive(this));
		this.tasks.addTask(2, new EntityAICastSpell<EntityFireGuardian>(this, NPCSpells.instance.meltArmor, 12, 23, 40, BossActions.CASTING));
		this.tasks.addTask(3, new EntityAIFlamethrower(this));
		this.tasks.addTask(4, new EntityAICastSpell<EntityFireGuardian>(this, NPCSpells.instance.fireBolt, 12, 23, 5, BossActions.CASTING));
	}

	public boolean getIsUnderground(){
		return this.isUnderground;
	}


	@Override
	public void setCurrentAction(BossActions action){
		super.setCurrentAction(action);

		if (action == BossActions.SPINNING){
			this.addVelocity(0, 1.5, 0);
		}else{
			hitCount = 0;
			isUnderground = false;
		}

		if (!worldObj.isRemote){
			AMNetHandler.INSTANCE.sendActionUpdateToAllAround(this);
		}
	}

	private void nova(){
		List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(2.5, 2.5, 2.5).addCoord(0, -3, 0));
		for (EntityLivingBase ent : entities){
			if (ent == this)
				continue;
			ent.attackEntityFrom(DamageSources.causeFireDamage(this), 5);
		}
	}

	private void flamethrower(){
		Vec3d look = this.getLook(1.0f);
		List<EntityLivingBase> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(2.5, 2.5, 2.5).addCoord(look.xCoord * 3, 0, look.zCoord * 3));
		for (EntityLivingBase ent : entities){
			if (ent == this)
				continue;
			ent.attackEntityFrom(DamageSources.causeFireDamage(this), 5);
		}
	}

	private void doFlameShield(){
		if (worldObj.isRemote){
			return;
		}else{
			for (Object p : worldObj.playerEntities){
				EntityPlayer player = (EntityPlayer)p;
				if (this.getDistanceSqToEntity(player) < 9){
					player.attackEntityFrom(DamageSources.causeFireDamage(this), 2);
				}
			}
		}
	}

	@Override
	public boolean isBurning(){
		return !isUnderground;
	}

	@Override
	public void onUpdate(){

		if (ticksInCurrentAction == 30 && currentAction == BossActions.SPINNING){
			nova();
		}

		if (ticksInCurrentAction > 13 && currentAction == BossActions.LONG_CASTING){
			if (this.getAttackTarget() != null)
				this.faceEntity(this.getAttackTarget(), 10, 10);
			flamethrower();
		}

		doFlameShield();

		super.onUpdate();
	}

	@Override
	public void fall(float par1, float par2){
		if (this.getCurrentAction() == BossActions.SPINNING){
			this.isUnderground = true;
			return;
		}
		super.fall(par1, par2);
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){

		if (par1DamageSource.isFireDamage()){
			this.heal(par2);
			return false;
		}

		if (this.isUnderground && this.getCurrentAction() != BossActions.SPINNING){
			return false;
		}else{
			if (this.getCurrentAction() == BossActions.SPINNING)
				++hitCount;
		}

		return super.attackEntityFrom(par1DamageSource, par2);
	}

	@Override
	protected float modifyDamageAmount(DamageSource source, float damageAmt){
		if (source == DamageSource.drown)
			damageAmt *= 2;
		else if (source instanceof DamageSourceFrost)
			damageAmt /= 3;
		return damageAmt;
	}

	@Override
	protected SoundEvent getHurtSound(){
		return AMSounds.FIRE_GUARDIAN_HIT;
	}

	@Override
	protected SoundEvent getDeathSound(){
		return AMSounds.FIRE_GUARDIAN_DEATH;
	}

	@Override
	protected SoundEvent getAmbientSound(){
		return AMSounds.FIRE_GUARDIAN_IDLE;
	}

	@Override
	public SoundEvent getAttackSound(){
		return AMSounds.FIRE_GUARDIAN_ATTACK;
	}

	public int getNumHits(){
		return this.hitCount;
	}

	@Override
	protected void dropFewItems(boolean par1, int par2){
		if (par1)
			this.entityDropItem(new ItemStack(ItemDefs.infinityOrb, 1, 2), 0.0f);

		int i = rand.nextInt(4);

		for (int j = 0; j < i; j++){
			this.entityDropItem(new ItemStack(ItemDefs.essence, 1, ArsMagicaAPI.getAffinityRegistry().getId(Affinity.FIRE)), 0.0f);
		}

		i = rand.nextInt(10);

		if (i < 3 && par1){
			this.entityDropItem(ItemDefs.fireEarsEnchanted.copy(), 0.0f);
		}
	}

	@Override
	protected Color getBarColor() {
		return Color.RED;
	}
}
