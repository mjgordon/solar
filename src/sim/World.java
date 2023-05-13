package sim;

import java.util.ArrayList;

public class World {
	public ArrayList<Actor> actors;
	
	public void update() {
		if (actors.size() < 200) {
			if (Math.random() < 0.0001) {
				actors.add(new Actor());
			}
		}
		
	}
}
