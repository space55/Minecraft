/*******************************************************************************
 * Copyright (C) Eamonn Nugent - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Eamonn Nugent <elg.nugent@gmail.com>, 2016
 *******************************************************************************/

package bknd;

import world.Chunk;

public class ChunkUpdateThread implements Runnable
{
	Chunk chunk;

	public ChunkUpdateThread(Chunk chunk)
	{
		this.chunk = chunk;
	}

	public void run()
	{
		chunk.updateAll();
	}
}
