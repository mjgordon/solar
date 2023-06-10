package sim_social;

import java.util.LinkedList;

import processing.core.PVector;
import sim_social.goal.*;

import static solar.Bridge.p;

import sim_spatial.Ship;
import sim_spatial.SolarBodyAuto;
import sim_spatial.SolarSystem;

@SuppressWarnings("static-access")
public class Actor {
	/**
	 * Current planet, moon, station etc the actor is on. If null, they are in space
	 */
	public SolarBodyAuto location;

	Ship ship = null;

	public long money;

	public Job job;

	public LinkedList<Goal> goals;

	public String name = "";

	public static int actorCounter = 0;

	public boolean dead = false;


	public Actor(SolarBodyAuto location) {
		this.location = location;
		money = 1000000;
		money = (long) p.random(900000, 1100000);
		job = Job.TRADER;

		goals = new LinkedList<Goal>();

		name = "#" + p.nf(actorCounter, 8);
		actorCounter += 1;

		World.logString("Actor " + name + " created with job " + job.toString().toLowerCase());
	}


	public void update(SolarSystem solarSystem) {
		if (goals.size() == 0) {
			updateFindGoal(solarSystem);
		}
		else {
			updateDoGoal(solarSystem);
		}
	}


	private void updateDoGoal(SolarSystem solarSystem) {
		Goal goal = goals.getFirst();

		if (goal instanceof GoalBuyShip) {
			if (money > 10000) {
				money -= 10000;
				ship = new Ship(location.getGlobalPosition());
				World.logString("Actor " + name + " buys a ship on " + location.name);
				goals.removeFirst();
			}
		}

		else if (goal instanceof GoalExchangeResource) {
			GoalExchangeResource goalExchange = (GoalExchangeResource) goal;
			// Buy
			if (goalExchange.amount > 0) {
				money -= location.marketPrices.get(goalExchange.resource) * goalExchange.amount;
				goals.removeFirst();
			}
			// Sell
			else {
				money += location.marketPrices.get(goalExchange.resource) * -goalExchange.amount;
				goals.removeFirst();
			}

		}

		else if (goal instanceof GoalNavigate) {
			GoalNavigate goalNavigate = (GoalNavigate) goal;

			if (location != null) {
				ship.position = location.getGlobalPosition();
				solarSystem.ships.add(ship);
				World.logString("Actor " + name + " launches from " + location.name);
				location = null;
			}

			else {
				if (ship != null && ship.toRemove) {
					dead = true;
				}
				else {
					SolarBodyAuto nearestBody = solarSystem.getSolarBodyNearestDangerous(ship.position);
					PVector destinationVector = PVector.sub(goalNavigate.destination.getGlobalPosition(), ship.position);
					
					PVector goalVector = destinationVector.copy();
					goalVector.setMag(1);
					PVector avoidVector = PVector.sub(ship.position, nearestBody.getGlobalPosition());
					avoidVector.setMag(0.3f);

					goalVector.add(avoidVector);

					PVector headingVector = new PVector(p.cos(ship.heading), p.sin(ship.heading));

					float crossZ = PVector.cross(headingVector, goalVector, new PVector()).z;

					float delta = (crossZ / p.abs(crossZ)) * 0.02f;

					ship.heading += delta;

					if (ship.velocity.dot(goalVector) < 0.0005) {
						float accel = 0.000002f;
						ship.velocity.x += p.cos(ship.heading) * accel;
						ship.velocity.y += p.sin(ship.heading) * accel;
						ship.isAccel = true;
					}
					else {
						ship.isAccel = false;
					}
					
					if (destinationVector.mag() < goalNavigate.destination.radius) {
						location = goalNavigate.destination;
						solarSystem.ships.remove(ship);
						goals.removeFirst();
						World.logString("Actor " + name + " lands at " + location.name);
					}
				}
			}
		}
	}


	private void updateFindGoal(SolarSystem solarSystem) {
		switch (job) {
		case TRADER:
			if (ship == null) {
				goals.addFirst(new GoalBuyShip());
				World.logString("Actor " + name + " decides to buy a ship");
			}
			else {
				findDeal(solarSystem);
			}
			break;
		}
	}


	private boolean findDeal(SolarSystem solarSystem) {
		Resource bestLocalResource = null;
		int bestLocalPrice = Integer.MAX_VALUE;

		for (Resource key : location.marketPrices.keySet()) {
			if (bestLocalResource == null || location.marketPrices.get(key) < bestLocalPrice) {
				bestLocalResource = key;
				bestLocalPrice = location.marketPrices.get(key);
			}
		}

		if (bestLocalResource == null) {
			return false;
		}
		else {
			int maxAmount = (int) (money / bestLocalPrice);

			SolarBodyAuto bestLocation = null;
			int bestOtherPrice = 0;

			for (SolarBodyAuto body : solarSystem.solarBodyHead.getArray()) {
				if (body.marketPrices.containsKey(bestLocalResource) == false) {
					continue;
				}
				if (bestLocation == null || body.marketPrices.get(bestLocalResource) > bestOtherPrice) {
					bestLocation = body;

					bestOtherPrice = body.marketPrices.get(bestLocalResource);
				}
			}

			goals.addFirst(new GoalExchangeResource(bestLocalResource, -maxAmount));
			goals.addFirst(new GoalNavigate(bestLocation));
			goals.addFirst(new GoalExchangeResource(bestLocalResource, maxAmount));

			World.logString("Actor " + name + " decides to buy " + maxAmount + " tons of " + bestLocalResource + " from " + location.name + " and sell at " + bestLocation.name);
		}

		return true;

	}


	public enum Job {
		TRADER;
	}
}
