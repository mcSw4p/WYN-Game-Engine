package net.wynsolutions.ge;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.JFrame;

import net.wynsolutions.ge.camera.Camera;
import net.wynsolutions.ge.map.DemoMap;
import net.wynsolutions.ge.textures.Texture;

/**
 * Copyright (C) 2017  Sw4p
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Sw4p
 *
 */
public class GameEngine extends JFrame implements Runnable{
	
	/*
	 * This project was created based on the instructions here(http://www.instructables.com/id/Making-a-Basic-3D-Engine-in-Java/)
	 * The idea behind this project is to help make simple Java games by providing an easy to hook into game engine.
	 */
	
	/** Default Serial ID */
	private static final long serialVersionUID = 1L;

	/** Display size, Map, and Pixel map */
	private int dHeight, dWidth;
	private int[][] map = DemoMap.map;
	public int[] pixels;
	
	/** Game Running boolean */
	private boolean running;
	
	/** Game Thread */
	private Thread thread;
	
	/** Display Image */
	private BufferedImage image;
	
	/** List of textures */
	private ArrayList<Texture> textures;	
	
	/** Camera and Screen calculation classes */
	private Camera camera;
	private Screen screen;
	
	/** GameEngine Instance */
	private static GameEngine instance;

	/**
	 * <p>Initialize GameEngine. This will create a new JFrame with your dimensions(@param displayHeight @param displayWidth)
	 *    and render a custom map within the new frame.</p>
	 * 
	 * @param gameMap Custom Game Map
	 * @param displayHeight JFrame Display Height
	 * @param displayWidth JFrame Display Width
	 * @param displayTitle JFrame Display Title
	 * @param resizable JFrame Resizable 
	 * @param gameTextures List of Textures for your Game Map
	 */
	private void init(int[][] gameMap, int displayHeight, int displayWidth, String displayTitle, boolean resizable, ArrayList<Texture> gameTextures) {
		dHeight = displayHeight;
		dWidth = displayWidth;
		textures = gameTextures;
		instance = this;
		
		map = gameMap;
		
		setSize(dWidth, dHeight);
		setResizable(resizable);
		setTitle(displayTitle);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.black);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * <p>Initialize GameEngine. This will create a new JFrame with your dimensions(@param displayHeight @param displayWidth)
	 *    and render a custom map within the new frame.</p>
	 * 
	 * @param gameMap Custom Game Map
	 * @param displayHeight JFrame Display Height
	 * @param displayWidth JFrame Display Width
	 * @param displayTitle JFrame Display Title
	 * @param resizable JFrame Resizable 
	 * @param gameTextures List of Textures for your Game Map
	 */
	public GameEngine(int[][] gameMap, int displayHeight, int displayWidth, String displayTitle, boolean resizable, ArrayList<Texture> gameTextures) {		
		this.init(gameMap, displayHeight, displayWidth, displayTitle, resizable, gameTextures);
	}
	
	/**
	 * <p>Initialize GameEngine. This will create a new JFrame with your dimensions(@param displayHeight @param displayWidth)
	 *    and render a custom map within the new frame.</p>
	 * 
	 * @param gameMap Custom Game Map
	 * @param displayHeight JFrame Display Height
	 * @param displayWidth JFrame Display Width
	 * @param gameTextures List of Textures for your Game Map
	 */
	public GameEngine(int[][] gameMap, int displayHeight, int displayWidth, ArrayList<Texture> gameTextures) {
		this.init(gameMap, displayHeight, displayWidth, "WYN - Game Engine", false, gameTextures);
	}
	
	/**
	 * <p>Initialize GameEngine. This will create a new JFrame with dimensions(640x480) and render the Demo Map within the frame.</p> 
	 */
	public GameEngine() {
		ArrayList<Texture> t = new ArrayList<Texture>();
		t.add(DemoMap.wood);
		t.add(DemoMap.brick);
		t.add(DemoMap.bluestone);
		t.add(DemoMap.stone);
		this.init(DemoMap.map, 480, 640, "WYN - Game Engine", false, t);
	}
	
