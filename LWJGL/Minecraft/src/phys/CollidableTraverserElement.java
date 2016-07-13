package phys;

import java.util.ArrayList;
import java.util.List;

import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Triangle;

/**
 * The CollidableTraverserElement is used to check for collisions inside a Mesh
 * faster than by
 * just iterating over its Triangles.
 *
 * It’s an very simple customizable alternative for the Octree
 *
 * @author kusi
 * @version 1
 */
public class CollidableTraverserElement
{

	// The fake mesh of the element
	private List<Triangle> mesh;

	// The com.jme3.bounding.BoundingVolume, jme3 standard
	BoundingVolume boundingVolume;

	// actually the subtree
	public List<CollidableTraverserElement> elements = new ArrayList<CollidableTraverserElement>();

	/**
	 * Returns the Results of a collision of the Traversable Mesh with any
	 * Collidable object
	 *
	 * @param coll
	 * @return
	 */
	public CollisionResults getResults(Collidable coll)
	{
		CollisionResults results = new CollisionResults();

		CollisionResults check = new CollisionResults();
		boundingVolume.collideWith(coll, check);
		if (check.getClosestCollision() != null)
		{
			if (elements.size() > 0)
			{
				processSubtree(coll, results);
			}
			else
			{
				checkFakeMesh(coll, results);
			}
		}
		return results;
	}

	/**
	 * This method will actually check for collisions inside the fakemesh
	 *
	 * @param coll
	 * @param results
	 */
	private void checkFakeMesh(Collidable coll, CollisionResults results)
	{
		CollisionResults triangleResults = new CollisionResults();
		for (Triangle tri : getMesh())
		{
			coll.collideWith(tri, triangleResults);
			if (triangleResults.getClosestCollision() != null)
				results.addCollision(triangleResults.getClosestCollision());
		}
	}

	/**
	 * Processes the subtree until, as seen in getResults, a fake mesh was found
	 * 
	 * @param coll
	 * @param results
	 */
	private void processSubtree(Collidable coll, CollisionResults results)
	{
		for (CollidableTraverserElement element : elements)
		{
			CollisionResults childResults = element.getResults(coll);
			if (childResults.getClosestCollision() != null)
				results.addCollision(childResults.getClosestCollision());
		}
	}

	// GETTERS AND SETTERS

	public CollidableTraverserElement(BoundingVolume box)
	{
		boundingVolume = box;
	}

	public void setBoundingVolume(BoundingVolume volume)
	{
		boundingVolume = volume;
	}

	public void setMesh(List<Triangle> mesh)
	{
		this.mesh = mesh;
	}

	public List<Triangle> getMesh()
	{
		return mesh;
	}

	public void addElements(List<CollidableTraverserElement> newElements)
	{
		this.elements.addAll(newElements);
	}

}