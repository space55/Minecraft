package bknd;

import block.Air;
import block.Block;
import block.Stone;
import world.Chunk;
import world.TerrainGenerator;

public class TerrainGeneratorThread implements Runnable
{
	boolean caves = true;
	boolean terrain = true;
	boolean render = false;

	Chunk parent;
	Block[][][] blocks = new Block[TerrainGenerator.getXS()][TerrainGenerator.getZS()][TerrainGenerator.getYS()];

	int xId;
	int yId;

	public TerrainGeneratorThread(Chunk parent)
	{
		this.parent = parent;
	}

	public void run()
	{
		xId = (int) parent.xId;
		yId = (int) parent.yId;
		generateTerrain();
		// excavateCaves();
		parent.setBlocks(blocks);
		if (render)
		{
			parent.updateAll();
		}
		// Start.getThread().notify();

		parent.generated = true;
	}

	public void generateTerrain()
	{
		boolean temp[][][] = TerrainGenerator.generate(xId, yId);
		// System.out.println(xId + ", " + yId);
		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{
				for (int y = 0; y < 128; y++)
				{
					if (temp[x][z][y])
					{
						blocks[x][z][y] = new Stone((int) (x + xId * 16), (int) (z + yId * 16), y, parent);
					}
					else
					{
						blocks[x][z][y] = new Air((int) (x + xId * 16), (int) (z + yId * 16), y, parent);
					}
				}
			}
			// System.out.println("X " + x);
		}
		System.out.println("Terrain Done (" + xId + ", " + yId + ")");
	}

	public void excavateCaves()
	{
		boolean temp[][][] = TerrainGenerator.excavate(xId, yId);
		// System.out.println(xId + ", " + yId);
		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{
				for (int y = 0; y < 128; y++)
				{
					if (temp[x][z][y])
					{
						blocks[x][z][y] = new Air((int) (x + xId * 16), (int) (z + yId * 16), y, parent);
					}
				}
			}
			// System.out.println("X " + x);
		}
		System.out.println("Excavation Done (" + xId + ", " + yId + ")");
	}

	public void setCaves(boolean caves)
	{
		this.caves = caves;
	}

	public void setTerrain(boolean terrain)
	{
		this.terrain = terrain;
	}

	public void setRender(boolean render)
	{
		this.render = render;
	}

	public void setParent(Chunk parent)
	{
		this.parent = parent;
	}
}
