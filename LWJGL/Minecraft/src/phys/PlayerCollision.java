/*******************************************************************************
 * Copyright (C) Eamonn Nugent - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Eamonn Nugent <elg.nugent@gmail.com>, 2016
 *******************************************************************************/

package phys;

import com.jme3.math.Vector3f;

import client.Start;
import world.World;

public class PlayerCollision
{
	private static final double G = 9.81;

	public static Vector3f gravity = new Vector3f(0.0f, 9.81f, 0.0f);

	public static void check()
	{
		Start.getRN().getchild(Start.getRN().getChildIndex(Start.getPlayer())).
		float px = p.x;
		float pz = p.z;
		float py = p.y;
		if (World.getBlock((int) px, (int) pz, (int) py - 1).getId() != 1)
		{
			Vector3f v = Start.getPlayer().getVelocity();
			v = new Vector3f(v.x, 0, v.y);
			System.out.println("STAHP");
		}
		else
		{
			Start.getPlayer().setGravity(gravity);
		}
	}
}
