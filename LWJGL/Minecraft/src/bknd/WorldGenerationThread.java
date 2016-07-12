/*******************************************************************************
 * Copyright (C) Eamonn Nugent - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Eamonn Nugent <elg.nugent@gmail.com>, 2016
 *******************************************************************************/

package bknd;

import client.Start;
import world.World;

public class WorldGenerationThread implements Runnable
{

	@Override
	public void run()
	{
		World.generateInitial();
		Start.optimizeWorld();
	}

}
