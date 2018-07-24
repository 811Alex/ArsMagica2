package am2.common.blocks;

import java.util.Collection;
import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.oredict.OreDictionary;

public class BlockDesertNova extends BlockAMFlower{

	static HashSet<Block> blockSands = null;

	public BlockDesertNova(){
		super();
	}

	@Override
	public int tickRate(World par1World){
		return 800;
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Desert;
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		if (worldIn.getBlockState(pos.down()).getBlock() == Blocks.SAND){
			return true;
		}
		if (blockSands == null){// sand is defined by Forge, hence only first call will be 'true'
			Collection<ItemStack> itemStackSands = OreDictionary.getOres("sand", false);
			blockSands = new HashSet<Block>(itemStackSands.size());
			for (ItemStack itemStack : itemStackSands){
				Block oreBlock = Block.getBlockFromItem(itemStack.getItem());
				if (oreBlock != Blocks.AIR){
					blockSands.add(oreBlock);
				}
			}
		}
		return blockSands != null && blockSands.contains(worldIn.getBlockState(pos.down()).getBlock());
	}
}
