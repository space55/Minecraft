package client;

import java.util.ArrayList;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import bknd.ChunkReaderThread;
import bknd.GenerationThread;
import entity.Player;
import jme3tools.optimize.GeometryBatchFactory;
import world.TerrainGenerator;
import world.World;

public class Start extends SimpleApplication
{
	public static Start app;

	private static Node world;
	private static ArrayList<Spatial> toAttach = new ArrayList<Spatial>();

	private static BulletAppState bulletAppState;

	private static Player player;

	private static boolean shouldCheckForSaved = false;

	private static int fps_max = 32;
	private static long mspf = 1000 / fps_max;
	long time = 0;

	public static void main(String[] args)
	{
		app = new Start();
		app.start();
	}

	public static AssetManager getAM()
	{
		return app.assetManager;
	}

	public static Node getRN()
	{
		return app.rootNode;
	}

	public static Node getWN()
	{
		return world;
	}

	public static void optimizeWorld()
	{
		GeometryBatchFactory.optimize(world);
	}

	public static Thread getThread()
	{
		return Thread.currentThread();
	}

	@Override
	public void simpleInitApp()
	{

		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		player = new Player();
		player.setUpKeys();
		player.createNew();
		flyCam.setMoveSpeed(0);
		// bulletAppState.setDebugEnabled(true);
		// bulletAppState.setThreadingType(BulletAppState.ThreadingType.PARALLEL);

		world = new Node("World");
		rootNode.attachChild(world);

		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
		SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 43.92f, 0.33f, 0.61f);
		fpp.addFilter(ssaoFilter);
		viewPort.addProcessor(fpp);

		DirectionalLight sun = new DirectionalLight();
		sun.setColor(ColorRGBA.White);
		sun.setDirection(new Vector3f(-.5f, -.5f, -.5f).normalizeLocal());
		rootNode.addLight(sun);

		World.init();
		TerrainGenerator.init();
		if (shouldCheckForSaved)
		{
			checkForSaved();
		}
		else
		{
			generate();
		}

		World.saveChunks();

		/* Drop shadows */
		/*
		 * final int SHADOWMAP_SIZE = 1024; DirectionalLightShadowRenderer dlsr
		 * = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE,
		 * 3); dlsr.setLight(sun); viewPort.addProcessor(dlsr);
		 * 
		 * DirectionalLightShadowFilter dlsf = new
		 * DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
		 * dlsf.setLight(sun); dlsf.setEnabled(true); FilterPostProcessor fpp1 =
		 * new FilterPostProcessor(assetManager); fpp1.addFilter(dlsf);
		 * viewPort.addProcessor(fpp1);
		 */
	}

	@Override
	public void simpleUpdate(float tpf)
	{
		player.onLoop();

		long t = System.currentTimeMillis();
		long s = mspf - (t - time);
		// System.out.println(s);
		try
		{
			if (s > 0)
			{
				Thread.sleep(s);
			}
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		time = System.currentTimeMillis();
	}

	public static void checkForSaved()
	{
		if (!ChunkReaderThread.hasSaved())
		{
			generate();
		}
		else
		{
			World.loadChunks();
			World.updateAll();
		}
	}

	public static void generate()
	{
		// World.generate1();
		// World.loadChunks();
		// new Thread(new WorldGenerationThread()).start();
		World.generateInitial();
		// World.render();
		// World.reRenderEdges();

		optimizeWorld();
	}

	public static void generateThreaded()
	{
		Thread t = new Thread(new GenerationThread());
		t.start();
	}

	public static void attach(Spatial spatial)
	{
		toAttach.add(spatial);
	}

	public static BulletAppState getBAS()
	{
		return bulletAppState;
	}

	public Camera getCam()
	{
		return cam;
	}

	public InputManager getim()
	{
		return inputManager;
	}
}