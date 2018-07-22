package net.dark_roleplay.medieval.common.world_generation.feature;

import java.util.Random;

import net.dark_roleplay.medieval.common.handler.DRPMedievalBlocks;
import net.dark_roleplay.medieval.common.handler.DRPMedievalConfig;
import net.dark_roleplay.medieval.common.world_generation.noise.SimplexNoise;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.IWorldGenerator;

public class DryClayGenerator implements IWorldGenerator{

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
			case 0: 
				generateOverworld(world, random, chunkX  * 16, chunkZ * 16);
				break;
		}
	}
	
	public void generateOverworld(World world, Random rand, int x, int z) {
		Random rnd = new Random(world.getSeed());
		int xOffset = rnd.nextInt(1000);
		int zOffset = rnd.nextInt(1000);
		float frequency = 750;
		for(int x2 = 1; x2 <= 16; x2++) {
			for(int z2 = 1; z2 <= 16; z2++) {
				int x3 = x + x2;
				int z3 = z + z2;
				
				float chance = SimplexNoise.noise(x3 + xOffset, z3 + zOffset, frequency);
				if(chance > 0.85F) {
					System.out.println("Generated Clay");
					int y = getHeighestOccurence(Blocks.GRASS, world, x3, z3);
					if(y == 0) y = getHeighestOccurence(Blocks.DIRT, world, x3, z3);
					if(y == 0) continue;
					
					for(int i = 0; i < 5; i++) {
						if(y <= 0) { z++; continue;}
						BlockPos pos = new BlockPos(x3, y - i, z3);
						Block b = getBlockToPlace(world, pos);
						if(b == Blocks.AIR)
							continue;
						world.setBlockState(pos, b.getDefaultState());
					}
				}
			}
		}
	}
	
	public Block getBlockToPlace(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		
		if(block != Blocks.DIRT && block != Blocks.GRASS) {
			return Blocks.AIR;
		}else {
			if(world.getBlockState(pos.up()).getBlock() == Blocks.WATER)
				return Blocks.CLAY;
			if(block == Blocks.GRASS)
				return DRPMedievalBlocks.DRY_CLAY_GRASS;
			return DRPMedievalBlocks.DRY_CLAY;
		}
	}
	
	public int getHeighestOccurence(Block block, World world, int x, int z) {
		for(int i = world.getActualHeight(); i > 32; i--) {
			if(world.getBlockState(new BlockPos(x, i, z)).getBlock() == block)
				return i;
		}
		return 0;
	}
}
