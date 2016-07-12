package block;

import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

import client.Start;
import world.Chunk;
import world.TerrainGenerator;
import world.World;

public abstract class Block
{
	protected int id;
	protected int x;
	protected int z;
	protected int y;

	private boolean surxp = false;
	private boolean surxm = false;
	private boolean suryp = false;
	private boolean surym = false;
	private boolean surzp = false;
	private boolean surzm = false;

	private int indxp = -1;
	private int indxm = -1;
	private int indyp = -1;
	private int indym = -1;
	private int indzp = -1;
	private int indzm = -1;

	private Geometry xp;
	private Geometry xm;
	private Geometry yp;
	private Geometry ym;
	private Geometry zp;
	private Geometry zm;

	Chunk parent;

	public Block(int x, int z, int y, Chunk parent)
	{
		this.x = x;
		this.z = z;
		this.y = y;
		this.parent = parent;
		id = getId();
	}

	public void render()
	{
		if (getMaterial() == null)
		{
			return;
		}
		detach();
		renderFaces();
	}

	public void update()
	{
		isSurroundedCalc();
		render();
	}

	public void detach()
	{
		/*if (Start.getWN().hasChild(xp))
		{
			Start.getWN().detachChild(xp);
		}
		if (Start.getWN().hasChild(xm))
		{
			Start.getWN().detachChild(xm);
		}
		if (Start.getWN().hasChild(yp))
		{
			Start.getWN().detachChild(yp);
		}
		if (Start.getWN().hasChild(ym))
		{
			Start.getWN().detachChild(ym);
		}
		if (Start.getWN().hasChild(zp))
		{
			Start.getWN().detachChild(zp);
		}
		if (Start.getWN().hasChild(zm))
		{
			Start.getWN().detachChild(zm);
		}*/

		if (indxp != -1)
		{
			Start.getWN().detachChild(xp);
			indxp = -1;
			System.out.println("Detach!");
		}
		if (indxm != -1)
		{
			Start.getWN().detachChild(xm);

			indxm = -1;
		}
		if (indyp != -1)
		{
			Start.getWN().detachChild(yp);
			indyp = -1;
		}
		if (indym != -1)
		{
			Start.getWN().detachChild(ym);
			indym = -1;
		}
		if (indzp != -1)
		{
			Start.getWN().detachChild(zp);
			indzp = -1;
		}
		if (indzm != -1)
		{
			Start.getWN().detachChild(zm);
			indzm = -1;
		}
	}

	public void derender()
	{
		detach();
		xp = null;
		xm = null;
		yp = null;
		ym = null;
		zp = null;
		zm = null;
	}

