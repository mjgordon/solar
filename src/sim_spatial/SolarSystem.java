package sim_spatial;

import static solar.Bridge.p;

import java.util.ArrayList;

import processing.core.PGraphics;
import processing.core.PVector;
import sim_social.Resource;

@SuppressWarnings("static-access")
public class SolarSystem {

	public SolarBodyAuto solarBodyHead;

	public  ArrayList<Ship> ships;

	public static final float g = 0.000005f;
	//public static final float g = 0.0f;

	public static PVector viewOffset = new PVector(0, 0);
	public static float viewZoom = 100;


	public SolarSystem() {

	}


	public void loadDemo() {

		int earthRotationTime = 100000;

		solarBodyHead = new SolarBodyAuto("Sol", 0xFFFFFF00, 0.1f, 0, 0, false);

		//@formatter:off
		SolarBodyAuto planetMercury = new SolarBodyAuto("Mercury", 0xFFFFAAAA, 0.003f,  earthRotationTime * 0.24f,  0.4f, false);
		SolarBodyAuto planetVenus =   new SolarBodyAuto("Venus",   0xFF0000FF, 0.003f,  earthRotationTime * 0.61f,  0.7f, false);
		SolarBodyAuto planetEarth =   new SolarBodyAuto("Earth",   0xFF00FF00, 0.01f,   earthRotationTime * 1f,     1,    false);
		SolarBodyAuto planetMars =    new SolarBodyAuto("Mars",    0xFFFF0000, 0.005f,  earthRotationTime * 1.88f,  1.5f, false);
		SolarBodyAuto planetJupiter = new SolarBodyAuto("Jupiter", 0xFFFFAA00, 0.1f,    earthRotationTime * 11.87f, 5.2f, false);
		SolarBodyAuto planetSaturn =  new SolarBodyAuto("Saturn",  0xFFAA5500, 0.1f,    earthRotationTime * 29.47f, 9.6f, false);
		SolarBodyAuto planetUranus =  new SolarBodyAuto("Uranus",  0xFF0000AA, 0.05f,   earthRotationTime * 84.07f, 19.2f, false);
		SolarBodyAuto planetNeptune = new SolarBodyAuto("Neptune", 0xFF4400AA, 0.05f,   earthRotationTime * 164.9f, 30f,  false);
		SolarBodyAuto planetPluto =   new SolarBodyAuto("Pluto",   0xFFAAAAAA, 0.0035f, earthRotationTime * 284f,   39.5f, false);
		SolarBodyAuto planetEris =    new SolarBodyAuto("Eris",    0xFFAAAAAA, 0.0035f, earthRotationTime * 558f,   67.7f, false);

		SolarBodyAuto moonLuna = new SolarBodyAuto("Luna", 0xFFDDDDDD, 0.001f, earthRotationTime / 12, 0.05f, true);
		planetEarth.addOrbital(moonLuna);
		
		SolarBodyAuto moonDeimos = new SolarBodyAuto("Deimos", 0xFFDDDDDD, 0.001f, earthRotationTime / 12, 0.05f, true);
		SolarBodyAuto moonPhobos = new SolarBodyAuto("Phobos", 0xFFDDDDDD, 0.001f, earthRotationTime / 12, 0.05f, true);
		moonDeimos.orbitOffset = 0;
		moonPhobos.orbitOffset = p.PI;
		planetMars.addOrbital(moonDeimos);
		planetMars.addOrbital(moonPhobos);
		
		//@formatter:on
		
		planetMercury.setSemiMinorByPercent(2.2f);
		planetVenus.setSemiMinorByPercent(0.002f);
		planetEarth.setSemiMinorByPercent(0.014f);
		planetMars.setSemiMinorByPercent(0.44f);
		planetJupiter.setSemiMinorByPercent(0.12f);
		planetSaturn.setSemiMinorByPercent(0.16f);
		planetUranus.setSemiMinorByPercent(0.11f);
		planetNeptune.setSemiMinorByPercent(0.004f);
		planetPluto.setSemiMinorByPercent(0.2f);
		planetEris.setSemiMinorByPercent(0.3f);

		solarBodyHead.addOrbital(planetMercury);
		solarBodyHead.addOrbital(planetVenus);
		solarBodyHead.addOrbital(planetEarth);
		solarBodyHead.addOrbital(planetMars);
		solarBodyHead.addOrbital(planetJupiter);
		solarBodyHead.addOrbital(planetSaturn);
		solarBodyHead.addOrbital(planetUranus);
		solarBodyHead.addOrbital(planetNeptune);
		solarBodyHead.addOrbital(planetPluto);
		solarBodyHead.addOrbital(planetEris);
		
		moonLuna.marketPrices.put(Resource.FOOD,80);
		moonLuna.marketPrices.put(Resource.METAL,120);
		
		moonPhobos.marketPrices.put(Resource.FOOD,120);
		moonPhobos.marketPrices.put(Resource.METAL,80);

		ships = new ArrayList<Ship>();
		for (int i = 0; i < 50; i++) {
			ships.add(new Ship(new PVector(p.random(-60,60), p.random(-60, 60))));
		}

	}


	public void update(int time) {
		solarBodyHead.update(time);

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


	public void draw(PGraphics g, int t) {
		g.background(0);
		g.translate(g.width / 2, g.height / 2);

		solarBodyHead.draw(g, viewOffset, viewZoom, t);

		for (Ship s : ships) {
			s.draw(g);
		}
		
		PVector lunaPosition = solarBodyHead.getArray().get(4).getGlobalPosition().add(viewOffset).mult(viewZoom);
		PVector deimosPosition = solarBodyHead.getArray().get(6).getGlobalPosition().add(viewOffset).mult(viewZoom);
		
		g.stroke(255);
		g.line(lunaPosition.x, lunaPosition.y, deimosPosition.x, deimosPosition.y);
	}


	public void mouseDragged(int dx, int dy) {
		SolarSystem.viewOffset.x += dx / viewZoom;
		SolarSystem.viewOffset.y += dy / viewZoom;
	}


	public void mouseWheel(float delta) {
		viewZoom *= (1.0 + (delta * -0.1));

		if (viewZoom < 0) {
			viewZoom = 0.1f;
		}
	}
	
	public SolarBodyAuto getSolarBodyByName(String name) {
		return solarBodyHead.getByName(name);
	}
	
	public SolarBodyAuto getSolarBodyNearestDangerous(PVector v) {
		SolarBodyAuto best = null;
		float bestDistance = Float.MAX_VALUE;
		
		for (SolarBodyAuto body : solarBodyHead.getArray()) {
			if (body.landable) {
				continue;
			}
			float dist = v.dist(body.position);
			if (best == null || dist < bestDistance) {
				best = body;
				bestDistance =  dist;
			}
		}
		return best;
	}

}
