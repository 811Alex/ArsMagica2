package am2.common.trackers;

import java.util.ArrayList;
import java.util.HashMap;

import am2.common.defs.BlockDefs;
import am2.common.defs.ItemDefs;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;

public class ItemFrameWatcher{

	private final HashMap<EntityItemFrameComparator, Integer> watchedFrames;
	private final ArrayList<EntityItemFrameComparator> queuedAddFrames;
	private final ArrayList<EntityItemFrameComparator> queuedRemoveFrames;

	private static final int processTime = 800;

	public ItemFrameWatcher(){
		watchedFrames = new HashMap<EntityItemFrameComparator, Integer>();
		queuedAddFrames = new ArrayList<EntityItemFrameComparator>();
		queuedRemoveFrames = new ArrayList<EntityItemFrameComparator>();
	}

	public void checkWatchedFrames(){
		ArrayList<EntityItemFrameComparator> toRemove = new ArrayList<EntityItemFrameComparator>();

		updateQueuedChanges();

		for (EntityItemFrameComparator frameComp : watchedFrames.keySet()){

			Integer time = watchedFrames.get(frameComp);
			if (time == null) time = 0;

			if (frameComp == null || frameComp.frame == null || frameComp.frame.worldObj == null)
				continue;

			if (!frameComp.frame.worldObj.isRemote || time >= processTime)
				toRemove.add(frameComp);

			if (frameIsValid(frameComp.frame)){
				if (!checkFrameRadius(frameComp)){
					toRemove.remove(frameComp);
				}
			}else{
				time++;
				watchedFrames.put(frameComp, time);
			}
		}

		for (EntityItemFrameComparator frame : toRemove){
			stopWatchingFrame(frame.frame);
		}
	}

	private boolean checkFrameRadius(EntityItemFrameComparator frameComp){

		int radius = 3;

		boolean shouldRemove = true;
		Integer time;

		EntityItemFrame frame = frameComp.frame;
		time = watchedFrames.get(frameComp);

		for (int i = -radius; i <= radius; ++i){
			for (int j = -radius; j <= radius; ++j){
				for (int k = -radius; k <= radius; ++k){

					if (frame.worldObj.getBlockState(frame.getPosition().add(i, j, k)).getBlock() == BlockDefs.liquid_essence.getBlock()){
						if (time == null){
							time = 0;
						}
						time++;



						if (time >= processTime){
							if (!frame.worldObj.isRemote){
								frame.setDisplayedItem(new ItemStack(ItemDefs.arcaneCompendium));
								shouldRemove = true;
								break;
							}
						}else{
							shouldRemove = false;
						}
					}
				}
			}
		}

		watchedFrames.put(frameComp, time);
		return shouldRemove;
	}

	private boolean frameIsValid(EntityItemFrame frame){
		return frame != null && !frame.isDead && frame.getDisplayedItem() != null && frame.getDisplayedItem().getItem() instanceof ItemBook;
	}

	private void updateQueuedChanges(){

		//safe copy to avoid CME
		EntityItemFrameComparator[] toAdd = queuedAddFrames.toArray(new EntityItemFrameComparator[queuedAddFrames.size()]);
		queuedAddFrames.clear();

		for (EntityItemFrameComparator comp : toAdd){
			if (comp.frame != null && (comp.frame.getDisplayedItem() == null || comp.frame.getDisplayedItem().getItem() != ItemDefs.arcaneCompendium))
				watchedFrames.put(comp, 0);
		}

		//safe copy to avoid CME, again with queued removes
		EntityItemFrameComparator[] toRemove = queuedRemoveFrames.toArray(new EntityItemFrameComparator[queuedRemoveFrames.size()]);
		queuedRemoveFrames.clear();

		for (EntityItemFrameComparator comp : toRemove){
			Integer time = watchedFrames.get(comp);
			watchedFrames.remove(comp);
		}
	}

	public void startWatchingFrame(EntityItemFrame frame){
		queuedAddFrames.add(new EntityItemFrameComparator(frame));
	}

	public void stopWatchingFrame(EntityItemFrame frame){
		queuedRemoveFrames.add(new EntityItemFrameComparator(frame));
	}

	private class EntityItemFrameComparator{
		private final EntityItemFrame frame;

		public EntityItemFrameComparator(EntityItemFrame frame){
			this.frame = frame;
		}

		@Override
		public boolean equals(Object obj){
			if (frame == null) return false;
			if (obj instanceof EntityItemFrame){
				return ((EntityItemFrame)obj).getEntityId() == frame.getEntityId() && ((EntityItemFrame)obj).worldObj.isRemote == frame.worldObj.isRemote;
			}
			if (obj instanceof EntityItemFrameComparator){
				return ((EntityItemFrameComparator)obj).frame.getEntityId() == frame.getEntityId() && ((EntityItemFrameComparator)obj).frame.worldObj.isRemote == frame.worldObj.isRemote;
			}
			return false;
		}

		@Override
		public int hashCode(){
			if (frame == null || frame.worldObj == null) return 0;
			return frame.getEntityId() + (frame.worldObj.isRemote ? 1 : 2);
		}
	}
}
