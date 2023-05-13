package sim;

import static solar.Bridge.p;

import java.util.ArrayList;

import org.joml.Vector2d;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

public class SolarBodyAuto extends SolarBody {
	
	public float orbitTime;
	
	public float orbitRadiusA;
	public float orbitRadiusB;
	
	public boolean destructive = true;
	
	public ArrayList<SolarBodyAuto> orbitals;
	
	public SolarBodyAuto(String name, int color, float radius, float orbitTime, float orbitRadiusA) {
		super(name, new PVector(PApplet.cos(orbitRadiusA),0), radius, color);
		
		this.position.rotate(p.random(p.TWO_PI));
		
		this.radius = radius;
		this.orbitTime = orbitTime;
		this.orbitRadiusA = orbitRadiusA;
		this.orbitRadiusB = orbitRadiusA;
		
		this.orbitals = new ArrayList<SolarBodyAuto>();
	}
	
	public void update(int t) {
		if (orbitTime != 0) {
			float n = t / orbitTime * PApplet.TWO_PI;
			position.set(PApplet.cos(n) * orbitRadiusA, PApplet.sin(n) * orbitRadiusB);	
		}
		
		
		for (SolarBodyAuto body : orbitals) {
			body.update(t);
		}
		
	}
	
	public void draw(PGraphics g) {
		g.pushMatrix();
		
		g.noFill();
		g.stroke(255,128);
		g.ellipse(0,0, (float)orbitRadiusA * 2, (float)orbitRadiusB * 2);
		
		g.translate((float)position.x, (float)position.y);
		
		g.fill(color);
		g.noStroke();
		
		g.ellipse(0,0, (float)radius * 2, (float)radius * 2);
		
		
		
		for (SolarBodyAuto body : orbitals) {
			body.draw(g);
		}
		
		g.popMatrix();
	}
	
	public ArrayList<SolarBodyAuto> getArray() {
		ArrayList<SolarBodyAuto> out = new ArrayList<SolarBodyAuto>();
		
		out.add(this);
		for (SolarBodyAuto body : orbitals) {
			body.getArray(out);
		}
		
		return out;
	}
	
	
	private ArrayList<SolarBodyAuto> getArray(ArrayList<SolarBodyAuto> in) {
		in.add(this);
		for (SolarBodyAuto body : orbitals) {
			body.getArray(in);
		}
		
		return in;
	}

}
