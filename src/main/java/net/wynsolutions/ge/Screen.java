package net.wynsolutions.ge;

import java.awt.Color;
import java.util.ArrayList;

import net.wynsolutions.ge.camera.Camera;
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
public class Screen {
	
	/** Variables */
	private int[][] map;
	private int  width, height;
	private ArrayList<Texture> textures;
	
	public Screen(int[][] m, ArrayList<Texture> tex, int w, int h) {
		map = m;
		textures = tex;
		width = w;
		height = h;
	}
	
	public int[] update(Camera camera, int[] pixels) {
		for(int n=0; n<pixels.length/2; n++) {
			if(pixels[n] != Color.DARK_GRAY.getRGB()) pixels[n] = Color.DARK_GRAY.getRGB();
		}
		
		for(int i=pixels.length/2; i<pixels.length; i++){
			if(pixels[i] != Color.gray.getRGB()) pixels[i] = Color.gray.getRGB();
		}
	    
	    for(int x=0; x<width; x=x+1) {
	    	
			double cameraX = 2 * x / (double)(width) -1;
		    double rayDirX = (camera.getxDir() + (camera.getxPlane() * cameraX));
		    double rayDirY = (camera.getyDir() + (camera.getyPlane() * cameraX));
		    
		    //Map position
		    int mapX = (int)camera.getxPos();
		    int mapY = (int)camera.getyPos();
		    
		    //length of ray from current position to next x or y-side
		    double sideDistX;
		    double sideDistY;
		    
		    //Length of ray from one side to next in map
		    double deltaDistX = Math.sqrt(1 + (rayDirY*rayDirY) / (rayDirX*rayDirX));
		    double deltaDistY = Math.sqrt(1 + (rayDirX*rayDirX) / (rayDirY*rayDirY));
		    double perpWallDist;
		    
		    //Direction to go in x and y
		    int stepX, stepY;
		    boolean hit = false;//was a wall hit
		    int side=0;//was the wall vertical or horizontal
		    
		    //Figure out the step direction and initial distance to a side
		    if (rayDirX < 0){
		    	stepX = -1;
		    	sideDistX = ((camera.getxPos() - mapX) * deltaDistX);
		    } else{
		    	stepX = 1;
		    	sideDistX = ((mapX + 1.0 - camera.getxPos()) * deltaDistX);
		    }
		    
		    if (rayDirY < 0){
		    	stepY = -1;
		        sideDistY = ((camera.getyPos() - mapY) * deltaDistY);
		    } else{
		    	stepY = 1;
		        sideDistY = ((mapY + 1.0 - camera.getyPos()) * deltaDistY);
		    }
		    
		    //Loop to find where the ray hits a wall
		    while(!hit) {
		    	//Jump to next square
		    	if (sideDistX < sideDistY) {
		    		sideDistX += deltaDistX;
		    		mapX += stepX;
		    		side = 0;
		        }  else {
		        	sideDistY += deltaDistY;
		        	mapY += stepY;
		        	side = 1;
		        }
		    	//Check if ray has hit a wall
		    	//System.out.println(mapX + ", " + mapY + ", " + map[mapX][mapY]);
		    	if(map[mapX][mapY] > 0) hit = true;
		    }
		    
		    //Calculate distance to the point of impact
		    if(side==0){
		    	perpWallDist = Math.abs((mapX - camera.getxPos() + (1 - stepX) / 2) / rayDirX);
		    }else{
		    	perpWallDist = Math.abs((mapY - camera.getyPos() + (1 - stepY) / 2) / rayDirY);	
		    }
		    
		    //Now calculate the height of the wall based on the distance from the camera
		    int lineHeight;
		    
		    if(perpWallDist > 0){
		    	lineHeight = Math.abs((int)(height / perpWallDist));
		    }else{
		    	lineHeight = height;
		    }
		    
		    //calculate lowest and highest pixel to fill in current stripe
		    int drawStart = -lineHeight/2+ height/2;
		    
		    if(drawStart < 0)
		    	drawStart = 0;
		    
		    int drawEnd = lineHeight/2 + height/2;
		    
		    if(drawEnd >= height) 
		    	drawEnd = height - 1;
		    
		    //add a texture
		    int texNum = map[mapX][mapY] - 1;
		    double wallX;//Exact position of where wall was hit
		    
		    if(side==1) {//If its a y-axis wall
		    	wallX = (camera.getxPos() + ((mapY - camera.getyPos() + (1 - stepY) / 2) / rayDirY) * rayDirX);
		    } else {//X-axis wall
		    	wallX = (camera.getyPos() + ((mapX - camera.getxPos() + (1 - stepX) / 2) / rayDirX) * rayDirY);
		    }
		    
		    wallX-=Math.floor(wallX);
		    
		    //x coordinate on the texture
		    int texX = (int)(wallX * (textures.get(texNum).SIZE));
		    if(side == 0 && rayDirX > 0)
		    	texX = textures.get(texNum).SIZE - texX - 1;
		    if(side == 1 && rayDirY < 0)
		    	texX = textures.get(texNum).SIZE - texX - 1;
		    
		    //calculate y coordinate on texture
		    for(int y=drawStart; y<drawEnd; y++) {
		    	int texY = (((y*2 - height + lineHeight) << 6) / lineHeight) / 2;
		    	int color;
		    	if(side==0) color = textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)];
		    	else color = (textures.get(texNum).pixels[texX + (texY * textures.get(texNum).SIZE)]>>1) & 8355711;//Make y sides darker
		    	pixels[x + y*(width)] = color;
		    }
		}
		return pixels;
	}
}

