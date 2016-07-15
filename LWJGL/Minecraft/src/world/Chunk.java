package world;

import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import bknd.TerrainGeneratorThread;
import block.Air;
import block.Block;
import block.Stone;
import client.Start;
import jme3tools.optimize.GeometryBatchFactory;

public class Chunk
{
	public double xId;
	public double yId;
	public static int xS = TerrainGenerator.getXS();
	public static int yS = TerrainGenerator.getYS();
	public static int zS = TerrainGenerator.getZS();
	public boolean generated = false;

	Block[][][] blocks = new Block[xS][zS][yS];

	private Node chunk;
	private RigidBodyControl collisionLandscape;
	private Spatial chunkModel;

	public Chunk(double xId, double yId)
	{
		this.xId = xId;
		this.yId = yId;
		chunk = new Node("Chunk_" + xId + "_" + yId);
		Start.getWN().attachChild(chunk);
	}

	public void render()
	{
		for (int x = 0; x < xS; x++)
		{
			for (int z = 0; z < zS; z++)
			{
				for (int y = 0; y < yS; y++)
				{
					blocks[x][z][y].render();
				}
			}
			// Start.optimizeWorld();
		}
	}

	public void generate()
	{
		generate(false);
	}

	public void generate(boolean render)
	{
		TerrainGeneratorThread tgt = new TerrainGeneratorThread(this);
		tgt.setRender(render);
		Thread t = new Thread(tgt);
		t.start();

		try
		{
			t.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		/*if (World.getChunk((int) xId + 1, (int) yId) != null)
		{
			World.getChunk((int) xId + 1, (int) yId).generateEdge(Direction.XP);
		}
		
		if (World.getChunk((int) xId - 1, (int) yId) != null)
		{
			World.getChunk((int) xId - 1, (int) yId).generateEdge(Direction.XM);
		}
		
		if (World.getChunk((int) xId, (int) yId + 1) != null)
		{
			World.getChunk((int) xId, (int) yId + 1).generateEdge(Direction.ZP);
		}
		
		if (World.getChunk((int) xId, (int) yId - 1) != null)
		{
			World.getChunk((int) xId, (int) yId - 1).generateEdge(Direction.ZM);
		}*/
	}

	public void generateAllEdges()
	{
		generateEdge(Direction.YP);
		generateEdge(Direction.YM);
		generateAllHorizEdges();
	}

	public void generateAllHorizEdges()
	{
		generateEdge(Direction.XP);
		generateEdge(Direction.XM);
		generateEdge(Direction.ZP);
		generateEdge(Direction.ZM);
	}

	public void generateEdge(Direction dir)
	{
		switch (dir)
		{
		case XP:
			for (int i = 0; i < zS - 1; i++)
			{
				for (int j = 0; j < yS - 1; j++)
				{
					blocks[xS - 1][i][j].update();
				}
			}
			break;
		case XM:
			for (int i = 0; i < zS - 1; i++)
			{
				for (int j = 0; j < yS - 1; j++)
				{
					blocks[0][i][j].update();
				}
			}
			break;
		case ZP:
			for (int i = 0; i < xS - 1; i++)
			{
				for (int j = 0; j < yS - 1; j++)
				{
					blocks[i][zS - 1][j].update();
				}
			}
			break;
		case ZM:
			for (int i = 0; i < xS - 1; i++)
			{
				for (int j = 0; j < yS - 1; j++)
				{
					blocks[i][0][j].update();
				}
			}
			break;
		default:
			break;
		}
		Start.optimizeWorld();
	}

	/**
	 * Just... Don't touch this...
	 * 
	 * @param blocks
	 *            3D Array of Blocks held by the chunk
	 */
	public void setBlocks(Block[][][] blocks)
	{
		this.blocks = blocks;
	}

	public Block getBlock(int x, int z, int y)
	{
		return blocks[x][z][y];
	}

	public void setBlock(int x, int z, int y, Block block)
	{
		blocks[x][z][y] = block;
	}

	public void updateAll()
	{
		// System.out.println(Thread.currentThread().getId());
		if (blocks[0][0][0] == null)
		{
			throw new NotGeneratedException("Chunk " + xId + ", " + yId + " not generated.");
		}
		for (int x = 0; x < xS; x++)
		{
			for (int z = 0; z < zS; z++)
			{
				for (int y = 0; y < yS; y++)
				{
					blocks[x][z][y].update();
				}
			}
			// System.out.println("XU " + x);
		}
		onUpdate();
		long time = System.currentTimeMillis();
		optimizeChunk();
		Start.optimizeWorld();
		System.out.println(System.currentTimeMillis() - time);

		System.out.println("Updated All for " + xId + ", " + yId);
	}

	private void optimizeChunk()
	{
		GeometryBatchFactory.optimize(chunk);
	}

	public void onUpdate()
	{
		// chunkModel = (Spatial) chunk;
		// chunk.attachChild(chunkModel);
		// CollisionShape sceneShape =
		// CollisionShapeFactory.createMeshShape(chunkModel);
		// collisionLandscape = new RigidBodyControl(sceneShape, 0);
		// chunkModel.addControl(collisionLandscape);
		// Start.getBAS().getPhysicsSpace().add(collisionLandscape);
		chunk.setModelBound(new BoundingBox());
		chunk.updateModelBound();

	}

	public void generate1()
	{
		blocks[0][0][0] = new Stone(0, 0, 0, this);
		blocks[0][0][0].update();
	}

	public boolean isBlock(int x, int z, int y)
	{
		return !blocks[x][z][y].isTrans();
	}

	public Node getNode()
	{
		return chunk;
	}

	@Deprecated
	public void reRenderEdges()
	{
		for (int x = 0; x < 2; x++)
		{
			for (int z = 0; z < 2; z++)
			{
				for (int y = 0; y < yS; y++)
				{
					blocks[(x == 0) ? 0 : xS - 1][(z == 0) ? 0 : zS - 1][y].update();
				}
			}
		}
		Start.optimizeWorld();
	}

	/**
	 * @deprecated No longer used for terrain generation. Now using
	 *             multithreaded workloads for better user-end performance
	 */
	@Deprecated
	public void generateTerrain()
	{
		boolean temp[][][] = TerrainGenerator.generate(xId, yId);
		// System.out.println(xId + ", " + yId);
		for (int x = 0; x < xS; x++)
		{
			for (int z = 0; z < zS; z++)
			{
				for (int y = 0; y < yS; y++)
				{
					if (temp[x][z][y])
					{
						blocks[x][z][y] = new Stone((int) (x + xId * 16), (int) (z + yId * 16), y, this);
					}
					else
					{
						blocks[x][z][y] = new Air((int) (x + xId * 16), (int) (z + yId * 16), y, this);
					}
				}
				// Start.optimizeWorld();
			}
			// System.out.println("X " + x);
		}
		System.out.println("Terrain Done (" + xId + ", " + yId + ")");
	}

	/**
	 * @deprecated No longer used for terrain generation. Now using
	 *             multithreaded workloads for better user-end performance
	 */
	@Deprecated
	public void excavateCaves()
	{
		boolean temp[][][] = TerrainGenerator.excavate(xId, yId);
		// System.out.println(xId + ", " + yId);
		for (int x = 0; x < xS; x++)
		{
			for (int z = 0; z < zS; z++)
			{
				for (int y = 0; y < yS; y++)
				{
					if (temp[x][z][y])
					{
						blocks[x][z][y] = new Air((int) (x + xId * 16), (int) (z + yId * 16), y, this);
					}
				}
				Start.optimizeWorld();
			}
			// System.out.println("X " + x);
		}
		System.out.println("Excavation Done (" + xId + ", " + yId + ")");
	}

	/**
	 * @deprecated This generates using old methods. Now using multithreaded
	 *             terrain generation for faster load times (user time, not
	 *             compute time)
	 *             IT REALLY DOESN'T WORK
	 */
	@Deprecated
	public void generateST()
	{
		generateTerrain();
		excavateCaves();
	}
}
