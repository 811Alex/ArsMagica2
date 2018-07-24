package am2.common.blocks.tileentity;

import java.util.ArrayList;
import java.util.Iterator;

import am2.api.math.AMVector3;
import am2.common.defs.BlockDefs;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCrystalMarkerSpellExport extends TileEntityCrystalMarker{
	static final int RESCAN_INTERVAL = 600;
	static final int UPDATE_INTERVAL = 100;

	ArrayList<AMVector3> craftingAltarCache;
	int updateCounter = 0;

	public TileEntityCrystalMarkerSpellExport(){
		this(0);
	}

	public TileEntityCrystalMarkerSpellExport(int type){
		super(type);
		craftingAltarCache = new ArrayList<AMVector3>();
	}
	@Override
	public void update(){
		super.update();
		if (this.updateCounter % RESCAN_INTERVAL == 0){
			scanForCraftingAltars();
		}
		this.updateCounter++;
	}

	private void scanForCraftingAltars(){
		craftingAltarCache.clear();
		for (int i = -10; i <= 10; ++i){
			for (int j = -10; j <= 10; ++j){
				for (int k = -10; k <= 10; ++k){
					if (i == 0 && j == 0 && k == 0)
						continue;

					Block block = this.worldObj.getBlockState(pos.add(i, j, k)).getBlock();
					if (block == BlockDefs.craftingAltar){
						craftingAltarCache.add(new AMVector3(pos.add(i, j, k)));
					}
				}
			}
		}
	}

	private boolean updateFilter(){
		ArrayList<ItemStack> filter = new ArrayList<ItemStack>();
		Iterator<AMVector3> it = this.craftingAltarCache.iterator();
		boolean changed = false;
		while (it.hasNext()){
			TileEntityCraftingAltar altar = getCATE((AMVector3)it.next());
			if (altar == null){
				it.remove();
				continue;
			}
			if (altar.isCrafting()){
				filter.add(altar.getNextPlannedItem());
				changed = true;
			}
		}

		this.filterItems = filter.toArray(new ItemStack[filter.size()]);
		return changed;
	}

	private TileEntityCraftingAltar getCATE(AMVector3 vec){
		TileEntity te = this.worldObj.getTileEntity(vec.toBlockPos());
		if (te != null && te instanceof TileEntityCraftingAltar)
			return (TileEntityCraftingAltar)te;

		return null;
	}
}
