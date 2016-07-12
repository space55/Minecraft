package world;

import java.util.Random;

import func.PerlinNoise;
import func.SimplexNoise;

public class TerrainGenerator
{
	private static int xS = 16;
	private static int yS = 128;
	private static int zS = 16;

	private static SimplexNoise noise = null;
	private static SimplexNoise caves = null;

	private static final double FEATURE_SIZE = 0.5;

	public static void init()
	{
		Random rand = new Random();
		noise = new SimplexNoise(100, 0.1, rand.nextInt(10000));
		caves = new SimplexNoise(100, 0.1, rand.nextInt(10000));
	}

	public static boolean[][][] generate(double xId, double yId)
	{
		// x, z, y
		boolean[][][] chunk = new boolean[xS][zS][yS];
		// SimplexNoise.noise(xin, yin, zin, win);
		double xOff = xId * 16;
		double yOff = yId * 16;
		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{
				for (int y = 0; y < 128; y++)
				{

					double tx = (x + xOff) / FEATURE_SIZE;
					double ty = (y) / FEATURE_SIZE;
					double tz = (z + yOff) / FEATURE_SIZE;
					// System.out.println(tx + ", " + ty + ", " + tz);
					double n = noise.getNoise(tx, ty, tz);
					// System.out.println(n);
					chunk[x][z][y] = n > 0;

					// chunk[x][z][y] = true;
				}
			}
		}
		return chunk;
	}

	public static boolean[][][] excavate(double xId, double yId)
	{
		// x, z, y
		boolean[][][] chunk = new boolean[xS][zS][yS];
		double xOff = xId * 16;
		double yOff = yId * 16;
		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{
				for (int y = 0; y < 128; y++)
				{
					double tx = (x + xOff) / (FEATURE_SIZE * 2);
					double ty = (y) / (FEATURE_SIZE * 2);
					double tz = (z + yOff) / (FEATURE_SIZE * 2);
					// System.out.println(tx + ", " + ty + ", " + tz);
					double n = Math.abs(caves.getNoise(tx, ty, tz));
					double b = Math.abs(PerlinNoise.noise(tx, ty, tz));
					// System.out.println(n);
					chunk[x][z][y] = n < 0.001 && b < 0.001;
				}
			}
		}

		chunk = makeCircles(chunk);

		return chunk;
	}

	public static boolean[][][] makeCircles(boolean[][][] chunk)
	{
		boolean[][][] caves = new boolean[xS][zS][yS];
		for (int x = 0; x < 16; x++)
		{
			for (int z = 0; z < 16; z++)
			{
				for (int y = 0; y < 128; y++)
				{
					if (chunk[x][z][y] == true)
					{
						// System.out.println("Yep");
						int cs = 4;
						for (int x1 = 0; x1 < cs; x1++)
						{
							for (int z1 = 0; z1 < cs; z1++)
							{
								for (int y1 = 0; y1 < cs; y1++)
								{
									int x2 = x + x1 - (int) (cs / 2);
									int y2 = y + y1 - (int) (cs / 2);
									int z2 = z + z1 - (int) (cs / 2);
									if (x2 >= 0 && y2 >= 0 && z2 >= 0 && x2 < 16 && y2 < 128 && z2 < 16)
									{
										double m = Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
										// System.out.println(m);
										double n = Math.sqrt(Math.pow(z2 - z, 2) + Math.pow(m, 2));
										// System.out.println(m);
										if (n < cs / 2)
										{
											caves[x2][z2][y2] = true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return caves;
	}

	public static int getXS()
	{
		return xS;
	}

	public static int getYS()
	{
		return yS;
	}

	public static int getZS()
	{
		return zS;
	}
}
