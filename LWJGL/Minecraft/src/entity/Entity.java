package entity;

import com.jme3.math.Vector3f;

public abstract class Entity
{
	public abstract Vector3f getLoc();

	public abstract void update();

	public abstract void onLoop();

	public abstract void kill();

	public abstract void createNew();
}
