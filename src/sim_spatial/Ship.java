package sim_spatial;

import static solar.Bridge.p;

import processing.core.PGraphics;
import processing.core.PVector;

@SuppressWarnings("static-access")
public class Ship {
	public PVector position;
	
	public PVector velocity = new PVector();
	
	public float heading = p.random(0, p.TWO_PI);
	
	public boolean toRemove = false;
	
	public boolean isAccel = false;
	
	public Ship(PVector position) {
		this.position = position;
	}
	
	public void update(SolarSystem system) {
		for (SolarBodyAuto body : system.solarBodyHead.getArray()) {
			PVector diff = PVector.sub(body.position, position);
			
			if (!body.landable && diff.mag() < body.radius) {
				toRemove = true;
			}
			else {
				diff.setMag(getGravity(body));
				velocity.add(diff);	
			}
		}
		
		position.add(velocity);
	}
	
	public float getGravity(SolarBodyAuto body) {
		PVector diff = PVector.sub(body.position, position);
		
		float r2 = diff.magSq();
		return SolarSystem.g * body.mass / r2;
	}
	
	public void draw(PGraphics g) {
		
		g.stroke(255);
		
		PVector renderPosition = position.copy();
		renderPosition.add(SolarSystem.viewOffset);
		renderPosition.mult(SolarSystem.viewZoom);
		

		float x = renderPosition.x + (p.cos(heading) * 10);
		float y = renderPosition.y + (p.sin(heading) * 10);
		g.line(renderPosition.x, renderPosition.y, x, y);
		
		PVector velocityPosition = PVector.mult(velocity, 10);
		velocityPosition.add(position);
		velocityPosition.add(SolarSystem.viewOffset);
		velocityPosition.mult(SolarSystem.viewZoom);
		g.stroke(0,255,255);
		g.line(renderPosition.x, renderPosition.y, velocityPosition.x, velocityPosition.y);
		
		
		g.noStroke();
		if (isAccel) {
			g.fill(255,0,0);	
		}
		else {
			g.fill(255);
		}
		
		g.ellipse(renderPosition.x + 0,renderPosition.y +  0, 4, 4);
		
		
	}
}