	/**
	 * <p>Start rendering the game within the JFrame with a custom camera.</p>
	 * @param cam Camera class
	 */
	public synchronized void start(Camera cam) {
		thread = new Thread(this);
		image = new BufferedImage(dWidth, dHeight,BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		
		addKeyListener(cam);
		running = true;
		
		screen = new Screen(map, textures, dWidth, dHeight);
		thread.start();
	}
	
	/**
	 * <p>Start rendering the game within the JFrame.</p>
	 */
	public synchronized void start() {
		camera = new Camera(4.5, 4.5, 1, 0, 0, -.66);
		this.start(camera);
	}
	
	/**
	 * <p>Stop rendering the game within the JFrame.</p>
	 */
	public void stop() {
		running = false;
		try {
			thread.join();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>Render the game image the the JFrame.</p>
	 */
	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		bs.show();
	}
	
	/**
	 * <p>Game Runnable the updates, game time every 60 seconds.</p>
	 */
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;
		double delta = 0;
		requestFocus();
		while(running) {
			long now = System.nanoTime();
			delta = delta + ((now-lastTime) / ns);
			lastTime = now;
			while (delta >= 1){
				screen.update(camera, pixels);
				camera.update(map);
				delta--;
			}
			render();
		}
	}
	
	/*
	 * This is just a testing method to test the GameEngine while debugging.
	 */
	public static void main(String[] args) {
		new GameEngine().start();
	}

	/**
	 * <p>Returns the Display Height.</p>
	 * 
	 * @return dHeight
	 */
	public int getdHeight() {
		return dHeight;
	}

	/**
	 * <p>Set the Display Width.</p>
	 * 
	 * @param dHeight the dHeight to set
	 */
	public void setdHeight(int dHeight) {
		this.dHeight = dHeight;
	}

	/**
	 * <p>Returns the Display Width.</p>
	 * 
	 * @return the dWidth
	 */
	public int getdWidth() {
		return dWidth;
	}

	/**
	 * <p>Sets the Display Width.</p>
	 * 
	 * @param dWidth the dWidth to set
	 */
	public void setdWidth(int dWidth) {
		this.dWidth = dWidth;
	}

	/**
	 * <p>Returns the Game map.</p>
	 * 
	 * @return the map
	 */
	public int[][] getMap() {
		return map;
	}

	/**
	 * <p>Sets the Game Map.</p>
	 * 
	 * @param map the map to set
	 */
	public void setMap(int[][] map) {
		this.map = map;
	}

	/**
	 * <p>Returns the running boolean.</p>
	 * 
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * <p>Sets weather the game is running.
	 *    * You should use GameEngine.stop() instead!</p>
	 * 
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * <p>Returns the Game Textures list.</p>
	 * 
	 * @return the textures
	 */
	public ArrayList<Texture> getTextures() {
		return textures;
	}

	/**
	 * <p>Sets the Game Textures list.</p>
	 * 
	 * @param textures the textures to set
	 */
	public void setTextures(ArrayList<Texture> textures) {
		this.textures = textures;
	}

	/**
	 * <p>Returns the Screen class.</p>
	 * 
	 * @return the screen
	 */
	public Screen getScreen() {
		return screen;
	}

	/**
	 * <p>Sets the Screen class.</p>
	 * 
	 * @param screen the screen to set
	 */
	public void setScreen(Screen screen) {
		this.screen = screen;
	}

	/**
	 * <p>Returns the Game thread.</p>
	 * 
	 * @return the thread
	 */
	public Thread getThread() {
		return thread;
	}

	/**
	 * <p>Returns the BufferedImage.</p>
	 * 
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * <p>Returns the Game Camera.</p>
	 * 
	 * @return the camera
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * <p>Returns the GameEngine instance.</p>
	 * 
	 * @return the instance
	 */
	public static GameEngine getInstance() {
		return instance;
	}

}
