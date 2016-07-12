package client;

import java.util.ArrayList;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import bknd.ChunkReaderThread;
import bknd.GenerationThread;
import jme3tools.optimize.GeometryBatchFactory;
import world.TerrainGenerator;
import world.World;

public class Start extends SimpleApplication implements ActionListener
{
	private static Start app;

	private static Node world;
	private static ArrayList<Spatial> toAttach = new ArrayList<Spatial>();

	private static BulletAppState bulletAppState;
	private static CharacterControl player;
	private static CapsuleCollisionShape capsuleShape;

	private Vector3f walkDirection = new Vector3f();
	private boolean left = false, right = false, up = false, down = false;

	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();

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
		/*
		 * Box b = new Box(1, 1, 1); // create cube shape
		 * Geometry geom = new Geometry("Box", b); // create cube geometry from
		 * the
		 * // shape
		 * Material mat = new Material(assetManager,
		 * "Common/MatDefs/Misc/Unshaded.j3md"); // create
		 * // a
		 * // simple
		 * // material
		 * mat.setColor("Color", ColorRGBA.Blue); // set color of material to
		 * blue
		 * geom.setMaterial(mat); // set the cube's material
		 * rootNode.attachChild(geom); // make the cube appear in the scene
		 */

		bulletAppState = new BulletAppState();
		stateManager.attach(bulletAppState);

		setUpKeys();

		capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
		player = new CharacterControl(capsuleShape, 0.05f);
		player.setJumpSpeed(20);
		player.setFallSpeed(30);
		player.setGravity(30);
		player.setPhysicsLocation(new Vector3f(0, 10, 0));
		bulletAppState.getPhysicsSpace().add(player);

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
		checkForSaved();

		// World.saveChunks();

		/* Drop shadows */
		/*final int SHADOWMAP_SIZE = 1024;
		DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
		dlsr.setLight(sun);
		viewPort.addProcessor(dlsr);
		
		DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
		dlsf.setLight(sun);
		dlsf.setEnabled(true);
		FilterPostProcessor fpp1 = new FilterPostProcessor(assetManager);
		fpp1.addFilter(dlsf);
		viewPort.addProcessor(fpp1);*/

		/*
		 * while (Thread.activeCount() > 4)
		 * {
		 * try
		 * {
		 * Thread.sleep(200);
		 * }
		 * catch (InterruptedException e)
		 * {
		 * // TODO Auto-generated catch block
		 * e.printStackTrace();
		 * }
		 * }
		 */
	}

	@Override
	public void simpleUpdate(float tpf)
	{
		camDir.set(cam.getDirection()).multLocal(0.6f);
		camLeft.set(cam.getLeft()).multLocal(0.4f);
		walkDirection.set(0, 0, 0);
		if (left)
		{
			walkDirection.addLocal(camLeft);
		}
		if (right)
		{
			walkDirection.addLocal(camLeft.negate());
		}
		if (up)
		{
			walkDirection.addLocal(camDir);
		}
		if (down)
		{
			walkDirection.addLocal(camDir.negate());
		}
		player.setWalkDirection(walkDirection);
		cam.setLocation(player.getPhysicsLocation());
	}

	public static void checkForSaved()
	{
		if (!ChunkReaderThread.hasSaved())
		{
			TerrainGenerator.init();
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

	private void setUpKeys()
	{
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(this, "Left");
		inputManager.addListener(this, "Right");
		inputManager.addListener(this, "Up");
		inputManager.addListener(this, "Down");
		inputManager.addListener(this, "Jump");
	}

	public void onAction(String binding, boolean value, float tpf)
	{
		if (binding.equals("Left"))
		{
			if (value)
			{
				left = true;
			}
			else
			{
				left = false;
			}
		}
		else if (binding.equals("Right"))
		{
			if (value)
			{
				right = true;
			}
			else
			{
				right = false;
			}
		}
		else if (binding.equals("Up"))
		{
			if (value)
			{
				up = true;
			}
			else
			{
				up = false;
			}
		}
		else if (binding.equals("Down"))
		{
			if (value)
			{
				down = true;
			}
			else
			{
				down = false;
			}
		}
		else if (binding.equals("Jump"))
		{
			player.jump();
		}
	}
}