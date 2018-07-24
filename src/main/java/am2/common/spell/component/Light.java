package am2.common.spell.component;

import java.util.EnumSet;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import am2.api.affinity.Affinity;
import am2.api.blocks.IMultiblock;
import am2.api.blocks.IMultiblockGroup;
import am2.api.blocks.Multiblock;
import am2.api.blocks.MultiblockGroup;
import am2.api.power.IPowerNode;
import am2.api.rituals.IRitualInteraction;
import am2.api.rituals.RitualShapeHelper;
import am2.api.spell.Operation;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellData;
import am2.api.spell.SpellModifiers;
import am2.common.blocks.BlockMageLight;
import am2.common.buffs.BuffEffectIllumination;
import am2.common.defs.BlockDefs;
import am2.common.defs.ItemDefs;
import am2.common.defs.PotionEffectsDefs;
import am2.common.items.ItemOre;
import am2.common.power.PowerNodeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Light extends SpellComponent implements IRitualInteraction{

	@Override
	public boolean applyEffectBlock(SpellData spell, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		if (world.getBlockState(pos).getBlock().equals(BlockDefs.obelisk)){
			if (RitualShapeHelper.instance.matchesRitual(this, world, pos)){
				if (!world.isRemote){
					RitualShapeHelper.instance.consumeReagents(this, world, pos);
					RitualShapeHelper.instance.consumeShape(this, world, pos);
					world.setBlockState(pos, BlockDefs.celestialPrism.getDefaultState());
					PowerNodeRegistry.For(world).registerPowerNode((IPowerNode<?>)world.getTileEntity(pos));
				}else{

				}

				return true;
			}
		}

		if (world.getBlockState(pos).getBlock() == Blocks.AIR) blockFace = null;
		if (blockFace != null){
			pos = pos.offset(blockFace);
		}

		if (world.getBlockState(pos).getBlock() != Blocks.AIR)
			return false;

		if (!world.isRemote){
			world.setBlockState(pos, BlockDefs.blockMageLight.getDefaultState().withProperty(BlockMageLight.COLOR, EnumDyeColor.byMetadata(getColor(spell))));
		}

		return true;
	}
	
	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.COLOR);
	}
	
	private int getColor(SpellData spell) {
		int dye_color_num = 15;
		if (spell.isModifierPresent(SpellModifiers.COLOR)) {
			dye_color_num = spell.getStoredData().getInteger("Color");
		}
		return 15 - dye_color_num;
	}

	@Override
	public boolean applyEffectEntity(SpellData spell, World world, EntityLivingBase caster, Entity target){
		if (target instanceof EntityLivingBase){
			int duration = (int) spell.getModifiedValue(PotionEffectsDefs.DEFAULT_BUFF_DURATION, SpellModifiers.DURATION, Operation.MULTIPLY, world, caster, target);
			//duration = SpellUtils.modifyDurationBasedOnArmor(caster, duration);
			if (!world.isRemote)
				((EntityLivingBase)target).addPotionEffect(new BuffEffectIllumination(duration, spell.getModifierCount(SpellModifiers.BUFF_POWER)));
			return true;
		}
		return false;
	}

	@Override
	public float manaCost(){
		return 50;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

    @Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.NONE);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.WHITE.getDyeDamage()),
				BlockDefs.cerublossom,
				Blocks.TORCH,
				BlockDefs.vinteumTorch
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}

	@Override
	public IMultiblock getRitualShape(){
		Multiblock newDef = new Multiblock("celestialPurification");
		newDef.groups = Lists.newArrayList(RitualShapeHelper.instance.purification.getMultiblockGroups());
		IMultiblockGroup obelisk = new MultiblockGroup("obelisk", Lists.newArrayList(BlockDefs.obelisk.getDefaultState()), true);
		obelisk.addBlock(new BlockPos (0, 0, 0));
		newDef.addGroup(obelisk);
		return newDef;
	}

	@Override
	public ItemStack[] getRitualReagents(){
		return new ItemStack[]{
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_MOONSTONE),
				new ItemStack(ItemDefs.manaFocus)
		};
	}

	@Override
	public int getRitualReagentSearchRadius(){
		return 3;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {}

}
