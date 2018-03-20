package am2.api.extensions;

import java.util.concurrent.Callable;

import am2.api.spell.SpellData;
import am2.common.extensions.EntityExtension;
import am2.common.spell.ContingencyType;
import am2.common.utils.NBTUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public interface IEntityExtension {

	boolean hasEnoughtMana(float f);
		
	void setContingency(ContingencyType type, SpellData stack);
	
	ContingencyType getContingencyType();
	
	SpellData getContingencyStack();
	
	double getMarkX();
	
	double getMarkY();
	
	double getMarkZ();
	
	int getMarkDimensionID();
	
	float getCurrentMana();
	
	int getCurrentLevel();
	
	float getCurrentBurnout();
	
	int getCurrentSummons();
	
	float getCurrentXP();
	
	int getHealCooldown();
	
	void lowerHealCooldown(int amount);
	
	void placeHealOnCooldown();
	
	void lowerAffinityHealCooldown(int amount);
	
	int getAffinityHealCooldown();
	
	void placeAffinityHealOnCooldown(boolean full);
	
	float getMaxMana();
	
	float getMaxXP();
	
	float getMaxBurnout();
	
	void setAffinityHealCooldown(int affinityHealCooldown);
	
	void setCurrentBurnout(float currentBurnout);
	
	void setCurrentLevel(int currentLevel);
	
	void setCurrentMana(float currentMana);
	
	void setCurrentSummons(int currentSummons);
	
	void setCurrentXP(float currentXP);
	
	void setHealCooldown(int healCooldown);
	
	void setMarkX(double markX);
	
	void setMarkY(double markY);
	
	void setMarkZ(double markZ);
	
	void setMarkDimensionID(int markDimensionID);
	
	void setMark(double x, double y, double z, int dim);
	
	void setShrunk(boolean shrunk);
	
	boolean isShrunk();

	void setInverted(boolean inverted);

	void setFallProtection(float fallProtection);
	
	boolean isInverted();
	
	void addEntityReference(EntityLivingBase entity);
	
	void init(EntityLivingBase entity);
	
	boolean canHeal();
	
	int getMaxSummons();
	
	boolean shouldUpdate();
	byte[] generateUpdatePacket();
	void handleUpdatePacket(byte[] bytes);
	void forceUpdate();
	
	class Storage implements IStorage<IEntityExtension> {
		
		@Override
		public NBTBase writeNBT(Capability<IEntityExtension> capability, IEntityExtension instance, EnumFacing side) {
			NBTTagCompound compound = new NBTTagCompound();
			NBTTagCompound am2tag = NBTUtils.getAM2Tag(compound);
			am2tag.setFloat("CurrentMana", instance.getCurrentMana());
			am2tag.setInteger("CurrentLevel", instance.getCurrentLevel());
			am2tag.setFloat("CurrentXP", instance.getCurrentXP());
			am2tag.setFloat("CurrentBurnout", instance.getCurrentBurnout());
			am2tag.setInteger("CurrentSummons", instance.getCurrentSummons());
			
			am2tag.setInteger("HealCooldown", instance.getHealCooldown());
			am2tag.setInteger("AffinityHealCooldown", instance.getAffinityHealCooldown());
			
			am2tag.setBoolean("Shrunk", instance.isShrunk());
			am2tag.setBoolean("Inverted", instance.isInverted());
			am2tag.setFloat("FallProtection", instance.getFallProtection());
			
			am2tag.setDouble("MarkX", instance.getMarkX());
			am2tag.setDouble("MarkY", instance.getMarkY());
			am2tag.setDouble("MarkZ", instance.getMarkZ());
			am2tag.setInteger("MarkDimensionId", instance.getMarkDimensionID());
			am2tag.setFloat("TK_Distance", instance.getTKDistance());
			am2tag.setFloat("ManaShielding", instance.getManaShielding());
			NBTTagCompound contingencyTag = NBTUtils.addTag(am2tag, "Contingency");
			if (instance.getContingencyType() != ContingencyType.NULL) {
				contingencyTag.setString("Type", instance.getContingencyType().name().toLowerCase());
				contingencyTag.setTag("Spell", instance.getContingencyStack().writeToNBT(new NBTTagCompound()));
			} else {
				contingencyTag.setString("Type", "null");			
			}
			return compound;
		}
	
		@Override
		public void readNBT(Capability<IEntityExtension> capability, IEntityExtension instance, EnumFacing side, NBTBase nbt) {
			NBTTagCompound am2tag = NBTUtils.getAM2Tag((NBTTagCompound)nbt);
			instance.setCurrentMana(am2tag.getFloat("CurrentMana"));
			instance.setCurrentLevel(am2tag.getInteger("CurrentLevel"));
			instance.setCurrentXP(am2tag.getFloat("CurrentXP"));
			instance.setCurrentBurnout(am2tag.getFloat("CurrentBurnout"));
			instance.setCurrentSummons(am2tag.getInteger("CurrentSummons"));
			
			instance.setHealCooldown(am2tag.getInteger("HealCooldown"));
			instance.setAffinityHealCooldown(am2tag.getInteger("AffinityHealCooldown"));
			
			instance.setShrunk(am2tag.getBoolean("Shrunk"));
			instance.setInverted(am2tag.getBoolean("Inverted"));
			instance.setFallProtection(am2tag.getFloat("FallProtection"));
			
			instance.setMarkX(am2tag.getDouble("MarkX"));
			instance.setMarkY(am2tag.getDouble("MarkY"));
			instance.setMarkZ(am2tag.getDouble("MarkZ"));
			instance.setMarkDimensionID(am2tag.getInteger("MarkDimensionId"));
			
			instance.setTKDistance(am2tag.getFloat("TK_Distance"));
			instance.setManaShielding(am2tag.getFloat("ManaShielding"));
			
			NBTTagCompound contingencyTag = NBTUtils.addTag(am2tag, "Contingency");
			if (!contingencyTag.hasKey("Type") || !contingencyTag.getString("Type").equals("null")) {
				instance.setContingency(ContingencyType.fromName(contingencyTag.getString("Type")), SpellData.readFromNBT(contingencyTag.getCompoundTag("Spell")));
			} else {
				instance.setContingency(ContingencyType.NULL, null);
			}
		}
	}
	
	class Factory implements Callable<IEntityExtension> {

		@Override
		public IEntityExtension call() {
			return new EntityExtension();
		}
		
	}

	boolean addSummon(EntityCreature entityliving);

	boolean getCanHaveMoreSummons();

	void updateManaLink(EntityLivingBase caster);

	void deductMana(float amt);

	void spawnManaLinkParticles();

	boolean removeSummon();

	boolean isManaLinkedTo(EntityLivingBase entity);

	void cleanupManaLinks();

	float getBonusMaxMana();

	float getBonusCurrentMana();

	boolean shouldReverseInput();

	boolean getIsFlipped();

	float getFlipRotation();

	float getPrevFlipRotation();

	float getShrinkPct();

	float getPrevShrinkPct();
	
	void setTKDistance(float newDist);
	
	void addToTKDistance(float toAdd);
	
	float getTKDistance();

	void syncTKDistance();

	float getFallProtection();

	void manaBurnoutTick();

	boolean setMagicLevelWithMana(int level);

	void addMagicXP(float xp);

	void setDisableGravity(boolean b);

	boolean isGravityDisabled();

	Entity getInanimateTarget();

	void setInanimateTarget(Entity ent);

	void setFlipRotation(float rot);

	void setPrevFlipRotation(float rot);

	float getManaShielding();
	
	void setManaShielding(float manaShielding);
}
