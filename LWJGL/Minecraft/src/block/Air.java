package block;

import com.jme3.material.Material;

import world.Chunk;

public class Air extends Block
{
	private int id = 0;

	public Air(int x, int z, int y, Chunk parent)
	{
		super(x, z, y, parent);
	}

	@Override
	public Material getMaterial()
	{
		return null;
	}

	@Override
	public int getId()
	{
		return id;
	}

	@Override
	public boolean isTrans()
	{
		return true;
	}

}