	public void renderFaces()
	{
		if (getMaterial() != null)
		{
			float rotn = FastMath.PI / 2;
			if (!surxp)
			{
				xp = new Geometry("Quad", new Quad(1, 1));
				xp.setLocalTranslation(new Vector3f((float) (x + 0.5), (float) (y - 0.5), (float) (z + 0.5)));
				xp.setMaterial(getMaterial());
				Quaternion rot = new Quaternion();
				rot.fromAngles(0, rotn, 0);
				xp.setLocalRotation(rot);
				// Start.attach(xp);
				// indxp = Start.getWN().attachChild(xp);
				indzm = parent.getNode().attachChild(xp);
			}
			if (!surxm)
			{
				xm = new Geometry("Quad", new Quad(1, 1));
				xm.setLocalTranslation(new Vector3f((float) (x - 0.5), (float) (y - 0.5), (float) (z - 0.5)));
				xm.setMaterial(getMaterial());
				Quaternion rot = new Quaternion();
				rot.fromAngles(0, -rotn, 0);
				xm.setLocalRotation(rot);
				// Start.attach(xm);
				// indxm = Start.getWN().attachChild(xm);
				indzm = parent.getNode().attachChild(xm);
			}
			if (!suryp)
			{
				yp = new Geometry("Quad", new Quad(1, 1));
				yp.setLocalTranslation(new Vector3f((float) (x - 0.5), (float) (y + 0.5), (float) (z + 0.5)));
				yp.setMaterial(getMaterial());
				Quaternion rot = new Quaternion();
				rot.fromAngles(-rotn, 0, 0);
				yp.setLocalRotation(rot);
				// Start.attach(yp);
				// indyp = Start.getWN().attachChild(yp);
				indzm = parent.getNode().attachChild(yp);
			}
			if (!surym)
			{
				ym = new Geometry("Quad", new Quad(1, 1));
				ym.setLocalTranslation(new Vector3f((float) (x - 0.5), (float) (y - 0.5), (float) (z - 0.5)));
				ym.setMaterial(getMaterial());
				Quaternion rot = new Quaternion();
				rot.fromAngles(rotn, 0, 0);
				ym.setLocalRotation(rot);
				// Start.attach(ym);
				// indym = Start.getWN().attachChild(ym);
				indzm = parent.getNode().attachChild(ym);
			}
			if (!surzp)
			{
				zp = new Geometry("Quad", new Quad(1, 1));
				zp.setLocalTranslation(new Vector3f((float) (x - 0.5), (float) (y - 0.5), (float) (z + 0.5)));
				zp.setMaterial(getMaterial());
				Quaternion rot = new Quaternion();
				rot.fromAngles(0, 0, 0);
				zp.setLocalRotation(rot);
				// Start.attach(zp);
				// indzp = Start.getWN().attachChild(zp);
				indzm = parent.getNode().attachChild(zp);
			}
			if (!surzm)
			{
				zm = new Geometry("Quad", new Quad(1, 1));
				zm.setLocalTranslation(new Vector3f((float) (x + 0.5), (float) (y - 0.5), (float) (z - 0.5)));
				zm.setMaterial(getMaterial());
				Quaternion rot = new Quaternion();
				rot.fromAngles(-(2 * rotn), 0, (2 * rotn));
				zm.setLocalRotation(rot);
				// Start.attach(zm);
				// indzm = Start.getWN().attachChild(zm);
				indzm = parent.getNode().attachChild(zm);
			}
		}
	}

	public void isSurroundedCalc()
	{
		int xS = TerrainGenerator.getXS();
		int zS = TerrainGenerator.getZS();
		int yS = TerrainGenerator.getYS();
		int x = (int) (this.x - parent.xId * 16);
		int z = (int) (this.z - parent.yId * 16);
		surxm = x > 0 && !(parent.getBlock(x - 1, z, y).isTrans());
		surzm = z > 0 && !(parent.getBlock(x, z - 1, y).isTrans());
		surym = y > 0 && !(parent.getBlock(x, z, y - 1).isTrans());
		surxp = x < xS - 1 && !(parent.getBlock(x + 1, z, y).isTrans());
		surzp = z < zS - 1 && !(parent.getBlock(x, z + 1, y).isTrans());
		suryp = y < yS - 1 && !(parent.getBlock(x, z, y + 1).isTrans());
		if (x == 0)
		{
			try
			{
				surxm = !World.getChunk((int) parent.xId - 1, (int) parent.yId).getBlock(xS - 1, z, y).isTrans();
			}
			catch (NullPointerException e)
			{
				// Unknown?
			}
		}
		else if (x == xS - 1)
		{
			try
			{
				surxp = !World.getChunk((int) parent.xId + 1, (int) parent.yId).getBlock(0, z, y).isTrans();
			}
			catch (NullPointerException e)
			{
				// Unknown?
			}
		}
		if (z == 0)
		{
			try
			{
				surzm = !World.getChunk((int) parent.xId, (int) parent.yId - 1).getBlock(x, zS - 1, y).isTrans();
			}
			catch (NullPointerException e)
			{
				// Unknown?
			}
		}
		else if (z == zS - 1)
		{
			try
			{
				surzp = !World.getChunk((int) parent.xId, (int) parent.yId + 1).getBlock(x, 0, y).isTrans();
			}
			catch (NullPointerException e)
			{
				// Unknown?
			}
		}

	}

	public boolean isSurrounded()
	{
		return surxp && surxm && suryp && surym && surzp && surzm;
	}

	public abstract boolean isTrans();

	public abstract Material getMaterial();

	public abstract int getId();
}
