package sim_spatial;

import static solar.Bridge.p;

import java.util.ArrayList;
import java.util.HashMap;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import sim_social.Resource;

@SuppressWarnings("static-access")
public class SolarBodyAuto extends SolarBody {
	
	public float orbitTime;
	
	public float orbitRadiusA;
	public float orbitRadiusB;
	
	private ArrayList<SolarBodyAuto> orbitals;
	
	public float orbitOffset = 0;
	
	private SolarBodyAuto parent = null;
	
	public boolean landable;
	
	public HashMap<Resource,Integer> marketPrices;
	
	public SolarBodyAuto(String name, int color, float radius, float orbitTime, float orbitRadiusA, boolean landable) {
		super(name, new PVector(orbitRadiusA,0), radius, color);
		
		this.orbitOffset = p.random(p.TWO_PI);
		
		this.radius = radius;
		this.orbitTime = orbitTime;
		this.orbitRadiusA = orbitRadiusA;
		this.orbitRadiusB = orbitRadiusA;
		
		this.orbitals = new ArrayList<SolarBodyAuto>();
		
		this.landable = landable;
		
		marketPrices = new HashMap<Resource, Integer>();
	}
	
	public void update(int t) {
		if (orbitTime != 0) {
			float n = t / orbitTime * PApplet.TWO_PI + orbitOffset;
			position.set(PApplet.cos(n) * orbitRadiusA, PApplet.sin(n) * orbitRadiusB);	
		}
		
		
		for (SolarBodyAuto body : orbitals) {
			body.update(t);
		}
		
	}
	
	public void draw(PGraphics g, PVector offset, float scale, int t) {
		
		PVector renderPosition = position.copy();
		renderPosition.add(offset);
		renderPosition.mult(scale);
		
		PVector zeroPosition = offset.copy();
		zeroPosition.mult(scale);
		
		g.noFill();
		g.stroke(255,128);
		
		float renderRadiusA = orbitRadiusA * scale;
		float renderRadiusB = orbitRadiusB * scale;
		g.ellipse(zeroPosition.x,zeroPosition.y, renderRadiusA * 2, renderRadiusB * 2);
		
		

		
		
		float renderRadius = radius * scale;
		if (renderRadius < 5) {
			g.noFill();
			g.stroke(color);
			g.ellipse(renderPosition.x,renderPosition.y,10,10);
			g.ellipse(renderPosition.x,renderPosition.y,6,6);
		}
		else {
			g.fill(color);
			g.noStroke();
			g.ellipse(renderPosition.x,renderPosition.y, renderRadius * 2, renderRadius * 2);	
		}
		
		PVector velocityPosition = renderPosition.copy();
		velocityPosition.add(getGlobalVelocity(t).setMag(20));
		g.strokeWeight(3);
		g.stroke(0,255,255);
		g.line(renderPosition.x, renderPosition.y, velocityPosition.x, velocityPosition.y);
		g.strokeWeight(1);
		

		for (SolarBodyAuto body : orbitals) {
			body.draw(g, offset.copy().add(this.position), scale, t);
		}
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

	
	public void setSemiMinorByPercent(float percent) {
		orbitRadiusB = orbitRadiusA - (orbitRadiusA * percent * 0.01f);
	}
	
	public PVector getGlobalPosition() {
		if (this.parent == null) {
			return position.copy();
		}
		else {
			return position.copy().add(parent.getGlobalPosition());
		}
	}
	
	public PVector getGlobalVelocity(int t) {
		if (orbitTime == 0) {
			return new PVector();
		}
		float n = (t + 5) / orbitTime * PApplet.TWO_PI + orbitOffset;
		PVector positionDiff = new PVector(PApplet.cos(n) * orbitRadiusA, PApplet.sin(n) * orbitRadiusB);
		positionDiff.sub(position);
		
		float n2 = t / orbitTime * PApplet.TWO_PI + orbitOffset + (p.PI / 2);
		PVector direction = new PVector(PApplet.cos(n2) * orbitRadiusA, PApplet.sin(n2) * orbitRadiusB);
		
		direction.setMag(positionDiff.mag() / 5);
		
		
		
		if (parent != null) {
			direction.add(parent.getGlobalVelocity(t));
		}
		
		return direction;
	}
	
	public void addOrbital(SolarBodyAuto child) {
		orbitals.add(child);
		child.parent = this;
	}
	
	public SolarBodyAuto getByName(String name) {
		if (name.equals(this.name)) {
			return this;
		}
		else {
			for (SolarBodyAuto orbital : orbitals) {
				SolarBodyAuto response = orbital.getByName(name);
				if (response != null) {
					return response;
				}
			}
			
			return null;
		}
	}
}
