package sim;

import static solar.Bridge.p;

import processing.core.PGraphics;
import processing.core.PVector;

public class Ship {
	public PVector position;
	
	public PVector velocity = new PVector();
	
	public float heading = p.random(0, p.TWO_PI);
	
	public boolean toRemove = false;
	
	public Ship(PVector position) {
		this.position = position;
	}
	
	public void update(SolarSystem system) {
		for (SolarBodyAuto body : system.head.getArray()) {
			PVector diff = PVector.sub(body.position, position);
			
			if (body.destructive && diff.mag() < body.radius) {
				toRemove = true;
			}
			else {
				float r2 = diff.magSq();
				diff.setMag(SolarSystem.g * body.mass / r2);
				velocity.add(diff);	
			}
		}
		
		position.add(velocity);
	}
	
	public void draw(PGraphics g) {
		g.pushMatrix();
		g.stroke(255);
		g.translate(position.x, position.y);
		g.rotate(heading);
		
		g.line(-5,-4,5,-4);
		g.line(0, -10, 0, 10);
		
		g.noStroke();
		g.fill(255,0,0);
		g.ellipse(0, 0, 2, 2);
		
		g.popMatrix();
	}
}
