package am2.common.entity;

import com.google.common.base.Optional;

import am2.api.math.AMVector3;
import am2.common.blocks.tileentity.TileEntityCraftingAltar;
import am2.common.entity.ai.EntityAISpellmaking;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityShadowHelper extends EntityLiving{

	private static final DataParameter<String> DW_MIMIC_USER = EntityDataManager.createKey(EntityShadowHelper.class, DataSerializers.STRING); //who are we going to mimic (MC skin)?	
	private static final DataParameter<Optional<ItemStack>> DW_SEARCH_ITEM = EntityDataManager.createKey(EntityShadowHelper.class, DataSerializers.OPTIONAL_ITEM_STACK); //what are we currently looking for?
	private static final DataParameter<Integer> DW_TRANS_LOC_X = EntityDataManager.createKey(EntityShadowHelper.class, DataSerializers.VARINT); //x-coordinate of search
	private static final DataParameter<Integer> DW_TRANS_LOC_Y = EntityDataManager.createKey(EntityShadowHelper.class, DataSerializers.VARINT); //y-coordinate of search
	private static final DataParameter<Integer> DW_TRANS_LOC_Z = EntityDataManager.createKey(EntityShadowHelper.class, DataSerializers.VARINT); //z-coordinate of search
	private static final DataParameter<Optional<ItemStack>> DW_HELD_ITEM = EntityDataManager.createKey(EntityShadowHelper.class, DataSerializers.OPTIONAL_ITEM_STACK); //current held item
	private static final DataParameter<Integer> DW_DROP_LOC_X = EntityDataManager.createKey(EntityShadowHelper.class, DataSerializers.VARINT); //x-coordinate of search
	private static final DataParameter<Integer> DW_DROP_LOC_Y = EntityDataManager.createKey(EntityShadowHelper.class, DataSerializers.VARINT); //y-coordinate of search
	private static final DataParameter<Integer> DW_DROP_LOC_Z = EntityDataManager.createKey(EntityShadowHelper.class, DataSerializers.VARINT); //z-coordinate of search

	private TileEntityCraftingAltar altarTarget = null;
	private String lastDWString = "";

	public EntityShadowHelper(World world){
		super(world);
		initAI();
	}

	@Override
	public void onDeath(DamageSource par1DamageSource){
		super.onDeath(par1DamageSource);
	}

	@Override
	protected SoundEvent getDeathSound(){
		return null;
	}

	@Override
	protected boolean canDespawn(){
		return false;
	}

	@Override
	protected void onDeathUpdate(){
		this.setDead();
	}

	@Override
	protected void entityInit(){
		super.entityInit();
		this.dataManager.register(DW_MIMIC_USER, "");
		this.dataManager.register(DW_SEARCH_ITEM, Optional.of(new ItemStack(Items.APPLE)));
		this.dataManager.register(DW_TRANS_LOC_X, 0);
		this.dataManager.register(DW_TRANS_LOC_Y, 0);
		this.dataManager.register(DW_TRANS_LOC_Z, 0);
		this.dataManager.register(DW_HELD_ITEM, Optional.of(new ItemStack(Items.PAPER)));
		this.dataManager.register(DW_DROP_LOC_X, 0);
		this.dataManager.register(DW_DROP_LOC_Y, 0);
		this.dataManager.register(DW_DROP_LOC_Z, 0);
	}

	public void setSearchLocationAndItem(AMVector3 location, ItemStack item){
		if (this.worldObj.isRemote) return;
		this.dataManager.set(DW_SEARCH_ITEM, Optional.of(item));
		this.dataManager.set(DW_TRANS_LOC_X, (int)location.x);
		this.dataManager.set(DW_TRANS_LOC_Y, (int)location.y);
		this.dataManager.set(DW_TRANS_LOC_Z, (int)location.z);
	}

	public void setDropoffLocation(AMVector3 location){
		this.dataManager.set(DW_DROP_LOC_X, (int)location.x);
		this.dataManager.set(DW_DROP_LOC_Y, (int)location.y);
		this.dataManager.set(DW_DROP_LOC_Z, (int)location.z);
	}

	public AMVector3 getSearchLocation(){
		return new AMVector3(this.dataManager.get(DW_TRANS_LOC_X), this.dataManager.get(DW_TRANS_LOC_Y), this.dataManager.get(DW_TRANS_LOC_Z));
	}

	public AMVector3 getDropLocation(){
		return new AMVector3(this.dataManager.get(DW_DROP_LOC_X), this.dataManager.get(DW_DROP_LOC_Y), this.dataManager.get(DW_DROP_LOC_Z));
	}

	public ItemStack getSearchItem(){
		return this.dataManager.get(DW_SEARCH_ITEM).orNull();
	}

	public void setHeldItem(ItemStack item){
		this.dataManager.set(DW_HELD_ITEM, Optional.of(item));
	}

	public void setMimicUser(String userName){
		this.dataManager.set(DW_MIMIC_USER, userName);
	}

	public String getMimicUser(){
		return this.dataManager.get(DW_MIMIC_USER);
	}

	public boolean hasSearchLocation(){
		return !this.getSearchLocation().equals(AMVector3.zero());
	}

	public TileEntityCraftingAltar getAltarTarget(){
		return this.altarTarget;
	}

	public void setAltarTarget(TileEntityCraftingAltar target){
		this.altarTarget = target;
	}

	private void initAI(){
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(2, new EntityAISpellmaking(this));
	}

	@Override
	public ItemStack getHeldItemMainhand(){
		return this.dataManager.get(DW_HELD_ITEM).orNull();
	}

	@Override
	public void onUpdate(){
		super.onUpdate();
		if (!worldObj.isRemote && (altarTarget == null || !altarTarget.isCrafting())){
			this.unSummon();
		}
	}

	@Override
	protected SoundEvent getHurtSound(){
		return null;
	}

	public void unSummon(){
		this.attackEntityFrom(DamageSource.generic, 5000);
	}
}
