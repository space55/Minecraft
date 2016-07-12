package block;

import world.Chunk;

public class BlockType
{
	public static Block get(int x, int z, int y, int id, Chunk parent)
	{
		if (id == 0)
		{
			return new Air(x, z, y, parent);
		}
		else if (id == 1)
		{
			return new Stone(x, z, y, parent);
		}
		return null;
	}
}
