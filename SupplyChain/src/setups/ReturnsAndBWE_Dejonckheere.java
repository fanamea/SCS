package setups;

import java.util.ArrayList;

import repast.simphony.random.RandomHelper;
import modules.Link;
import InventoryPolicies.ContinuousOUT;
import InventoryPolicies.PeriodicOUT;
import InventoryPolicies.PeriodicOUT_Returns;
import InventoryPolicies.PeriodicOUT_Returns2;
import agents.Business;
import agents.Customer;
import agents.MaterialSource;
import agents.Node;
import agents.Retailer;
import demandPattern.Constant;
import demandPattern.NormalDistribution;
import demandPattern.RandomWalk;

public class ReturnsAndBWE_Dejonckheere extends Setup{
	
	
	public ReturnsAndBWE_Dejonckheere(){
		
		super();
		
		//Structure
		sources.add(new MaterialSource(this, 6));
		retailers.add(new Retailer(this, 5));		
		retailers.add(new Retailer(this, 4));		
		retailers.add(new Retailer(this, 3));
		retailers.add(new Retailer(this, 2));
		customers.add(new Customer(this, new NormalDistribution(100.0, 40.0)));
		
		//Define Product
		
		//Links
		
		links.add(new Link(sources.get(0), retailers.get(0)));		
		
		links.add(new Link(retailers.get(0), retailers.get(1)));
		links.add(new Link(retailers.get(1), retailers.get(2)));
		links.add(new Link(retailers.get(2), retailers.get(3)));		
		links.add(new Link(retailers.get(3), customers.get(0)));
		
		for(Link link : links){
			link.setFixCost(40);
			double mean = 4;
			double var = 4;
			double alpha = mean*mean/var;
			double lambda = 1/(var/mean);
			link.setDistrDuration(RandomHelper.createGamma(alpha, lambda));
		}
		
		//Init
		init();
		
		
		//Parameters
		MaterialSource source = sources.get(0);
		source.setCapacity(10000);
		
		customers.get(0).setNegativeOrders(false);
		
		for(Retailer ret: retailers){
			
			ret.setPlanningPeriod(1);
			
			//Inventory
			ret.setHoldingCost(2);
			ret.setServiceLevel(0.95);
			ret.setInventoryPolicy(new PeriodicOUT_Returns2());
			ret.setInitialInventory(0);
			
			ret.setInformationSharing(false);
			ret.setReturnsAllowed(false);
		}
		
		
		
	}
	
}
