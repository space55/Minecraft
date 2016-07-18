package entity;

import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;

import client.Start;

public class Player extends Entity implements ActionListener
{
	private BetterCharacterControl player;
	private static Material mat;

	public static Vector3f gravity = new Vector3f(0.0f, 9.81f, 0.0f);
	private Vector3f startLoc = new Vector3f(0f, 255f, 0f);

	private Vector3f camDir = new Vector3f();
	private Vector3f camLeft = new Vector3f();
	private Vector3f walkDirection = new Vector3f();
	private boolean left = false, right = false, up = false, down = false;

	public int pid;

	private Spatial s;
	private Camera cam;

	public Player()
	{
		cam = Start.app.getCam();
		s = new Geometry("CameraGeometry", new Quad(1, 1));
		mat = new Material(Start.getAM(), "Common/MatDefs/Misc/Unshaded.j3md");
		s.setMaterial(mat);
		s.setLocalTranslation(startLoc);
		cam.setLocation(startLoc);
		// System.out.println(s.toString());
	}

	public Vector3f getLoc()
	{
		return null;
	}

	public void update()
	{

	}

	public void onLoop()
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
			walkDirection = new Vector3f(cam.getDirection().getX(), 0, cam.getDirection().getZ());
		}
		if (down)
		{
			walkDirection = new Vector3f(-cam.getDirection().getX(), 0, -cam.getDirection().getZ());
		}
		player.setWalkDirection(walkDirection);
		// cam.setLocation(player.s);
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

	public void setUpKeys()
	{
		Start.app.getim().addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
		Start.app.getim().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
		Start.app.getim().addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
		Start.app.getim().addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
		Start.app.getim().addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
		Start.app.getim().deleteMapping("FLYCAM_UP");
		Start.app.getim().deleteMapping("FLYCAM_DOWN");
		Start.app.getim().addListener(this, "Left");
		Start.app.getim().addListener(this, "Right");
		Start.app.getim().addListener(this, "Up");
		Start.app.getim().addListener(this, "Down");
		Start.app.getim().addListener(this, "Jump");
	}

	public void kill()
	{

	}

	public void createNew()
	{
		Start.getRN().attachChild(s);
		player = new BetterCharacterControl(1, 2, 100);
		player.setGravity(gravity);
		s.addControl(player);
		setUpKeys();
	}

}
