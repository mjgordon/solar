package solar;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.MouseEvent;
import sim.SolarSystem;

public class Solar extends PApplet{
	
	int guiWidth = 800;
	
	SolarSystem solarSystem;
	
	PGraphics gWorld;
	
	PGraphics gUI;
	
	public void settings() {
		size(1024 + guiWidth,1024);
	}
	
	public void setup() {
		Bridge.p = this;
		
		gWorld = createGraphics(width - guiWidth,height);
		gUI = createGraphics(guiWidth, height);
		
		solarSystem = new SolarSystem();
		solarSystem.loadDemo();
		
		frameRate(300);
	}
	
	public void draw() {
		solarSystem.update();
		
		background(0);
		
		gWorld.beginDraw();
		solarSystem.draw(gWorld);
		gWorld.endDraw();
		
		gUI.beginDraw();
		gUI.background(200);
		gUI.fill(0);
		gUI.text("Offset / Zoom : " + solarSystem.viewOffset + " | " + solarSystem.viewZoom,10,12);
		gUI.endDraw();
		
		image(gWorld,0,0);
		image(gUI,width - gUI.width,0);
		
	}
	
	public void mouseDragged() {
		int dx = mouseX - pmouseX;
		int dy = mouseY - pmouseY;
		
		solarSystem.mouseDragged(dx, dy);
	}
	
	public void mouseWheel(MouseEvent event) {
		solarSystem.mouseWheel(event.getCount());
	}
	
	public static void main(String[] args) {
		PApplet.main(new String[] { solar.Solar.class.getName() });
	}

}
