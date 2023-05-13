package sim;

import java.util.ArrayList;

import org.joml.Vector2d;

import processing.core.PApplet;
import processing.core.PVector;

public class SolarBodyPhysics extends SolarBody {

	public PVector velocity;
		
	public boolean locked = false;
	
	public SolarBodyPhysics(String name, PVector position, PVector velocity, boolean locked) {
		super(name, position, 30, 0xFFFFFFFF);
		
		this.velocity = velocity;
		this.locked = locked;
		
		
		double m1 = 1000;
		double m2 = 10;
		double r = 300;
		
		double v = Math.sqrt(SolarSystem.g * (m1 + m2) / r);
	}
	
	public void update(ArrayList<SolarBodyPhysics> bodies) {
		for (SolarBodyPhysics sb1 : bodies) {
			if (sb1.locked) {
				continue;
			}
			for (SolarBodyPhysics sb2 : bodies) {
				if (sb1.equals(sb2)) {
					continue;
				}
				PVector diff = PVector.sub(sb2.position, sb1.position);
				float r2 = diff.magSq();
				diff.setMag(SolarSystem.g * sb2.mass / r2);
				sb1.velocity.add(diff);
			}
		}
		
		for (SolarBodyPhysics sb : bodies) {
			if (!sb.locked) {
				sb.position.add(sb.velocity);
				
			}
		}
	}
	
}
