package net.wynsolutions.ge.camera;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.wynsolutions.ge.GameEngine;

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
