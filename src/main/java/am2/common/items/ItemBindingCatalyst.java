package am2.common.items;

public class ItemBindingCatalyst extends ItemArsMagica{

	public static final int META_PICK = 0;
	public static final int META_AXE = 1;
	public static final int META_SWORD = 2;
	public static final int META_SHOVEL = 3;
	public static final int META_HOE = 4;
	public static final int META_BOW = 5;
	public static final int META_SHIELD = 6;

	public ItemBindingCatalyst(){
		super();
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}
}
