package solar;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.MouseEvent;
import sim_social.World;

@SuppressWarnings("static-access")
public class Solar extends PApplet{
	
	int guiWidth = 800;
	
	World world;
	
	PGraphics gWorld;
	
	PGraphics gUI;
	
	public void settings() {
		size(1024 + guiWidth,1024);
	}
	
	public void setup() {
		Bridge.p = this;
		
		gWorld = createGraphics(width - guiWidth,height);
		gUI = createGraphics(guiWidth, height);
		
		world = new World();
		
		frameRate(300);
	}
	
	public void draw() {
		world.update();
		
		background(0);
		
		//gWorld.noSmooth();
		gWorld.beginDraw();
		world.solarSystem.draw(gWorld, world.time);
		gWorld.endDraw();
		
		gUI.beginDraw();
		gUI.background(200);
		gUI.fill(0);
		gUI.text("Offset / Zoom : " + world.solarSystem.viewOffset + " | " + world.solarSystem.viewZoom,10,12);
		
		int yOffset = 0;
		for (String s : World.log) {
			gUI.text(s,10,36 + yOffset);
			yOffset += 12;
		}
		gUI.endDraw();
		
		image(gWorld,0,0);
		image(gUI,width - gUI.width,0);
		
	}
	
	public void keyPressed() {
		world.solarSystem.loadDemo();
	}
	
	public void mouseDragged() {
		int dx = mouseX - pmouseX;
		int dy = mouseY - pmouseY;
		
		world.solarSystem.mouseDragged(dx, dy);
	}
	
	public void mouseWheel(MouseEvent event) {
		world.solarSystem.mouseWheel(event.getCount());
	}
	
	public static void main(String[] args) {
		PApplet.main(new String[] { solar.Solar.class.getName() });
	}

}
