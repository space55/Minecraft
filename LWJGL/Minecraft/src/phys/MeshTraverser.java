package phys;

import java.util.ArrayList;
import java.util.List;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;

/**
 * The MeshTraverser will create you a CollidableTraverserElement root item.
 *
 * @author kusi
 * @version 1
 */
public class MeshTraverser
{

	// These are the 3 different standard partitioning types
	private final static int DIFFER_X = 0;
	private final static int DIFFER_Y = 1;
	private final static int DIFFER_Z = 2;

	// This const will tell you how many elements a leaf-mesh will have after
	// processing the mesh
	private final static int MAX_ELEMENTS_IN_FAKE_MESH = 15;

	/**
	 * This method will enable the rotation process by returning the next
	 * partitioning value
	 *
	 * @param differ
	 * @return
	 */
	private static int getNextDiffer(int differ)
	{
		if (differ >= 2)
			return 0;
		return ++differ;
	}

	/**
	 * Returns the root element of the Traversable Tree created from the initial
	 * Mesh
	 *
	 * @param mainMesh
	 * @return
	 */
	public CollidableTraverserElement getRootElement(Mesh mainMesh)
	{

		int differ = (int) (Math.random() * 2);

		BoundingBox volume = (BoundingBox) mainMesh.getBound();

		CollidableTraverserElement root = new CollidableTraverserElement(volume); // The
																					// root
																					// element

		List<Triangle> mesh = new ArrayList<Triangle>();
		Triangle tri = new Triangle();
		for (int i = 0; i < mainMesh.getTriangleCount(); i++)
		{
			mainMesh.getTriangle(i, tri);
			mesh.add(new Triangle(tri.get1().clone(), tri.get2().clone(), tri.get3().clone()));
		}

		root.addElements(getSubElements(volume, mesh, differ));

		return root;
	}

	/**
	 * Returns the maximum of the lower volume
	 *
	 * @param max
	 * @param min
	 * @param differ
	 * @return
	 */
	private Vector3f getMaxMin(Vector3f max, Vector3f min, int differ)
	{
		switch (differ)
		{
		case DIFFER_X:
			return new Vector3f(min.x + (max.x - min.x) / 2, max.y, max.z);
		case DIFFER_Y:
			return new Vector3f(max.x, min.y + (max.y - min.y) / 2, max.z);
		case DIFFER_Z:
			return new Vector3f(max.x, max.y, min.z + (max.z - min.z) / 2);
		}
		return new Vector3f(max.x, max.y, min.z + (max.z - min.z) / 2);
	}

	/**
	 * Returns the minimum of the upper volume
	 *
	 * @param max
	 * @param min
	 * @param differ
	 * @return
	 */
	private Vector3f getMinMax(Vector3f max, Vector3f min, int differ)
	{
		switch (differ)
		{
		case DIFFER_X:
			return new Vector3f(min.x + (max.x - min.x) / 2, min.y, min.z);
		case DIFFER_Y:
			return new Vector3f(min.x, min.y + (max.y - min.y) / 2, min.z);
		case DIFFER_Z:
			return new Vector3f(min.x, min.y, min.z + (max.z - min.z) / 2);
		}
		return new Vector3f(min.x, min.y, min.z + (max.z - min.z) / 2);
	}

	/**
	 * Recursively called method returning the next tree items from any current
	 * fake mesh
	 *
	 * @param volume
	 * @param mesh
	 * @param differ
	 * @return
	 */
	private List<CollidableTraverserElement> getSubElements(BoundingBox volume, List<Triangle> mesh, int differ)
	{

		List<CollidableTraverserElement> traverserChildren = new ArrayList<CollidableTraverserElement>();

		// Calculate min and max for the new Bounding Boxes
		Vector3f max = new Vector3f();
		max = volume.getMax(max);

		Vector3f min = new Vector3f();
		min = volume.getMin(min);

		Vector3f maxMin = getMaxMin(max, min, differ);
		Vector3f minMax = getMinMax(max, min, differ);

		// Create Bounding Boxes
		BoundingBox b1 = new BoundingBox(min, maxMin);
		BoundingBox b2 = new BoundingBox(minMax, max);

		// Create the 2 fake meshes
		List<Triangle> mesh1 = new ArrayList<Triangle>();
		List<Triangle> mesh2 = new ArrayList<Triangle>();

		for (Triangle tri : mesh)
		{
			// add the triangles to the "meshes"
			if (b1.contains(tri.getCenter()))
				mesh1.add(tri);
			else
				mesh2.add(tri);
		}

		// keep on creating new children or stop by adding the mesh
		traverserChildren.add(prepareTraverser(b1, mesh1, differ));
		traverserChildren.add(prepareTraverser(b2, mesh2, differ));

		return traverserChildren;
	}

	/**
	 * This method will either add the elements of the fake mesh and therefore
	 * stop the recursive
	 * invocations creating some kind of traversal leaf, or continue calling the
	 * recursive stuff
	 *
	 * @param volume
	 * @param triangles
	 * @param differ
	 * @return
	 */
	private CollidableTraverserElement prepareTraverser(BoundingBox volume, List<Triangle> triangles, int differ)
	{
		CollidableTraverserElement element = new CollidableTraverserElement(volume);

		if (triangles.size() > MAX_ELEMENTS_IN_FAKE_MESH)
			element.addElements(getSubElements(volume, triangles, getNextDiffer(differ)));
		else
			element.setMesh(triangles);
		return element;
	}
}
