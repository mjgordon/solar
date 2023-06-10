package sim_social.goal;

import sim_social.Resource;

public class GoalExchangeResource extends Goal {
	public Resource resource;
	public int amount;
	
	public GoalExchangeResource(Resource resource, int amount) {
		this.resource = resource;
		this.amount = amount;
	}
}
