package am2.common.items;

import am2.api.ArsMagicaAPI;
import am2.common.entity.EntityFlicker;
import net.minecraft.item.ItemStack;

public class ItemFlickerJar extends ItemArsMagica{
	public ItemFlickerJar(){
		super();
		this.setMaxDamage(0);
		setHasSubtypes(true);
	}

	public void setFlickerJarTypeFromFlicker(ItemStack stack, EntityFlicker flick){
		stack.setItemDamage(ArsMagicaAPI.getAffinityRegistry().getId(flick.getFlickerAffinity()));
	}
}
