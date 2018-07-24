package am2.common.blocks;

import am2.common.defs.CreativeTabsDefs;
import am2.common.items.ItemBlockSubtypes;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockVinteumTorch extends BlockTorch{

	public BlockVinteumTorch(){
		super();
		setCreativeTab(CreativeTabsDefs.tabAM2Blocks);
	}

	@Override
	public int getLightValue(IBlockState state){
		return 14;
	}

	public BlockVinteumTorch registerAndName(ResourceLocation rl) {
		this.setUnlocalizedName(rl.toString());
		GameRegistry.register(this, rl);
		GameRegistry.register(new ItemBlockSubtypes(this), rl);
		return this;
	}
}
