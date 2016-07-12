/*******************************************************************************
 * Copyright (C) Eamonn Nugent - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Eamonn Nugent <elg.nugent@gmail.com>, 2016
 *******************************************************************************/

package bknd;

import java.io.File;

import func.NegArray;

public class ChunkReaderThread implements Runnable
{
	private NegArray chunks;

	public ChunkReaderThread(NegArray chunks)
	{
		this.chunks = chunks;
	}

	public void run()
	{
		chunks.readAll();
	}

	public static boolean hasSaved()
	{
		File[] fileList = new File("data/").listFiles();
		return fileList.length > 0;
	}
}
