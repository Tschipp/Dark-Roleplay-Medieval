package net.dark_roleplay.medieval.common.objects.blocks.old;

import static net.dark_roleplay.medieval.common.objects.blocks.BlockProperties.FACING_HORIZONTAL;
import static net.dark_roleplay.medieval.common.objects.blocks.BlockProperties.STAIR_TYPE;

import net.dark_roleplay.library.experimental.blocks.BlockSettings;
import net.dark_roleplay.medieval.common.objects.blocks.BlockProperties.StairType;
import net.dark_roleplay.medieval.common.objects.blocks.blocks.FacedBlock;
import net.dark_roleplay.medieval.common.objects.blocks.tile_entities.TE_Roof;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class NormalRoof extends FacedBlock{

	public NormalRoof (String name, BlockSettings settings) {
		super(name, settings);
		this.setDefaultState(this.getDefaultState().withProperty(STAIR_TYPE, StairType.STRAIGHT));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING_HORIZONTAL, STAIR_TYPE});
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos){
		EnumFacing facing = state.getValue(FACING_HORIZONTAL);
		IBlockState otherBlock = world.getBlockState(pos.offset(facing));
		IBlockState otherBlock2 = world.getBlockState(pos.offset(facing.getOpposite()));
		if(otherBlock.getBlock() == this && otherBlock.getValue(FACING_HORIZONTAL) == facing.rotateY()) {
	        return state.withProperty(STAIR_TYPE, StairType.INNER_LEFT);
		}else if(otherBlock.getBlock() == this && otherBlock.getValue(FACING_HORIZONTAL) == facing.rotateYCCW()) {
	        return state.withProperty(STAIR_TYPE, StairType.INNER_RIGHT);
		}else if(otherBlock2.getBlock() == this && otherBlock2.getValue(FACING_HORIZONTAL) == facing.rotateY()) {
	        return state.withProperty(STAIR_TYPE, StairType.OUTER_LEFT);
		}else if(otherBlock2.getBlock() == this && otherBlock2.getValue(FACING_HORIZONTAL) == facing.rotateYCCW()) {
	        return state.withProperty(STAIR_TYPE, StairType.OUTER_RIGHT);
		}

        return state;
    }

	@Override
	public boolean hasTileEntity(IBlockState state){
        return true;
    }

	@Override
	public TileEntity createTileEntity(World world, IBlockState state){
		return new TE_Roof();
    }

//    @SideOnly(Side.CLIENT)
//    @Override
//    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
//        IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
//        Block block = iblockstate.getBlock();
//
//        if (this == Blocks.GLASS || this == Blocks.STAINED_GLASS)
//        {
//            if (blockState != iblockstate)
//            {
//                return true;
//            }
//
//            if (block == this)
//            {
//                return false;
//            }
//        }
//
//        return !this.ignoreSimilarity && block == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
//    }
}
