package bknd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import block.Block;
import block.BlockType;
import world.Chunk;
import world.TerrainGenerator;

public class ChunkReader
{
	static BufferedReader br = null;

	public static Chunk read(int x, int y)
	{
		System.out.println("Loading Chunk (" + x + ", " + y + ")");
		try
		{
			br = new BufferedReader(new FileReader("data/c" + x + "_" + y + ".sp"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}

		int xId = 0;
		int yId = 0;

		try
		{
			xId = Integer.parseInt(br.readLine().substring(5));
			yId = Integer.parseInt(br.readLine().substring(5));
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}

		Chunk chunk = new Chunk(xId, yId);

		int cx = 0;
		int cz = 0;

		for (int nx = 0; nx < TerrainGenerator.getXS(); nx++)
		{
			try
			{
				cx = Integer.parseInt(br.readLine().substring(3));
			}
			catch (NumberFormatException e1)
			{
				e1.printStackTrace();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}

			for (int nz = 0; nz < TerrainGenerator.getZS(); nz++)
			{
				String temp = "";
				try
				{
					cz = Integer.parseInt(br.readLine().substring(3));
					// System.out.println(cz);

					temp = br.readLine();
				}
				catch (NumberFormatException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}

				for (int cy = 0; cy < TerrainGenerator.getYS(); cy++)
				{
					// System.out.println(temp);
					int p = temp.indexOf(",");
					// System.out.println(temp);
					if (p != -1)
					{
						y = Integer.parseInt(temp.substring(0, p));
					}
					else
					{
						y = Integer.parseInt(temp);
					}
					temp = temp.substring(p + 1);
					Block block = BlockType.get(cz, cz, cy, y, chunk);
					/*if (block == null)
					{
						System.out.println(cx);
						System.out.println(cz);
						System.out.println(cy);
						System.out.println(y);
					}*/
					chunk.setBlock(cx, cz, cy, block);
				}
			}
			// System.out.println("X: " + nx + ", " + cx);
		}

		System.out.println("Chunk Loaded (" + x + ", " + y + ")");

		return chunk;
	}
}
