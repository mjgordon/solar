package sim;

import static solar.Bridge.p;

import java.util.ArrayList;

import processing.core.PGraphics;
import processing.core.PVector;

public class SolarSystem {
	
	SolarBodyAuto head;
	
	ArrayList<Ship> ships;
	
	
	public static final float g = 0.01f;
	
	public PVector viewOffset = new PVector(0,0);
	public float viewZoom = 1;
	
	public SolarSystem() {
		
	}
	
	public void loadDemo() {
		
		int earthRotationTime = 1000;

		
		head = new SolarBodyAuto("Sol",0xFFFFFF00,0.1f,0,0);
		
		SolarBodyAuto planetMercury = new SolarBodyAuto("Mercury", 0xFF0000FF,0.003f,earthRotationTime * 1,0.4f);
		SolarBodyAuto planetVenus = new SolarBodyAuto("Venus", 0xFF00FF00,0.003f,earthRotationTime * 1,0.7f);
		SolarBodyAuto planetEarth = new SolarBodyAuto("Earth", 0xFFFF0000,0.01f,earthRotationTime * 1,1);
		
		SolarBodyAuto moon1 = new SolarBodyAuto("Luna", 0xFFDDDDDD,10,3000,80);
		
		head.orbitals.add(planetMercury);
		head.orbitals.add(planetVenus);
		head.orbitals.add(planetEarth);
		//planet1.orbitals.add(moon1);
		
		ships = new ArrayList<Ship>();
		for (int i = 0; i < 50; i++) {
			ships.add(new Ship(new PVector(p.random(-500,500),p.random(-500,500))));	
		}
		
	}
	
	public void update() {
		head.update(p.frameCount);
		
		boolean removeFlag = false;
		for (Ship s : ships) {
			s.update(this);
			if (s.toRemove) {
				removeFlag = true;
			}
		}
		
		if (removeFlag) {
			ArrayList<Ship> shipsNew = new ArrayList<Ship>();
			for (Ship s : ships) {
				if (s.toRemove == false) {
					shipsNew.add(s);
				}
			}
			ships = shipsNew;
		}
		
		
	}
	
	public void draw(PGraphics g) {	
		g.background(0);
		g.translate(g.width / 2, g.height / 2);
		g.scale(viewZoom);
		g.translate(viewOffset.x, viewOffset.y);
		
		head.draw(g);
		
		for (Ship s : ships) {
			s.draw(g);
		}
	}
	
	public void mouseDragged(int dx, int dy) {
		this.viewOffset.x += dx / viewZoom;
		this.viewOffset.y += dy / viewZoom;
	}
	
	public void mouseWheel(float delta) {
		viewZoom *= (1.0 + (delta * -0.1));
		
		if (viewZoom < 0) {
			viewZoom = 0.1f;
		}
	}
	
	
}
