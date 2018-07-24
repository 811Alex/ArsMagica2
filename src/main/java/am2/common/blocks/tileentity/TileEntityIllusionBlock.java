package am2.common.blocks.tileentity;

import am2.common.defs.BlockDefs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityIllusionBlock extends TileEntity implements ITickable{
	
	private IBlockState mimicBlock;
	
	public TileEntityIllusionBlock() {
		
	}

	@Override
	public void update() {
		BlockPos pos = this.pos.down();
		IBlockState blockBellow = worldObj.getBlockState(pos);
		while (blockBellow.getBlock() == BlockDefs.illusionBlock) {
			pos = pos.down();
			blockBellow = worldObj.getBlockState(pos);
		}
		mimicBlock = blockBellow;
	}
	
	public IBlockState getMimicBlock() {
		return mimicBlock;
	}
}
