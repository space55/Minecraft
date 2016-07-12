/*******************************************************************************
 * Copyright (C) Eamonn Nugent - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Eamonn Nugent <elg.nugent@gmail.com>, 2016
 *******************************************************************************/

package bknd;

import func.NegArray;

public class ChunkSaverThread implements Runnable
{
	private NegArray chunks;

	public ChunkSaverThread(NegArray chunks)
	{
		this.chunks = chunks;
	}

	public void run()
	{
		chunks.saveAll();
	}
}
