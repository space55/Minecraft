package block;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;

import client.Start;
import world.Chunk;

public class Stone extends Block
{
	private int id = 1;
	Material mat;

	public Stone(int x, int z, int y, Chunk parent)
	{
		super(x, z, y, parent);

		mat = new Material(Start.getAM(), "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Gray);
	}

	public int getId()
	{
		return id;
	}

	@Override
	public Material getMaterial()
	{
		return mat;
	}

	@Override
	public boolean isTrans()
	{
		return false;
	}
}
