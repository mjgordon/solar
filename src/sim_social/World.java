package sim_social;

import java.util.ArrayList;
import java.util.LinkedList;

import sim_spatial.SolarSystem;

public class World {
	public ArrayList<Actor> actors;
	
	public static LinkedList<String> log = new LinkedList<String>();
	
	public SolarSystem solarSystem;
	
	public int time = 0;
	
	public World() {
		solarSystem = new SolarSystem();
		solarSystem.loadDemo();
		
		this.actors = new ArrayList<Actor>();
	}
	
	public void update() {
		if (actors.size() < 100) {
			if (Math.random() < 0.01) {
				actors.add(new Actor(solarSystem.getSolarBodyByName("Luna")));
			}
		}
		
		boolean removeFlag = true;
		for (Actor actor : actors) {
			actor.update(solarSystem);
			if (actor.dead) {
				removeFlag = true;
			}
		}
		
		if (removeFlag) {
			ArrayList<Actor> actorsNew = new ArrayList<Actor>();
			for (Actor actor : actors) {
				if (!actor.dead) {
					actorsNew.add(actor);
				}
			}
			actors = actorsNew;
		}
		
		
		
		solarSystem.update(time);
		
		time += 1;
		
	}
	
	public static void logString(String s) {
		log.addFirst(s);
		
		if (log.size() > 20) {
			log.removeLast();
		}
	}
}
