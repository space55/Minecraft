package func;

import java.awt.List;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import bknd.ChunkReader;
import bknd.ChunkSaver;
import world.Chunk;

public class NegArray extends List
{
	private static final long serialVersionUID = 4063550945617438408L;

	ArrayList<ArrayList<Chunk>> a; // ++ x+ y+
	ArrayList<ArrayList<Chunk>> b; // +- x+ y-
	ArrayList<ArrayList<Chunk>> c; // -- x- y-
	ArrayList<ArrayList<Chunk>> d; // -+ x- y+

	public NegArray()
	{
		a = new ArrayList<ArrayList<Chunk>>();
		b = new ArrayList<ArrayList<Chunk>>();
		c = new ArrayList<ArrayList<Chunk>>();
		d = new ArrayList<ArrayList<Chunk>>();
	}

	public void add(int x, int y, Chunk chunk)
	{
		int xn = Math.abs(x);
		int yn = Math.abs(y);

		int xa = xn;
		int ya = yn;

		if (x < 0)
		{
			xn--;
			if (y < 0)
			{
				yn--;
				if (c.size() <= xn)
				{
					ensSpace(c, xa);
					c.set(xn, new ArrayList<Chunk>());
				}
				if (c.get(xn).size() <= yn)
				{
					ensSpaceS(c.get(xn), ya);
				}
				c.get(xn).set(yn, chunk);
			}
			else
			{
				if (d.size() <= xn)
				{
					ensSpace(d, xa);
					d.set(xn, new ArrayList<Chunk>());
				}
				if (d.get(xn).size() <= yn)
				{
					ensSpaceS(d.get(xn), ya);
				}
				d.get(xn).set(yn, chunk);
			}
		}
		else
		{
			if (y < 0)
			{
				yn--;
				if (b.size() <= xn)
				{
					ensSpace(b, xa);
					b.set(xn, new ArrayList<Chunk>());
				}
				if (b.get(xn).size() <= yn)
				{
					ensSpaceS(b.get(xn), ya);
				}
				b.get(xn).set(yn, chunk);
			}
			else
			{
				if (a.size() <= xn)
				{
					ensSpace(a, xa);
					a.set(xn, new ArrayList<Chunk>());
				}
				if (a.get(xn).size() <= yn)
				{
					ensSpaceS(a.get(xn), ya);
				}
				a.get(xn).set(yn, chunk);
			}
		}
	}

	public Chunk get(int x, int y)
	{
		try
		{
			int xn = Math.abs(x);
			int yn = Math.abs(y);

			if (x < 0)
			{
				xn--;
				if (y < 0)
				{
					yn--;
					return c.get(xn).get(yn);
				}
				return d.get(xn).get(yn);
			}
			if (y < 0)
			{
				yn--;
				return b.get(xn).get(yn);
			}

			return a.get(xn).get(yn);
		}
		catch (NullPointerException | IndexOutOfBoundsException e)
		{
			return null;
		}
	}

	public void ensSpace(ArrayList<ArrayList<Chunk>> l, int spaces)
	{
		// spaces = Math.abs(spaces);
		l.addAll(Collections.<ArrayList<Chunk>> nCopies(spaces - l.size() + 1, new ArrayList<Chunk>()));
	}

	public void ensSpaceS(ArrayList<Chunk> l, int spaces)
	{
		// spaces = Math.abs(spaces);
		l.addAll(Collections.<Chunk> nCopies(spaces - l.size() + 1, null));
	}

	public void saveAll()
	{
		for (int x = 0; x < a.size(); x++)
		{
			for (int y = 0; y < a.get(x).size(); y++)
			{
				System.out.println("Saving Chunk " + x + ", " + y);
				ChunkSaver.save(get(x, y));
			}
		}
		for (int x = 0; x < b.size(); x++)
		{
			for (int y = 0; y < b.get(x).size(); y++)
			{
				System.out.println("Saving Chunk " + x + ", " + -y);
				ChunkSaver.save(get(x, -y));
			}
		}
		for (int x = 0; x < c.size(); x++)
		{
			for (int y = 0; y < c.get(x).size(); y++)
			{
				System.out.println("Saving Chunk " + -x + ", " + -y);
				ChunkSaver.save(get(-x, -y));
			}
		}
		for (int x = 0; x < d.size(); x++)
		{
			for (int y = 0; y < d.get(x).size(); y++)
			{
				System.out.println("Saving Chunk " + -x + ", " + y);
				ChunkSaver.save(get(-x, y));
			}
		}
	}

	public void readAll()
	{
		File[] fileList = new File("data/").listFiles();
		int leng = fileList.length;
		String[] files = new String[leng];
		int[] chunkX = new int[leng];
		int[] chunkY = new int[leng];

		for (int i = 0; i < leng; i++)
		{
			files[i] = fileList[i].toString();
		}

		for (int i = 0; i < leng; i++)
		{
			int a = files[i].indexOf("c") + 1;
			int b = files[i].indexOf("_");
			// System.out.println(files[i]);
			String c = files[i].substring(a, b);
			int x = Integer.parseInt(c);
			int d = files[i].indexOf("_") + 1;
			int e = files[i].indexOf(".");
			String f = files[i].substring(d, e);
			int y = Integer.parseInt(f);
			chunkX[i] = x;
			chunkY[i] = y;
		}

		for (int i = 0; i < leng; i++)
		{
			add(chunkX[i], chunkY[i], ChunkReader.read(chunkX[i], chunkY[i]));
		}
	}
}
