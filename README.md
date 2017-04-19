# WYN Game Engine
The WYN Game Engine is a simple Game Engine created in java for creating easy 3D games. You can provide textures and a map layout
and this will do the rest. WYN-GE is licensed under the GNU GPL v3 license, meaning you can place this project inside of your new Game
for ease of use. You still have to supply a copy of our license with your Game.

## Creating a map
```java
public class ExampleMap {

	/** Map */
	public static int[][] map = 
		{ /* This grid can be as big as you want */
			{1,1,1,1,1,1,1,1,2,2,2,2,2,2,2},
			{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
			{1,0,3,3,3,3,3,0,0,0,0,0,0,0,2},
			{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
			{1,0,3,0,0,0,3,0,2,2,2,0,2,2,2},
			{1,0,3,0,0,0,3,0,2,0,0,0,0,0,2},
			{1,0,3,3,0,3,3,0,2,0,0,0,0,0,2},
			{1,0,0,0,0,0,0,0,2,0,0,0,0,0,2},
			{1,1,1,1,1,1,1,1,4,4,4,0,4,4,4},
			{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
			{1,0,0,0,0,0,1,4,0,0,0,0,0,0,4},
			{1,0,0,2,0,0,1,4,0,3,3,3,3,0,4},
			{1,0,0,0,0,0,1,4,0,3,3,3,3,0,4},
			{1,0,0,0,0,0,0,0,0,0,0,0,0,0,4},
			{1,1,1,1,1,1,1,4,4,4,4,4,4,4,4}
		};
	
	/** Map Textures */       /* new Texture(pathToPicture, sizeOfPicture) */
	public static Texture wood = new Texture("src/main/resources/wood.jpg", 64);
	public static Texture brick = new Texture("src/main/resources/redbrick.jpg", 64);
	public static Texture bluestone = new Texture("src/main/resources/bluestone.jpg", 64);
	public static Texture stone = new Texture("src/main/resources/greystone.jpg", 64);
	
        /**
	 * <p>Returns the Textures used in the Map.</p>
	 * @return Textures
	 */
	public static ArrayList<Texture> getTextures(){
		ArrayList<Texture> t = new ArrayList<Texture>();
		t.add(DemoMap.wood);      // # 1
		t.add(DemoMap.brick);     // # 2
		t.add(DemoMap.bluestone); // # 3
		t.add(DemoMap.stone);     // # 4
		return t;
	}
  
}
```

## Starting the Game Engine
```java
public class Main {

  private int displayHeight = 480, displayWidth = 640;
  private String title = "Simple Game Example";
  private boolean resizable = false;
  
  private Camera camera = new Camera(4.5, 4.5, 1, 0, 0, -.66);

  public static void main(String[] args) {
  
    // Method 1 - Default everything
    new GameEngine().start();
    
    // Method 2 - Default Frame settings
    new GameEngine(ExampleMap.map, displayHeight, displayWidth, ExampleMap.getTextures()).start();
    
    // Method 3 - Custom
    new GameEngine(ExampleMap.map, displayHeight, displayWidth, title, resizable, ExampleMap.getTextures()).start();
    
    // Also you can override the default camera with
    new GameEngine().start(camera);
  }
  
}
```

