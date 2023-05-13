package sim;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class SolarBody {
	public String name;
	
	public PVector position;
	
	public float mass;
	
	public float radius;
	
	public int color;
	
	public float massFactor = 10;
	
	
	
	public SolarBody(String name, PVector position, float radius, int color) {
		this.name = name;
		this.position = position;
		
		this.radius = radius;
		this.color = color;
		
		this.mass = PApplet.PI * radius * radius * massFactor;
	}
}
