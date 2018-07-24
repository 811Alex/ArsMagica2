package am2.common.bosses;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.api.math.AMVector3;
import am2.api.sources.DamageSourceLightning;
import am2.common.bosses.ai.EntityAIDispel;
import am2.common.bosses.ai.EntityAIHurricane;
import am2.common.bosses.ai.EntityAISpawnWhirlwind;
import am2.common.defs.AMSounds;
import am2.common.defs.ItemDefs;
import am2.common.entity.ai.EntityAIGuardSpawnLocation;
import am2.common.packet.AMNetHandler;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.World;

public class EntityAirGuardian extends AM2Boss{
	private boolean useLeftArm = false;
	public float spinRotation = 0;
	private float orbitRotation;
	public int hitCount = 0;
	private boolean firstTick = true;

	public EntityAirGuardian(World par1World){
		super(par1World);
		this.setSize(0.6f, 2.5f);
	}

	@Override
	protected void applyEntityAttributes(){
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(220D);
	}

	@Override
	public int getTotalArmorValue(){
		return 14;
	}

	@Override
	protected void initSpecificAI(){
		this.tasks.addTask(7, new EntityAIWander(this, 0.7f));
		this.tasks.addTask(1, new EntityAIDispel(this));
		this.tasks.addTask(1, new EntityAISpawnWhirlwind(this, 0.5f));
		this.tasks.addTask(2, new EntityAIHurricane(this, 0.5f));
	}

	@Override
	public void setCurrentAction(BossActions action){
		super.setCurrentAction(action);

		this.spinRotation = 0;
		this.hitCount = 0;

		if (action == BossActions.CASTING)
			this.useLeftArm = !this.useLeftArm;

		if (!worldObj.isRemote){
			AMNetHandler.INSTANCE.sendActionUpdateToAllAround(this);
		}
	}

	public boolean useLeftArm(){
		return this.useLeftArm;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void onUpdate(){

		if (firstTick){
			this.tasks.addTask(0, new EntityAIGuardSpawnLocation(this, 0.5F, 5, 16, new AMVector3(this)));
			this.firstTick = false;
		}

		this.orbitRotation += 2f;
		this.orbitRotation %= 360;

		switch (currentAction){
		case IDLE:
			break;
		case SPINNING:
			this.spinRotation = (this.spinRotation - 40) % 360;
			break;
		}

		if (this.motionY < 0){
			this.motionY *= 0.8999999f;
		}

		if (this.posY < 150 && this.posY < 145)
			this.setDead();

		super.onUpdate();
	}

	public float getOrbitRotation(){
		return this.orbitRotation;
	}

	@Override
	public boolean canBePushed(){
		return this.getCurrentAction() != BossActions.SPINNING;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {}

	@Override
	protected void dropFewItems(boolean par1, int par2){
		if (par1)
			this.entityDropItem(new ItemStack(ItemDefs.infinityOrb, 1, 1), 0.0f);

		int i = rand.nextInt(4);

		for (int j = 0; j < i; j++){
			this.entityDropItem(new ItemStack(ItemDefs.essence, 1, ArsMagicaAPI.getAffinityRegistry().getId(Affinity.AIR)), 0.0f);
		}

		i = rand.nextInt(10);

		if (i < 3 && par1){
			this.entityDropItem(ItemDefs.airSledEnchanted.copy(), 0.0f);
		}
	}

	@Override
	public boolean attackEntityFrom(DamageSource par1DamageSource, float par2){
		hitCount++;
		return super.attackEntityFrom(par1DamageSource, par2);
	}

	@Override
	protected float modifyDamageAmount(DamageSource source, float damageAmt){
		if (source.isMagicDamage())
			damageAmt /= 2;
		else if (source instanceof DamageSourceLightning)
			damageAmt *= 2;
		return damageAmt;
	}

	@Override
	protected SoundEvent getHurtSound(){
		return AMSounds.AIR_GUARDIAN_HIT;
	}

	@Override
	protected SoundEvent getDeathSound(){
		return AMSounds.AIR_GUARDIAN_DEATH;
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return AMSounds.AIR_GUARDIAN_IDLE;
	}

	@Override
	public SoundEvent getAttackSound(){
		return null;
	}

	@Override
	protected Color getBarColor() {
		return Color.GREEN;
	}
}
