package am2.common.entity;

import java.util.List;

import am2.api.DamageSources;
import am2.common.extensions.EntityExtension;
import am2.common.utils.MathUtilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityManaVortex extends Entity{

	private float rotation;
	private float scale = 0.0f;

	public static final DataParameter<Integer> TICKS_TO_EXIST = EntityDataManager.createKey(EntityManaVortex.class, DataSerializers.VARINT);
	public static final DataParameter<Float> MANA_STOLEN = EntityDataManager.createKey(EntityManaVortex.class, DataSerializers.FLOAT);
	private boolean hasGoneBoom = false;

	public EntityManaVortex(World par1World){
		super(par1World);
	}

	@Override
	protected void entityInit(){
		this.dataManager.register(TICKS_TO_EXIST, 50 + rand.nextInt(250));
		this.dataManager.register(MANA_STOLEN, 0.0f);
	}

	public int getTicksToExist(){
		try{
			return this.dataManager.get(TICKS_TO_EXIST);
		}catch (Throwable t){
			return -1;
		}
	}

	@Override
	public void onUpdate(){
		this.ticksExisted++;
		this.rotation += 5;
		if (!this.worldObj.isRemote && (this.isDead || this.ticksExisted >= getTicksToExist())){
			this.setDead();
			return;
		}

		if (this.getTicksToExist() - this.ticksExisted <= 20){
			this.scale -= 1f / 20f;
		}else if (this.scale < 0.99f){
			this.scale = (float)(Math.sin((float)this.ticksExisted / 50));
		}

		if (getTicksToExist() - this.ticksExisted <= 5 && !hasGoneBoom){
			hasGoneBoom = true;
			List<EntityLivingBase> players = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(3 + Math.floor(this.ticksExisted / 50), 2, 3 + Math.floor(this.ticksExisted / 50)));
			float damage = this.dataManager.get(MANA_STOLEN) * 0.005f;
			if (damage > 100)
				damage = 100;

			Object[] playerArray = players.toArray();
			for (Object o : playerArray){
				EntityLivingBase e = (EntityLivingBase)o;
				RayTraceResult mop = this.worldObj.rayTraceBlocks(new Vec3d(this.posX, this.posY, this.posZ), new Vec3d(e.posX, e.posY + e.getEyeHeight(), e.posZ), false);
				if (mop == null)
					e.attackEntityFrom(DamageSources.causePhysicalDamage(this), damage);
			}
		}

		if (getTicksToExist() - this.ticksExisted > 30){
			//get all players within 5 blocks
			List<EntityLivingBase> players = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(3 + Math.floor(this.ticksExisted / 50), 2, 3 + Math.floor(this.ticksExisted / 50)));
			Object[] playerArray = players.toArray();

			for (Object o : playerArray){
				EntityLivingBase e = (EntityLivingBase)o;

				RayTraceResult mop = this.worldObj.rayTraceBlocks(new Vec3d(this.posX, this.posY, this.posZ), new Vec3d(e.posX, e.posY + e.getEyeHeight(), e.posZ), false);
				if (mop != null)
					continue;
				float manaStolen = EntityExtension.For(e).getMaxMana() * 0.01f;
				float curMana = EntityExtension.For(e).getCurrentMana();

				if (manaStolen > curMana)
					manaStolen = curMana;

				this.dataManager.set(MANA_STOLEN, this.dataManager.get(MANA_STOLEN) + manaStolen);
				EntityExtension.For(e).setCurrentMana(EntityExtension.For(e).getCurrentMana() - manaStolen);

				Vec3d movement = MathUtilities.GetMovementVectorBetweenEntities(e, this);
				float speed = -0.075f;

				e.addVelocity(movement.xCoord * speed, movement.yCoord * speed, movement.zCoord * speed);
			}
		}
	}

	public float getManaStolenPercent(){
		float damage = this.dataManager.get(MANA_STOLEN) * 0.005f;
		if (damage > 100)
			damage = 100;
		return damage / 100f;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound var1){
		dataManager.set(TICKS_TO_EXIST, var1.getInteger("ticksToExist"));
		dataManager.set(MANA_STOLEN, var1.getFloat("manaStolen"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound var1){
		var1.setInteger("ticksToExist", this.getTicksToExist());
		var1.setFloat("manaStolen", this.dataManager.get(MANA_STOLEN));
	}

	public float getRotation(){
		return this.rotation;
	}

	public float getScale(){
		return this.scale;
	}

	public float getManaStolen(){
		return this.dataManager.get(MANA_STOLEN);
	}
}
