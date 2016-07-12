package bknd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import world.Chunk;
import world.TerrainGenerator;

public class ChunkSaver
{
	static BufferedWriter bw = null;

	public static void save(Chunk chunk)
	{
		try
		{
			String a = "" + (int) chunk.xId;
			String b = "" + (int) chunk.yId;
			bw = new BufferedWriter(new FileWriter("data/c" + a + "_" + b + ".sp"));
		}
		catch (IOException e)
		{
			File dir = new File("data/");
			dir.mkdir();
			save(chunk);
			return;
		}

		write("xId: " + (int) chunk.xId + "\n");
		write("yId: " + (int) chunk.yId + "\n");

		for (int x = 0; x < TerrainGenerator.getXS(); x++)
		{
			write("x: " + x + "\n");
			for (int z = 0; z < TerrainGenerator.getZS(); z++)
			{
				write("z: " + z + "\n");
				for (int y = 0; y < TerrainGenerator.getYS(); y++)
				{
					write(chunk.getBlock(x, z, y).getId() + "");
					if (y != TerrainGenerator.getYS() - 1)
					{
						write(",");
					}
				}
				write("\n");
			}
		}

		bw = null;
	}

	public static void write(String str)
	{
		try
		{
			bw.write(str);
			bw.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
