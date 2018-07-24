package am2.common.armor;

import am2.ArsMagica2;
import am2.api.extensions.IAffinityData;
import am2.api.items.armor.ArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.common.extensions.AffinityData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ArmorEventHandler{

	@SubscribeEvent
	public void onEntityLiving(LivingUpdateEvent event){
		if (!(event.getEntityLiving() instanceof EntityPlayer))
			return;
		if (!event.getEntityLiving().worldObj.isRemote)
			ArmorHelper.HandleArmorInfusion((EntityPlayer) event.getEntityLiving());
		doInfusions(ImbuementApplicationTypes.ON_TICK, event, (EntityPlayer)event.getEntityLiving());
	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event){
		if (!(event.getEntityLiving() instanceof EntityPlayer))
			return;

		doInfusions(ImbuementApplicationTypes.ON_HIT, event, (EntityPlayer)event.getEntityLiving());

		if (event.getEntityLiving() instanceof EntityPlayer)
			doXPInfusion((EntityPlayer)event.getEntityLiving(), 0.01f, Math.max(0.05f, Math.min(event.getAmount(), 5)));
	}
	
	@SubscribeEvent
	public void onEntityJump(LivingJumpEvent event){
		if (!(event.getEntityLiving() instanceof EntityPlayer))
			return;

		doInfusions(ImbuementApplicationTypes.ON_JUMP, event, (EntityPlayer)event.getEntityLiving());
	}

	@SubscribeEvent
	public void onMiningSpeed(BreakSpeed event){
		doInfusions(ImbuementApplicationTypes.ON_MINING_SPEED, event, (EntityPlayer)event.getEntityPlayer());
	}

	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event){
		if (event.getSource().getEntity() instanceof EntityPlayer)
			doXPInfusion((EntityPlayer)event.getSource().getEntity(), 1, Math.min(20, event.getEntityLiving().getMaxHealth()));

		if (!(event.getEntityLiving() instanceof EntityPlayer))
			return;

		doInfusions(ImbuementApplicationTypes.ON_DEATH, event, (EntityPlayer)event.getEntityLiving());
	}

	private void doInfusions(ImbuementApplicationTypes type, Event event, EntityPlayer player){
		IAffinityData props = AffinityData.For(player);
		for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()){
			if (slot.getSlotType() != EntityEquipmentSlot.Type.ARMOR)
				continue;
			ArmorImbuement[] infusions = ArmorHelper.getInfusionsOnArmor(player, slot);
			for (ArmorImbuement inf : infusions){
				if (inf.getApplicationTypes().contains(type)){
					if (inf.canApply(player)){
						if (inf.applyEffect(player, player.worldObj, player.getItemStackFromSlot(slot), type, event)){
							if (inf.getCooldown() > 0){
								if (props.getCooldown(inf.getRegistryName().toString()) < inf.getCooldown()){
									props.addCooldown(inf.getRegistryName().toString(), inf.getCooldown());
									if (player instanceof EntityPlayerMP)
										ArsMagica2.proxy.blackoutArmorPiece((EntityPlayerMP)player, slot, inf.getCooldown());
								}
							}
						}
					}
				}
			}
		}
	}

	private void doXPInfusion(EntityPlayer player, float xpMin, float xpMax){
		float amt = (float)((player.worldObj.rand.nextFloat() * xpMin + (xpMax - xpMin)) * ArsMagica2.config.getArmorXPInfusionFactor());
		ArmorHelper.addXPToArmor(amt, player);
	}

}
