package sim_social.goal;

import sim_spatial.SolarBodyAuto;

public class GoalNavigate extends Goal {
	public SolarBodyAuto destination;
	
	public GoalNavigate(SolarBodyAuto destination) {
		this.destination = destination;
	}
}