## Example Camera
```java
public class Camera implements KeyListener{
	
	/** Position variables */
	private double xPos, yPos, xDir, yDir, xPlane, yPlane;
	
	/** Movement variables */
	private boolean WALK_LEFT, WALK_RIGHT, WALK_FORWARD, WALK_BACKWARD;
	private double MOVE_SPEED = .08;
	private double ROTATION_SPEED = .045;
	
	public Camera(double x, double y, double xd, double yd, double xp, double yp){	
		xPos = x;
		yPos = y;
		xDir = xd;
		yDir = yd;
		xPlane = xp;
		yPlane = yp;
	}

	public void keyPressed(KeyEvent key) {
		if((key.getKeyCode() == KeyEvent.VK_A))
			WALK_LEFT = true;
		if((key.getKeyCode() == KeyEvent.VK_D))
			WALK_RIGHT = true;
		if((key.getKeyCode() == KeyEvent.VK_W))
			WALK_FORWARD = true;
		if((key.getKeyCode() == KeyEvent.VK_S))
			WALK_BACKWARD = true;
	}

	public void keyReleased(KeyEvent key) {
		if((key.getKeyCode() == KeyEvent.VK_A))
			WALK_LEFT = false;
		if((key.getKeyCode() == KeyEvent.VK_D))
			WALK_RIGHT = false;
		if((key.getKeyCode() == KeyEvent.VK_W))
			WALK_FORWARD = false;
		if((key.getKeyCode() == KeyEvent.VK_S))
			WALK_BACKWARD = false;
		if((key.getKeyCode() == KeyEvent.VK_ESCAPE))
			GameEngine.getInstance().stop();
	}

	public void keyTyped(KeyEvent arg0) {}
	
	public void update(int[][] map) {
		if(WALK_FORWARD) {
			if(map[(int)(xPos + xDir * MOVE_SPEED)][(int)yPos] == 0) {
				xPos += (xDir * MOVE_SPEED);
			}
			if(map[(int)xPos][(int)(yPos + yDir * MOVE_SPEED)] ==0)
				yPos += (yDir * MOVE_SPEED);
		}
		if(WALK_BACKWARD) {
			if(map[(int)(xPos - xDir * MOVE_SPEED)][(int)yPos] == 0)
				xPos -= (xDir  *MOVE_SPEED);
			if(map[(int)xPos][(int)(yPos - yDir * MOVE_SPEED)]==0)
				yPos -= (yDir * MOVE_SPEED);
		}
		if(WALK_RIGHT) {
			double oldxDir = xDir, oldxPlane = xPlane;
			
			xDir = ((xDir * Math.cos(-ROTATION_SPEED)) - (yDir * Math.sin(-ROTATION_SPEED)));
			yDir = ((oldxDir * Math.sin(-ROTATION_SPEED)) + (yDir * Math.cos(-ROTATION_SPEED)));

			xPlane = ((xPlane * Math.cos(-ROTATION_SPEED)) - (yPlane * Math.sin(-ROTATION_SPEED)));
			yPlane = ((oldxPlane * Math.sin(-ROTATION_SPEED)) + (yPlane * Math.cos(-ROTATION_SPEED)));
		}
		if(WALK_LEFT) {
			double oldxDir = xDir, oldxPlane = xPlane;
			
			xDir = ((xDir * Math.cos(ROTATION_SPEED)) - (yDir * Math.sin(ROTATION_SPEED)));
			yDir = ((oldxDir * Math.sin(ROTATION_SPEED)) + (yDir * Math.cos(ROTATION_SPEED)));
			
			xPlane = ((xPlane * Math.cos(ROTATION_SPEED)) - (yPlane * Math.sin(ROTATION_SPEED)));
			yPlane = ((oldxPlane * Math.sin(ROTATION_SPEED)) + (yPlane * Math.cos(ROTATION_SPEED)));
		}
	}
  
  /* The rest of these methods must remain the same in your version */

	/**
	 * <p>Returns the X Pos.</p>
	 * 
	 * @return the xPos
	 */
	public double getxPos() {
		return xPos;
	}

	/**
	 * <p>Returns the Y Pos.</p>
	 * 
	 * @return the yPos
	 */
	public double getyPos() {
		return yPos;
	}

	/**
	 * <p>Returns the X Dir.</p>
	 * 
	 * @return the xDir
	 */
	public double getxDir() {
		return xDir;
	}

	/**
	 * <p>Returns the Y Dir.</p>
	 * 
	 * @return the yDir
	 */
	public double getyDir() {
		return yDir;
	}

	/**
	 * <p>Returns the X Plane.</p>
	 * 
	 * @return the xPlane
	 */
	public double getxPlane() {
		return xPlane;
	}

	/**
	 * <p>Returns the Y Plane.</p>
	 * 
	 * @return the yPlane
	 */
	public double getyPlane() {
		return yPlane;
	}
}
```

## Authors
* **Sw4p** - [mcSw4p](https://github.com/mcSw4p)