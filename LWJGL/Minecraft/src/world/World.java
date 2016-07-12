package world;

import bknd.ChunkReaderThread;
import bknd.ChunkSaver;
import bknd.ChunkSaverThread;
import client.Start;
import func.NegArray;

public class World
{
	private static NegArray chunks;
	private static int s = 4;

	public static void init()
	{
		if (chunks == null)
		{
			chunks = new NegArray();
		}
	}

	public static void generate(int xId, int yId)
	{
		// x = (x % 2 == 0) ? -(x/2) : x/2;

		createChunk(xId, yId);
	}

	public static void createChunk(int x, int y)
	{
		chunks.add(x, y, new Chunk(x, y));
		chunks.get(x, y).generate();
		System.out.println("Generated " + x + ", " + y);
	}

	public static void createAndRender(int x, int y)
	{
		chunks.add(x, y, new Chunk(x, y));
		chunks.get(x, y).generate(true);
		System.out.println("Generated & Rendered " + x + ", " + y);
	}

	public static void generateInitial()
	{
		for (int x = 0; x < s; x++)
		{
			for (int y = 0; y < s; y++)
			{
				createChunk(x - (int) (s / 2), y - (int) (s / 2));
				// Start.optimizeWorld();
			}
		}

		updateAll();
		System.out.println("All Initial Generation Finished");
	}

	public static void updateAll()
	{
		for (int x = 0; x < s; x++)
		{
			for (int y = 0; y < s; y++)
			{
				chunks.get(x - (int) (s / 2), y - (int) (s / 2)).updateAll();
			}
			Start.optimizeWorld();
		}
	}

	public static void generate1()
	{
		generateInitial();
		chunks.get(0, 0).generate1();
	}

	public static void saveChunk(int x, int y)
	{
		ChunkSaver.save(chunks.get(x, y));
	}

	public static void saveChunks()
	{
		Thread t = new Thread(new ChunkSaverThread(chunks));
		t.start();
	}

	public static void loadChunks()
	{
		new ChunkReaderThread(chunks).run();
	}

	public static Chunk getChunk(int x, int y)
	{
		return chunks.get(x, y);
	}

	public static void reRenderEdges()
	{
		for (int x = 0; x < s; x++)
		{
			for (int y = 0; y < s; y++)
			{
				chunks.get(x - (int) (s / 2), y - (int) (s / 2)).generateAllEdges();
			}
		}
	}

	@Deprecated
	public static void render()
	{
		for (int x = 0; x < 16; x++)
		{
			for (int y = 0; y < 16; y++)
			{
				Chunk temp = chunks.get(x, y);
				if (temp == null)
				{
					chunks.add(x, y, new Chunk(x, y));
				}
				else
				{
					temp.render();
				}
			}
		}
	}
}
